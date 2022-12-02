package hu.bme.aut.android.projectmanagerapp.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
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
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentTasksBinding
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.ui.adapter.TaskAdapter
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel
import kotlin.collections.ArrayList

class FragmentTasks : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private val tasks: ArrayList<Task> = ArrayList()
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: TaskAdapter
    private lateinit var token: String
    private lateinit var milestonename: String
    private var projectid = -1
    private var milestoneid = -1
    private val tasksViewModel: TasksViewModel by viewModels()
    private val loginViewModel: UserViewModel by viewModels()
    private lateinit var manager: LinearLayoutManager
    private var pageNumber = 1
    private var isScrolling = false
    private lateinit var recyclerView: RecyclerView
    private var more = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments != null) {
            val args: FragmentTasksArgs by navArgs()
            projectid = args.projectid
            token = args.token
            milestoneid = args.milestoneid
            milestonename = args.milestonename
            token = args.token

        }
        binding.tvTasks.setText("Tasks in " + milestonename)
        binding.loading.hide()
        if (milestoneid == -1)
            binding.toolbartasks.inflateMenu(R.menu.menu_milestone_toolbar)
        else
            binding.toolbartasks.inflateMenu(R.menu.menu_task_toolbar)
        binding.toolbartasks.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        more = true
        pageNumber = 1
        val navigationView = activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val searchView: SearchView = activity!!.findViewById(R.id.menu_search) as SearchView
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

        recyclerView = activity?.findViewById(R.id.rvTasks) as RecyclerView
        manager = LinearLayoutManager(this.activity)
        recyclerView.layoutManager = manager

        if (tasks.isNotEmpty())
            tasks.clear()
        adapter = TaskAdapter(tasks, projectid, token)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItems = manager.childCount
                val totalItems = manager.itemCount
                val scrollOutItems = manager.findFirstVisibleItemPosition()
                if (more && isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false
                    pageNumber++
                    loadTasks()
                }
            }
        })

        loadTasks()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val context = this.activity
        return when (item.itemId) {
            R.id.menu_help -> {
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setMessage(R.string.pick_project)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            R.id.menu_project -> {
                binding.root.findNavController().navigate(
                    FragmentTasksDirections.actionFragmentTasksToFragmentSingleProject(
                        token,
                        projectid
                    )
                )
                return true
            }
            R.id.menu_item -> {
                val drawer = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
                drawer.open()
                return true
            }
            R.id.menu_milestones -> {
                binding.root.findNavController().navigate(
                    FragmentTasksDirections.actionFragmentTasksToFragmentSingleMilestone(
                        projectid,
                        milestoneid,
                        token
                    )
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun loadTasks() {
        if (milestoneid != -1) {
            tasksViewModel.getTasks(token, projectid, milestoneid, pageNumber)
                ?.observe(this) { taskViewState ->
                    render(taskViewState)
                }
        } else
            tasksViewModel.getTasksByProject(token, projectid, pageNumber)
                ?.observe(this) { taskViewState ->
                    render(taskViewState)
                }
    }


    private fun render(result: TasksViewState) {
        when (result) {
            is InProgress -> {
                binding.loading.show()
            }
            is TaskResponseSuccess -> {
                binding.loading.hide()
                if (result.data.isNotEmpty()) {
                    val itr = result.data?.listIterator()
                    if (itr != null) {
                        while (itr.hasNext()) {
                            tasks.add(itr.next())
                        }
                    }
                    adapter.notifyDataSetChanged()

                } else {
                    pageNumber--
                    more = false
                }

            }
            is TaskResponseError -> {
                binding.loading.hide()
                if (result.exceptionMsg == "401") {
                    loginViewModel.getRefreshToken().observe(this) { loginViewState ->
                        run {
                            when (loginViewState) {
                                is hu.bme.aut.android.projectmanagerapp.ui.login.InProgress -> {}
                                is LoginResponseSuccess -> {
                                    token = loginViewState.data.accessToken
                                    loadTasks()
                                }
                                is LoginResponseError -> {
                                    this.view?.let {
                                        Snackbar.make(
                                            it,
                                            R.string.server_error,
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    this.view?.let {
                        Snackbar.make(it, R.string.server_error, Snackbar.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accountpage -> {
                binding.root.findNavController()
                    .navigate(FragmentTasksDirections.actionFragmentTasksToFragmentUser(token))
                return true
            }
            R.id.homepage -> {
                binding.root.findNavController()
                    .navigate(FragmentTasksDirections.actionFragmentTasksToFragmentProject(token))
                return true
            }
            R.id.taskspage -> {
                binding.root.findNavController().navigate(
                    FragmentTasksDirections.actionFragmentTasksToFragmentUpcomingTasks(token)
                )
                return true
            }
            else -> {
                return false
            }
        }
    }


    private fun filter(text: String) {
        val filteredlist = java.util.ArrayList<Task>()
        for (item in tasks) {
            if (item.name.lowercase().contains(text.lowercase(), true)) {
                filteredlist.add(item)
            }
        }
        adapter.filterList(filteredlist)

    }

}