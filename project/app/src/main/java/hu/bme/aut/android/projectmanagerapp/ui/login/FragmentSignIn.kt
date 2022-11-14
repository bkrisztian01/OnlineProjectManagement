package hu.bme.aut.android.projectmanagerapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSigninBinding

class FragmentSignIn : Fragment() {
    private var _binding: FragmentSigninBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignUp1.setOnClickListener {
            var error=false
            if (binding.editTextUser.text.toString().isEmpty()) {
                binding.editTextUser.requestFocus()
                binding.editTextUser.error = "Please enter a username"
                error=true
            }
            if (binding.editTextPw.text.toString().isEmpty()) {
                binding.editTextPw.requestFocus()
                binding.editTextPw.error = "Please enter a password"
                error=true
            }
            if(binding.editTextEmailadress.text.isEmpty()){
                binding.editTextPw.requestFocus()
                binding.editTextEmailadress.error = "Please enter your e-mail adress"
                error=true
            }
            if(binding.editTextName.text.isEmpty()) {
                binding.editTextPw.requestFocus()
                binding.editTextName.error = "Please enter your name adress"
                error=true
            }
            if(error){} else{
                Toast.makeText(context, "You created an account!", Toast.LENGTH_LONG).show()
                binding.root.findNavController().navigate(FragmentSignInDirections.actionFragmentSignInToFragmentWelcome())

            }
        }
    }
}