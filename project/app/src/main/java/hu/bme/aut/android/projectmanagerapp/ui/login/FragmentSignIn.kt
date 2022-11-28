package hu.bme.aut.android.projectmanagerapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSigninBinding
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel

class FragmentSignIn : Fragment() {
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.loading.hide()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignUp1.setOnClickListener {
            var error = false
            if (binding.editTextUser.text.toString().isEmpty()) {
                binding.editTextUser.requestFocus()
                binding.editTextUser.error = "Please enter a username"
            } else if (binding.editTextPw.text.toString().isEmpty()) {
                binding.editTextPw.requestFocus()
                binding.editTextPw.error = "Please enter a password"
            } else {
                if (binding.editTextEmailadress.text.isEmpty() || !binding.editTextEmailadress.text.contains("@") || !binding.editTextEmailadress.text.contains(".")) {
                    binding.editTextPw.requestFocus()
                    binding.editTextEmailadress.error = "Please enter your e-mail address"
                    error = true
                }
                if (binding.editTextName.text.isEmpty()) {
                    binding.editTextPw.requestFocus()
                    binding.editTextName.error = "Please enter your name address"
                    error = true
                }
            }

            if (!error) {
                userViewModel.createUser(binding.editTextUser.text.toString(), binding.editTextPw.text.toString(), binding.editTextName.text.toString(), binding.editTextEmailadress.text.toString())?.observe(viewLifecycleOwner) { signinViewState ->
                    run {
                        when (signinViewState) {
                            is InProgressSignIn -> {
                                binding.editTextPw.visibility = View.GONE
                                binding.textViewUser.visibility = View.GONE
                                binding.textViewEmail.visibility = View.GONE
                                binding.textViewPw.visibility = View.GONE
                                binding.textViewName.visibility = View.GONE
                                binding.editTextName.visibility = View.GONE
                                binding.editTextEmailadress.visibility = View.GONE
                                binding.editTextUser.visibility = View.GONE
                                binding.loading.show()
                            }
                            is SignInResponseSuccess -> {
                                binding.loading.hide()
                                if (signinViewState.data == "200") {
                                    Toast.makeText(context, "You created an account!", Toast.LENGTH_LONG).show()
                                    binding.root.findNavController().navigate(FragmentSignInDirections.actionFragmentSignInToFragmentWelcome())
                                } else
                                    Toast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                binding.loading.hide()
                                binding.editTextPw.visibility = View.VISIBLE
                                binding.textViewUser.visibility = View.VISIBLE
                                binding.textViewEmail.visibility = View.VISIBLE
                                binding.textViewPw.visibility = View.VISIBLE
                                binding.textViewName.visibility = View.VISIBLE
                                binding.editTextName.visibility = View.VISIBLE
                                binding.editTextEmailadress.visibility = View.VISIBLE
                                binding.editTextUser.visibility = View.VISIBLE
                                Toast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }
}