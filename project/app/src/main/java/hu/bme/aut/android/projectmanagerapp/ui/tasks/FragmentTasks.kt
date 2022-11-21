package hu.bme.aut.android.projectmanagerapp.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentTasksBinding
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.MilestoneAdapter
import hu.bme.aut.android.projectmanagerapp.ui.adapter.ProjectAdapter
import hu.bme.aut.android.projectmanagerapp.ui.adapter.TaskAdapter
import hu.bme.aut.android.projectmanagerapp.ui.milestone.FragmentMilestoneDirections
import hu.bme.aut.android.projectmanagerapp.ui.singletask.FragmentSingleTaskArgs
import java.util.*
import kotlin.collections.ArrayList

class FragmentTasks : Fragment(),NavigationView.OnNavigationItemSelectedListener {
    private val tasks: ArrayList<Task> = ArrayList<Task>()
    private lateinit var project: Project
    private var _binding: FragmentTasksBinding? = null
    private lateinit var milestone: Milestone
    private val binding get() = _binding!!
    private lateinit var user : User
    lateinit var adapter: TaskAdapter
    //private lateinit var token:String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentTasksArgs by navArgs()
            project = args.project
            user=args.user
            milestone=args.milestone
            //token=args.token
            binding.tvTasks.setText("Tasks in "+ milestone.name)
        }
        binding.toolbartasks.inflateMenu(R.menu.menu_task_toolbar)
        binding.toolbartasks.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }
    override fun onResume() {
        super.onResume()
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val searchView=activity!!.findViewById(R.id.menu_search) as SearchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return false
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{

        val context = this.activity
        return when (item.itemId){
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
                binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentSingleProject(project,user))
                return true
            }
            R.id.menu_item->{
                val drawer = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
                drawer.open()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated(view,savedInstanceState)
        val recyclerView = activity?.findViewById(R.id.rvTasks) as RecyclerView

        if(!tasks.isEmpty())
            tasks.clear()
        val itr = milestone.tasks.listIterator()
        if (itr != null) {
            while (itr.hasNext()) {
                tasks.add(itr.next())

            }
        }
        adapter = TaskAdapter(tasks,project,user)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.activity)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentUser(user))
                return true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentProject(user))
                return true
            }
            R.id.taskspage->{
                binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentUpcomingTasks(user))
                return true
            }
            else->{
                return false
            }
        }
    }


    private fun filter(text: String) {
        val filteredlist = java.util.ArrayList<Task>()
        for (item in tasks) {
            if (item.name.lowercase().contains(text.lowercase(),true)) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            adapter.filterList(null)
        } else {
            adapter.filterList(filteredlist)
        }
    }

}