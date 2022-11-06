package hu.bme.aut.android.projectmanagerapp.ui.user

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingleprojectBinding
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentTasksBinding
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentUserBinding
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.projects.FragmentProjectDirections
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksArgs
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksDirections

class DialogFragmentUser: DialogFragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?{
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        setUp()
        binding.btnCancel.setOnClickListener { dismiss() }
        val context=this.context
        if(context!=null) {
            binding.btnSignOut.setOnClickListener {
                androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Signing out?")
                    .setMessage(R.string.are_you_sure_want_to_sign_out)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT).show()
                        findNavController(this).navigate(DialogFragmentUserDirections.actionDialogFragmentUserToFragmentWelcome())
                    }
                    .setNegativeButton(R.string.no, null)

                    .show()
            }
        }
        return view
    }

    private fun setUp(){
        if (arguments!=null) {
            val args: DialogFragmentUserArgs by navArgs()
            user=args.user
            binding.tvtUser2.text=user.username
            binding.tvtName.text=user.fullname
        }
    }



}
