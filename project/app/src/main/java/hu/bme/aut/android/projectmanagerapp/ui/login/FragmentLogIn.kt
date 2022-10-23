package hu.bme.aut.android.projectmanagerapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentLoginBinding
import hu.bme.aut.android.projectmanagerapp.model.User

class FragmentLogIn : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
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
                binding.root.findNavController().navigate(FragmentLogInDirections.actionFragmentLogInToFragmentProject(
                    /*User(1,binding.editTextUser.text.toString(),binding.editTextPw.text.toString(),binding.editTextUser.text.toString()*/)
                )
            }
        }
    }
}