package hu.bme.aut.android.projectmanagerapp.ui.singletask

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingletaskBinding
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.PreReqAdapter
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.projects.ProjectViewModel
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.SingleProjectResponseError
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.SingleProjectResponseSuccess
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.SingleProjectViewModel
import hu.bme.aut.android.projectmanagerapp.ui.user.UserViewModel
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class FragmentSingleTask : Fragment(), AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private var taskid=-1
    private lateinit var task: Task
    private var _binding: FragmentSingletaskBinding? = null
    private val binding get() = _binding!!
    private val singleTaskViewModel: SingleTaskViewModel by viewModels()
    private lateinit var token: String
    private var projid=-1
    private var role: String=""
    private var prereqtasks: ArrayList<Task> = ArrayList()
    private lateinit var adapter: PreReqAdapter
    private val loginViewModel : UserViewModel by viewModels()
    private val projectViewModel: SingleProjectViewModel by viewModels()



    val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
        position = Position.Relative(0.5, 0.3)
    )

    private var setting=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentSingletaskBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.loading.hide()
        binding.tvTaskName.setVisibility(View.GONE)
        binding.scrolltask.setVisibility(View.GONE)



        if (arguments!=null) {
            val args: FragmentSingleTaskArgs by navArgs()
            taskid=args.taskid
            token=args.token
            projid=args.projectid
        }
        binding.toolbarsingletask.inflateMenu(R.menu.menu_singleproject_toolbar)
        binding.toolbarsingletask.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        load()


        binding.spStatus.adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.status_array))
        binding.spStatus.onItemSelectedListener = this
        binding.spStatus.setSelection(setting)
        return view
        }

    private fun load() {
        singleTaskViewModel.getSingleTask(token,projid,taskid)?.observe(this) { singleTaskViewState ->
            render(singleTaskViewState)
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
                        .setMessage("Here is some information on the task!")
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
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val manager = LinearLayoutManager(this.activity)
        val recyclerView = activity?.findViewById(R.id.rvPreReq) as RecyclerView
        recyclerView.layoutManager = manager
    }

    private fun render(result: SingleTaskViewState?) {
        when (result) {
            is InProgress -> {
                binding.loading.show()
            }
            is SingleTaskResponseSuccess -> {
                task= result.data
                binding.loading.hide()
                binding.savebtn.setOnClickListener{
                    task.status=binding.spStatus.selectedItem.toString()
                    if(binding.spStatus.selectedItem.toString()=="Done") {
                        binding.konfettiView.bringToFront()
                        binding.konfettiView.start(party)
                    }
                    singleTaskViewModel.updateTask(token,projid,task)?.observe(this){ updateViewState ->
                        when(updateViewState){
                            is InProgressUpdate->{}
                            is UpdateSuccess->{
                                if(updateViewState.data=="200")
                                    Toast.makeText(context, "Task info changed!", Toast.LENGTH_SHORT).show()
                                else{
                                    loadTask()
                                    binding.spStatus.setSelection(setting)
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else->{
                                loadTask()
                                binding.spStatus.setSelection(setting)
                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
                if(prereqtasks.isNotEmpty())
                    prereqtasks.clear()
                binding.discardbtn.setOnClickListener {
                    loadTask()
                    binding.spStatus.setSelection(setting)
                }
                loadTask()
                binding.scrolltask.setVisibility(View.VISIBLE)
                binding.tvTaskName.setVisibility(View.VISIBLE)
                projectViewModel.getSingleProject(token,projid)?.observe(this){ projectViewState->
                    run {
                        when(projectViewState){
                            is hu.bme.aut.android.projectmanagerapp.ui.singleproject.InProgress->{}
                            is SingleProjectResponseSuccess->{
                                if(projectViewState.data.userRole=="Member"){
                                    binding.spStatus.isEnabled=false
                                    binding.savebtn.isEnabled=false
                                    binding.savebtn.setBackgroundColor(Color.parseColor("#CCCCCC"))
                                }

                            }
                            is SingleProjectResponseError->{
                                role="Couldn't load role"
                            }
                        }

                    }

                }


            }
            is SingleTaskResponseError -> {
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
                else{
                    this.view?.let {
                        Snackbar.make(it, R.string.server_error, Snackbar.LENGTH_LONG).show()
                    }
                }

            }
            else -> {}
        }
    }


    private fun  loadTask(){
        when (task.status) {
            "In Progress" -> {
                binding.spStatus.setBackgroundColor(Color.parseColor("#F9CB9C"))
                binding.mtcardview.setBackgroundColor(Color.parseColor("#F9CB9C"))
                binding.spStatus.setSelection(0)
                setting=0
            }
            "Done" ->{
                binding.mtcardview.setBackgroundColor(Color.parseColor("#B6D7A8"))
                binding.spStatus.setBackgroundColor(Color.parseColor("#B6D7A8"))
                binding.spStatus.setSelection(3)
                setting=3
            }
            "Stopped" -> {
                binding.mtcardview.setBackgroundColor(Color.parseColor("#EA9999"))
                binding.spStatus.setBackgroundColor(Color.parseColor("#EA9999"))
                binding.spStatus.setSelection(2)
                setting=2
            }
            "Not Started" ->{
                binding.mtcardview.setBackgroundColor(Color.parseColor("#CCCCCC"))
                binding.spStatus.setBackgroundColor(Color.parseColor("#CCCCCC"))
                binding.spStatus.setSelection(1)
                setting=1
            }
            else->{
            }
        }
        binding.tvTaskName.setText(task.name+" info")
        if(task.deadline!=null)
            binding.tvDeadline.setText(task.deadline!!)
        else
            binding.tvDeadline.setText("No deadline specified")
        binding.ttDesc.setText(task.description)
        if(task.assignees==null)
            binding.tWorkers.text="No assignees"
        else {
            if(task.assignees!!.isEmpty())
                binding.tWorkers.text="No assignees"
            else {
                val itr = task.assignees!!.listIterator()
                var workers = ""
                while (itr.hasNext()) {
                    workers += itr.next().fullname
                    workers+='\n'
                }
                binding.tWorkers.text = workers
            }
        }

        if(task.prerequisiteTasks!=null){
            binding.rvPreReq.visibility=View.VISIBLE
            binding.tvNoPrereq.visibility=View.GONE
            val itr= task.prerequisiteTasks!!.listIterator()
            while(itr.hasNext()) {
                val item=itr.next()
                prereqtasks.add(item)
                if(item.status!="Done") {
                    binding.savebtn.isEnabled = false
                    binding.savebtn.setBackgroundColor(Color.parseColor("#CCCCCC"))
                }
            }
        adapter= PreReqAdapter(token,prereqtasks,projid)
        binding.rvPreReq.adapter=adapter
        }
        else {
            binding.rvPreReq.visibility=View.GONE
            binding.tvNoPrereq.visibility=View.VISIBLE
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (pos){
            0 -> {
                binding.spStatus.setBackgroundColor(Color.parseColor("#F9CB9C"))
                binding.mtcardview.setBackgroundColor(Color.parseColor("#F9CB9C"))
            }
            1 -> {
                binding.spStatus.setBackgroundColor(Color.parseColor("#CCCCCC"))
                binding.mtcardview.setBackgroundColor(Color.parseColor("#CCCCCC"))
            }
            2 -> {
                binding.spStatus.setBackgroundColor(Color.parseColor("#EA9999"))
                binding.mtcardview.setBackgroundColor(Color.parseColor("#EA9999"))
            }
            3 -> {
                binding.spStatus.setBackgroundColor(Color.parseColor("#B6D7A8"))
                binding.mtcardview.setBackgroundColor(Color.parseColor("#B6D7A8"))
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskToFragmentUser(token))
                return true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskToFragmentProject(token))
                return true
            }
            R.id.taskspage->{
                binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskToFragmentUpcomingTasks(token))
                return true
            }
            else->{
                return false
            }
        }
    }
}