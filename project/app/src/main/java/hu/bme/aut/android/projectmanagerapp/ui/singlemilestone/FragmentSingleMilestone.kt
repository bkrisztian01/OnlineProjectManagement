package hu.bme.aut.android.projectmanagerapp.ui.singlemilestone

import android.graphics.Color
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
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSinglemilestoneBinding
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TaskResponseError
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TaskResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.tasks.TasksViewModel
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel

class FragmentSingleMilestone : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentSinglemilestoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var milestone: Milestone
    private var tasks: ArrayList<Task> = ArrayList()
    private var projectid=-1
    private var milestoneid=-1
    private val singleMilestoneViewModel: SingleMilestoneViewModel by viewModels()
    private lateinit var token: String
    private val tasksViewModel: TasksViewModel by viewModels()
    private val loginViewModel : UserViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentSinglemilestoneBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.loading.hide()
        binding.scrollmilestone.visibility = View.GONE
        binding.tvMilestoneName.visibility = View.GONE
        if (arguments!=null) {
            val args: FragmentSingleMilestoneArgs by navArgs()
            projectid = args.projectid
            milestoneid=args.milestoneid
            token=args.token

        }
        binding.toolbarsinglemilestone.inflateMenu(R.menu.menu_single_milestone)
        binding.toolbarsinglemilestone.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        load()
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun load(){
        singleMilestoneViewModel.getMilestone(token,projectid,milestoneid)
            .observe(this) { singleMilestoneViewState ->
                render(singleMilestoneViewState)
            }
    }

    private fun render(result: SingleMilestoneViewState?) {
        when (result) {
            is InProgress -> {
                binding.loading.show()
            }
            is SingleMilestoneResponseSuccess -> {
                binding.loading.hide()
                milestone= result.data
                loadMilestone()
                binding.scrollmilestone.visibility = View.VISIBLE
                binding.tvMilestoneName.visibility = View.VISIBLE
            }
            is SingleMilestoneResponseError -> {
                if(result.exceptionMsg=="401") {
                    loginViewModel.getRefreshToken().observe(this) { loginViewState ->
                        run {
                            when (loginViewState) {
                                is hu.bme.aut.android.projectmanagerapp.ui.login.InProgress -> {}
                                is LoginResponseSuccess -> {
                                    token = loginViewState.data.toString()
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
                        .setTitle("Help")
                        .setMessage("Here is some information on the milestone!")
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
            R.id.menu_project->{
                binding.root.findNavController().navigate(FragmentSingleMilestoneDirections.actionFragmentSingleMilestoneToFragmentSingleProject(token,projectid))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun loadMilestone() {
        binding.tvMilestoneName.text = milestone.name+" info"
        val edate=milestone.deadline
        binding.tvDeadline.text = edate
        binding.tDesc.text = milestone.description
        unfinishedtasks()
        binding.tvStatus.text=milestone.status
        when (milestone.status) {
            "In Progress" -> {
                binding.mtstatus.setBackgroundColor(Color.parseColor("#F9CB9C"))
                binding.tvStatus.setBackgroundColor(Color.parseColor("#F9CB9C"))
            }
            "Done" -> {
                binding.mtstatus.setBackgroundColor(Color.parseColor("#B6D7A8"))
                binding.tvStatus.setBackgroundColor(Color.parseColor("#B6D7A8"))
            }
            "Stopped" -> {
                binding.mtstatus.setBackgroundColor(Color.parseColor("#EA9999"))
                binding.tvStatus.setBackgroundColor(Color.parseColor("#EA9999"))
            }
            "Not Started" -> {
                binding.mtstatus.setBackgroundColor(Color.parseColor("#CCCCCC"))
                binding.tvStatus.setBackgroundColor(Color.parseColor("#CCCCCC"))
            }
        }
    }
    private fun unfinishedtasks() {
        if(tasks.isNotEmpty())
            tasks.clear()
        tasksViewModel.getTasks(token,projectid,milestoneid,1)?.observe(this) { taskViewState ->
            when (taskViewState) {
                is hu.bme.aut.android.projectmanagerapp.ui.tasks.InProgress -> {
                    binding.tvsDate.text = "Loading..."
                }
                is TaskResponseSuccess -> {
                    val itr = taskViewState.data.listIterator()
                    while (itr.hasNext()) {
                        tasks.add(itr.next())
                    }
                    val itr1 = tasks.listIterator()
                    var num=0
                    while (itr1.hasNext()) {
                        if(itr1.next().status!="Done")
                            num++
                    }
                    binding.tvsDate.text=num.toString()
                }
                is TaskResponseError -> {
                    binding.tvUnfinished.text = "Couldn't get unfinished task number!"

                }
            }
        }

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentSingleMilestoneDirections.actionFragmentSingleMilestoneToFragmentUser(token))
                return true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentSingleMilestoneDirections.actionFragmentSingleMilestoneToFragmentProject(token))
                return true
            }
            R.id.taskspage->{
                //binding.root.findNavController().navigate(FragmentSingleMilestoneDirections.actionFragmentSingleMilestoneToFragmentUpcomingTasks(user))
                return true
            }
            else->{
                return false
            }
        }
    }
}