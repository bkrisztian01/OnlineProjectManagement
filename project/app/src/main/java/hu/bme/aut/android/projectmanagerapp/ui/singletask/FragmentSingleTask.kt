package hu.bme.aut.android.projectmanagerapp.ui.singletask

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingletaskBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class FragmentSingleTask : Fragment(), AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var project: Project
    private lateinit var task: Task
    private var _binding: FragmentSingletaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var user : User
    private val singleTaskViewModel: SingleTaskViewModel by viewModels()
    //private lateinit var token: String



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
        if (arguments!=null) {
            val args: FragmentSingleTaskArgs by navArgs()
            project = args.project
            task=args.task
            user=args.user
            //token=args.token

        }
        binding.toolbarsingletask.inflateMenu(R.menu.menu_singleproject_toolbar)
        binding.toolbarsingletask.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

            binding.savebtn.setOnClickListener{
                task.status=binding.spStatus.selectedItem.toString().filter { !it.isWhitespace() }
                if(binding.spStatus.selectedItem.toString()=="Done") {
                    binding.konfettiView.bringToFront()
                    binding.konfettiView.start(party)
                }
                singleTaskViewModel.updateTask(task)
                Toast.makeText(context, "Task info changed!", Toast.LENGTH_SHORT).show()
            }
            binding.discardbtn.setOnClickListener {
                loadTask()
                binding.spStatus.setSelection(setting)
            }
            loadTask()

            binding.spStatus.adapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                resources.getStringArray(R.array.status_array)

            )
            binding.spStatus.onItemSelectedListener = this
            binding.spStatus.setSelection(setting)
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
            binding.tEndDate.setText(task.deadline.substring(   0,10)+" "+project.startDate.substring(11,19))
            binding.ttDesc.setText(task.description)
            val itr = task.assignees.listIterator()
            var workers =""
            while (itr.hasNext()) {
                workers+=itr.next().fullname

            }
            binding.tWorkers.text=workers


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
                    binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskToFragmentUser(user))
                    return true
                }
                R.id.homepage->{
                    binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskToFragmentProject(user))
                    return true
                }
                R.id.taskspage->{
                    binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskToFragmentUpcomingTasks(user))
                    return true
                }
                else->{
                    return false
                }
            }
        }
    }