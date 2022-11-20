package hu.bme.aut.android.projectmanagerapp.ui.projects


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentProjectBinding
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.ProjectAdapter
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksArgs
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class FragmentProject : Fragment(),NavigationView.OnNavigationItemSelectedListener {
    private val projects: ArrayList<Project> = ArrayList<Project>()
    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProjectAdapter
    private lateinit var user: User
    private val projectViewModel : ProjectViewModel by viewModels()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentProjectArgs by navArgs()
            user = args.user
        }

        binding.toolbar.inflateMenu(R.menu.menu_project_toolbar)
        binding.toolbar.setOnMenuItemClickListener {
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
            R.id.app_bar_search->{
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(context)
                        .setTitle("Logging out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton(R.string.yes) { _, _ ->
                            Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT)
                                .show()
                            binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToFragmentWelcome()) }
                        .setNegativeButton(R.string.no, null)

                        .show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    override fun onResume() {
        super.onResume()
        if(projects.isNotEmpty())
            projects.clear()
        projectViewModel.getProjects()?.observe(this) { projectsViewState ->
            render(projectsViewState)
        }
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setCheckedItem(R.id.homepage)
        navigationView.setNavigationItemSelectedListener(this)

    }

    private fun filter(text: String) {
        val filteredlist = ArrayList<Project>()
        for (item in projects) {
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



    private fun render(result: ProjectsViewState) {
        when (result) {
            is InProgress -> {}
            is ProjectsResponseSuccess -> {
                binding.loading.hide()
                val itr = result.data?.listIterator()
                if (itr != null) {
                    while (itr.hasNext()) {
                        projects.add(itr.next())
                    }
                }
                val recyclerView = activity?.findViewById(R.id.rvProjects) as RecyclerView
                adapter = ProjectAdapter(user, projects)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this.activity)

                val searchView = activity?.findViewById(R.id.app_bar_search) as SearchView
                searchView.clearFocus()
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            is ProjectsResponseError -> {
                this.view?.let {
                    Snackbar.make(it, "Couldn't reach server!", Snackbar.LENGTH_LONG)
                        .setAction("Retry") {
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
                binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToFragmentUser(user))
                true
            }
            R.id.homepage->{
                true
            }
            R.id.taskspage->{
                binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToFragmentUpcomingTasks(user))
                true
            }
                else->{
                    false
                }
            }
        }



        }