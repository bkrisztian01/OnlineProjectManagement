package hu.bme.aut.android.projectmanagerapp.ui.singleproject

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
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingleprojectBinding
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.TaskAdapter
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TaskResponseError
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TaskResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TasksViewModel
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel

class FragmentSingleProject : Fragment(),NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentSingleprojectBinding? = null
    private val binding get() = _binding!!
    private lateinit var project: Project
    private var projectid=-1
    private val singleProjectViewModel: SingleProjectViewModel by viewModels()
    private lateinit var token: String
    private val tasksViewModel: TasksViewModel by viewModels()
    private val loginViewModel : UserViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentSingleprojectBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.loading.hide()
        binding.scrollproject.setVisibility(View.GONE)
        binding.tvProjectName.setVisibility(View.GONE)
        if (arguments!=null) {
            val args: FragmentSingleProjectArgs by navArgs()
            projectid = args.projectid
            token=args.token
        }
        load()
        binding.toolbarsingleproject.inflateMenu(R.menu.menu_singleproject_toolbar)
        binding.toolbarsingleproject.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }
    override fun onResume() {
        super.onResume()


        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun load(){
        singleProjectViewModel.getSingleProject(token, projectid)?.observe(this) { singleProjectViewState ->
            render(singleProjectViewState)
        }
    }

    private fun render(result: SingleProjectViewState?) {
        when (result) {
            is InProgress -> {
                binding.loading.show()
            }
            is SingleProjectResponseSuccess -> {
                binding.loading.hide()
                project= result.data
                loadProject()
                binding.scrollproject.setVisibility(View.VISIBLE)
                binding.tvProjectName.setVisibility(View.VISIBLE)
            }
            is SingleProjectResponseError -> {
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
                                            R.string.server_error,
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    this.view?.let {
                        Snackbar.make(it, R.string.server_error, Snackbar.LENGTH_LONG).show()
                    }
                }

            }
            else -> {}
        }
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
                        .setTitle(R.string.help)
                        .setMessage(R.string.proj_info)
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
    private fun loadProject() {
        binding.tvProjectName.setText(project.name+" info")
        if(project.startDate!=null){
            val sdate= project.startDate
            binding.tStartDate.setText(sdate)
        }else
            binding.tStartDate.setText(R.string.unspecified_startdate)

        if (project.endDate!=null) {
            val edate =project.endDate
            binding.tvDeadline.setText(edate)
        }else
            binding.tvDeadline.setText(R.string.unspecified_deadline)

        if (project.estimatedTime!=null){
            binding.tLength.setText(project.estimatedTime.toString()+ " days")
        }else
            binding.tLength.setText(R.string.unspecified_length)
        binding.tDesc.setText(project.description)
        unfinishedtasks()
    }
    private fun unfinishedtasks(){
        tasksViewModel.getTasksByProject(token, projectid,1)?.observe(this){ taskViewState ->
            when (taskViewState) {
                is hu.bme.aut.android.projectmanagerapp.ui.tasks.InProgress -> {
                    binding.tvsDate.text="Loading..."
                }
                is TaskResponseSuccess -> {
                    var num=0
                    val itr = taskViewState.data?.listIterator()
                    if (itr != null) {
                        while (itr.hasNext()) {
                            if(itr.next().status!="Done")
                                num++
                        }
                    }
                    binding.tvsDate.text=num.toString()
                }
                is TaskResponseError -> {
                    binding.tvsDate.text="Couldn't get data from server!"

                }
            }
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentSingleProjectDirections.actionFragmentSingleProjectToFragmentUser(token))
                return true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentSingleProjectDirections.actionFragmentSingleProjectToFragmentProject(token))
                return true
            }
            R.id.taskspage->{
                return true
            }
            else->{
                return false
            }
        }
    }
}