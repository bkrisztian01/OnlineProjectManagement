package hu.bme.aut.android.projectmanagerapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentWelcomeBinding
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksDirections
import kotlin.system.exitProcess

class FragmentWelcome : Fragment(){
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignIn.setOnClickListener {
            binding.root.findNavController().navigate(
            FragmentWelcomeDirections.actionFragmentWelcomeToFragmentLogIn())
        }
        binding.buttonSignUp.setOnClickListener {
            binding.root.findNavController().navigate(FragmentWelcomeDirections.actionFragmentWelcomeToFragmentSignIn())
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(context)
                        .setTitle("Exit")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton(R.string.yes) { _, _ ->
                            Toast.makeText(context, "Goodbye!", Toast.LENGTH_SHORT).show()
                            exitProcess(0)
                             }
                        .setNegativeButton(R.string.no, null)

                        .show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}