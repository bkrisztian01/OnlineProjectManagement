package hu.bme.aut.android.projectmanagerapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentLoginBinding
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.ProjectAdapter
import hu.bme.aut.android.projectmanagerapp.ui.login.InProgress
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel

class FragmentLogIn : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: UserViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.loading.hide()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonLogin.setOnClickListener {
            if (binding.editTextUser.text.toString().isEmpty()) {
                binding.editTextUser.requestFocus()
                binding.editTextUser.error = "Please enter your username"
            } else if (binding.editTextPw.text.toString().isEmpty()) {
                binding.editTextPw.requestFocus()
                binding.editTextPw.error = "Please enter your password"
            } else{
                loginViewModel.login(binding.editTextUser.text.toString(), binding.editTextPw.text.toString())?.observe(this.viewLifecycleOwner){
                    loginviewState->render(loginviewState)
                }

            }
        }
    }

    private fun render(result: LoginViewState?) {
        when (result) {
            is InProgress -> {
                binding.textViewPw.visibility = View.GONE
                binding.textViewUser.visibility = View.GONE
                binding.mtpw.visibility = View.GONE
                binding.mtuser.visibility = View.GONE
                binding.buttonLogin.visibility = View.GONE
                binding.loading.show()
            }
            is LoginResponseSuccess -> {
                val token=result.data.accessToken
                binding.root.findNavController().navigate(FragmentLogInDirections.actionFragmentLogInToFragmentProject(token))
            }
            is LoginResponseError -> {
                this.view?.let {
                    binding.loading.hide()
                    binding.textViewPw.visibility = View.VISIBLE
                    binding.textViewUser.visibility = View.VISIBLE
                    binding.mtpw.visibility = View.VISIBLE
                    binding.mtuser.visibility = View.VISIBLE
                    binding.buttonLogin.visibility = View.VISIBLE
                }
                if (result.exceptionMsg=="401")
                    this.view?.let { Snackbar.make(it, R.string.invalid_pw_or_username, Snackbar.LENGTH_LONG).show() }
                else
                    this.view?.let { Snackbar.make(it, R.string.server_error, Snackbar.LENGTH_LONG).show() }


            }
            else -> {}
        }


    }
}
