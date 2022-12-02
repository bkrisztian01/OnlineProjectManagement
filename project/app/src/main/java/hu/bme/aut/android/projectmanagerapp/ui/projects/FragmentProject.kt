package hu.bme.aut.android.projectmanagerapp.ui.projects


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentProjectBinding
import hu.bme.aut.android.projectmanagerapp.ui.adapter.ProjectAdapter
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel


class FragmentProject : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private val projects: ArrayList<Project> = ArrayList()
    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProjectAdapter
    private val projectViewModel: ProjectViewModel by viewModels()
    private val loginViewModel: UserViewModel by viewModels()
    private lateinit var token: String
    private lateinit var manager: LinearLayoutManager
    private var pageNumber = 1
    private var isScrolling = false
    private var more = true
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments != null) {
            val args: FragmentProjectArgs by navArgs()
            token = args.token
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val context = this.activity
        return when (item.itemId) {

            R.id.menu_help -> {
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

            R.id.menu_item -> {
                val drawer = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
                drawer.open()
                return true
            }
            R.id.app_bar_search -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(context)
                        .setTitle("Logging out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton(R.string.yes) { _, _ ->
                            Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT)
                                .show()
                            binding.root.findNavController()
                                .navigate(FragmentProjectDirections.actionFragmentProjectToFragmentWelcome())
                        }
                        .setNegativeButton(R.string.no, null)

                        .show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        more = true
        pageNumber = 1
        manager = LinearLayoutManager(this.activity)
        recyclerView = activity?.findViewById(R.id.rvProjects) as RecyclerView
        recyclerView.layoutManager = manager
        if (projects.isNotEmpty())
            projects.clear()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
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
                    load()

                }
            }
        })
        load()
        adapter = ProjectAdapter(projects, token)
        recyclerView.adapter = adapter
        val navigationView = activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setCheckedItem(R.id.homepage)
        navigationView.setNavigationItemSelectedListener(this)
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

    private fun load() {
        projectViewModel.getProjects(token, pageNumber)?.observe(this) { projectsViewState ->
            render(projectsViewState)
        }
    }


    private fun filter(text: String) {
        val filteredlist = ArrayList<Project>()
        for (item in projects) {
            if (item.name.lowercase().contains(text.lowercase(), true)) {
                filteredlist.add(item)
            }
        }
        adapter.filterList(filteredlist)
    }


    private fun render(result: ProjectsViewState) {
        when (result) {
            is InProgress -> {
                binding.loading.show()
            }
            is ProjectsResponseSuccess -> {
                binding.loading.hide()
                val itr = result.data.listIterator()
                if (result.data.isNotEmpty()) {
                    while (itr.hasNext()) {
                        val item = itr.next()
                        projects.add(item)
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    pageNumber--
                    more = false
                }

            }
            is ProjectsResponseError -> {
                binding.loading.hide()
                if (result.exceptionMsg == "401") {
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
        return when (item.itemId) {
            R.id.accountpage -> {
                binding.root.findNavController()
                    .navigate(FragmentProjectDirections.actionFragmentProjectToFragmentUser(token))
                true
            }
            R.id.homepage -> {
                true
            }
            R.id.taskspage -> {
                binding.root.findNavController().navigate(
                    FragmentProjectDirections.actionFragmentProjectToFragmentUpcomingTasks(token)
                )
                true
            }
            else -> {
                false
            }
        }
    }


}