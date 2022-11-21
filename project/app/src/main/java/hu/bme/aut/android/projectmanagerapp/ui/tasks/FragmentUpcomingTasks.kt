package hu.bme.aut.android.projectmanagerapp.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentUpcomingTasksBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.UpcomingTaskAdapter
import hu.bme.aut.android.projectmanagerapp.ui.projects.*
import hu.bme.aut.android.projectmanagerapp.ui.projects.InProgress
import java.util.ArrayList

class FragmentUpcomingTasks: Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private val projects: ArrayList<Project> = ArrayList<Project>()
    private val tasks: ArrayList<Task> = ArrayList<Task>()
    private var _binding: FragmentUpcomingTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private val projectViewModel : ProjectViewModel by viewModels()
    //private lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentUpcomingTasksBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentUpcomingTasksArgs by navArgs()
            user = args.user
            //token=args.token
        }

        binding.toolbarupcomingtasks.inflateMenu(R.menu.menu_project_toolbar)
        binding.toolbarupcomingtasks.setOnMenuItemClickListener {
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

            R.id.menu_help->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setIcon(R.drawable.ic_help_outline)
                        .setMessage(R.string.pick)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
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



    override fun onResume() {
        super.onResume()
        if(projects.isNotEmpty())
            projects.clear()
        if(tasks.isNotEmpty())
            tasks.clear()
        projectViewModel.getProjects(/*token*/)?.observe(this) { projectsViewState ->
            render(projectsViewState)
        }
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setCheckedItem(R.id.taskspage)
        navigationView.setNavigationItemSelectedListener(this)


    }
    private fun render(result: ProjectsViewState) {
        when (result) {
            is InProgress -> {binding.loadingview.show()}
            is ProjectsResponseSuccess ->{
                binding.loadingview.hide()
                val itr = result.data.listIterator()
                while (itr.hasNext()) {
                    itr.next().tasks.forEach {
                        if(it.status!="Done") {
                            projects.add(itr.next())
                            tasks.add(it)
                        }

                    }
                    /*
                    val itr1= itr.next().tasks.listIterator()
                    while(itr1.hasNext()){
                       /* if(itr1.next().assignees.contains(user)) {*/
                        /*if(itr1.next().status.length>0) {*/
                            projects.add(itr.next())
                            tasks.add(itr1.next())
                        /*}*/
                        /*}*/*/

                }
                val recyclerView = activity?.findViewById(R.id.rvUpcoming) as RecyclerView
                tasks.sortBy {it.deadline }
                val adapter = UpcomingTaskAdapter(user,tasks,projects)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this.activity)
            }
            is ProjectsResponseError ->{
                this.view?.let {
                    Snackbar.make(it, R.string.text_label, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_text) {
                            render(result)
                        }
                        .show()
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentUpcomingTasksDirections.actionFragmentUpcomingTasksToFragmentUser(user))
                true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentUpcomingTasksDirections.actionFragmentUpcomingTasksToFragmentProject(user))
                true
            }
            R.id.taskspage->{
                true
            }
            else->{
                false
            }
        }
    }


}