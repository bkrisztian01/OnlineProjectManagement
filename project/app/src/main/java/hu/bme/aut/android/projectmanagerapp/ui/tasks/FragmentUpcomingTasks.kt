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
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.UpcomingTaskAdapter
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.projects.*
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel
import java.util.ArrayList

class FragmentUpcomingTasks: Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private val projects: ArrayList<Project> = ArrayList<Project>()
    private val tasks: ArrayList<Task> = ArrayList<Task>()
    private var _binding: FragmentUpcomingTasksBinding? = null
    private val binding get() = _binding!!
    private val tasksViewModel : TasksViewModel by viewModels()
    private val loginViewModel : UserViewModel by viewModels()
    private lateinit var token: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentUpcomingTasksBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentUpcomingTasksArgs by navArgs()
            token=args.token
        }
        load()
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
                        .setMessage("Here, you can find tasks\ndue in the next 2 weeks.")
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

        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setCheckedItem(R.id.taskspage)
        navigationView.setNavigationItemSelectedListener(this)
    }
    private fun load(){
        tasksViewModel.getUpcomingTasks(token)?.observe(this) { tasksViewState ->
            render(tasksViewState)
        }
    }


    private fun render(result: TasksViewState) {
        when (result) {
            is InProgress -> {binding.loading.show()}
            is TaskResponseSuccess ->{
                binding.loading.hide()
                val itr = result.data.listIterator()
                while (itr.hasNext()) {
                    tasks.add(itr.next())

                }
                val recyclerView = activity?.findViewById(R.id.rvUpcoming) as RecyclerView
                tasks.sortBy {it.deadline }
                val adapter = UpcomingTaskAdapter(token,tasks,projects)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this.activity)
            }
            is TaskResponseError ->{
                if(result.exceptionMsg=="401") {
                    loginViewModel.getRefreshToken().observe(this) { loginViewState ->
                        run {
                            when (loginViewState) {
                                is hu.bme.aut.android.projectmanagerapp.ui.login.InProgress -> {}
                                is LoginResponseSuccess -> {
                                    token = loginViewState.data.accessToken
                                    load()
                                }
                                is LoginResponseError -> {
                                    this.view?.let {
                                        Snackbar.make(
                                            it,
                                            "Couldn't reach server!",
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }

                        }
                    }
                }
                else{
                    this.view?.let {
                        Snackbar.make(it, "Couldn't reach server!", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentUpcomingTasksDirections.actionFragmentUpcomingTasksToFragmentUser(token))
                true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentUpcomingTasksDirections.actionFragmentUpcomingTasksToFragmentProject(token))
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