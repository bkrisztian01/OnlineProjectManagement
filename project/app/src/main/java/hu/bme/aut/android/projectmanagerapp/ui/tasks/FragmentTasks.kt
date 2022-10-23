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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentTasksBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.ProjectAdapter
import hu.bme.aut.android.projectmanagerapp.ui.adapter.TaskAdapter
import hu.bme.aut.android.projectmanagerapp.ui.singletask.FragmentSingleTaskArgs
import java.util.*
import kotlin.collections.ArrayList

class FragmentTasks : Fragment() {
    private val tasks: ArrayList<Task> = ArrayList<Task>()
    private lateinit var project: Project
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentTasksArgs by navArgs()
            project = args.project
            binding.tvTitle.setText("Tasks in "+ project.name)
        }
        binding.toolbartasks.inflateMenu(R.menu.menu_task_toolbar)
        binding.toolbartasks.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val args: FragmentSingleTaskArgs by navArgs()
        //val user=args.user
        val context = this.activity
        return when (item.itemId){
            R.id.menu_sign_out->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Signing out?")
                        .setMessage(R.string.are_you_sure_want_to_sign_out)
                        .setPositiveButton(R.string.yes) { _, _ ->
                            Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT).show()
                            binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentWelcome()) }
                        .setNegativeButton(R.string.no, null)

                        .show()

                }
                return true
            }
            R.id.menu_help->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setMessage(R.string.pick_project)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            R.id.menu_project->{
                binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentSingleProject(project))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated(view,savedInstanceState)
        val recyclerView = activity?.findViewById(R.id.rvTasks) as RecyclerView

        //tasks = Task.createTaskList(10)
        tasks.add(Task(12,"Task50","desc","In Progress",
            Date(2002,12,21,23,59),
            Date(2002,12,21,23,59),12,12,ArrayList<Task>(),ArrayList<User>()
        ))
        val adapter = TaskAdapter(tasks,project)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.activity)

    }


}