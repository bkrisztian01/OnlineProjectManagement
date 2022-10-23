package hu.bme.aut.android.projectmanagerapp.ui.singletask

import android.graphics.Color
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
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingletaskBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksDirections

class FragmentSingleTask : Fragment() {
    private lateinit var project: Project
    private lateinit var task: Task
    private var _binding: FragmentSingletaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var user : User
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentSingletaskBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentSingleTaskArgs by navArgs()
            project = args.project
            task=args.task
            user=args.user

        }
        binding.toolbarsingletask.inflateMenu(R.menu.menu_singleproject_toolbar)
        binding.toolbarsingletask.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        loadTask()
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
                binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskToDialogFragmentUser(user))
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
    private fun  loadTask(){
        when (task.status) {
            "In Progress" -> binding.tStatus.setBackgroundColor(Color.parseColor("#F9CB9C"))
            "Finished" ->binding.tStatus.setBackgroundColor(Color.parseColor("#B6D7A8"))
            "Stuck" ->binding.tStatus.setBackgroundColor(Color.parseColor("#EA9999"))
            "Not started" ->binding.tStatus.setBackgroundColor(Color.parseColor("#CCCCCC"))
            else->{
            }
        }
        binding.tStatus.setText(task.status)
        binding.tvTaskName.setText(task.name+" info")
        binding.tStartDate.setText(task.startDate.toString())
        binding.tEndDate.setText(task.endDate.toString())
        binding.ttDesc.setText(task.desc)


    }
}