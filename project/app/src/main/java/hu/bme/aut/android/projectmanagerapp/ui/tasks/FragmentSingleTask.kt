package hu.bme.aut.android.projectmanagerapp.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingleprojectBinding
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingletaskBinding
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.ui.projects.FragmentSingleProjectDirections

class FragmentSingleTask : Fragment() {
    private var projectid: Int  = -1
    private var taskid: Int  = -1
    private var _binding: FragmentSingletaskBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentSingletaskBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentSingleTaskArgs by navArgs()
            projectid = args.projectid
            taskid=args.taskid
            binding.tvTaskName.setText("Task "+ taskid.toString()+" info")
        }
        binding.toolbarsingletask.inflateMenu(R.menu.menu_singleproject_toolbar)
        binding.toolbarsingletask.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val context = this.activity
        return when (item.itemId){
            R.id.menu_sign_out->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Signing out?")
                        .setMessage(R.string.are_you_sure_want_to_sign_out)
                        .setPositiveButton(R.string.yes) { _, _ ->
                            Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT).show()
                            binding.root.findNavController().navigate(
                                FragmentSingleTaskDirections.actionFragmentSingleTaskToFragmentWelcome()) }
                        .setNegativeButton(R.string.no, null)

                        .show()
                }
                return true
            }
            R.id.menu_help->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setMessage("Here is some information on the project!")
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}