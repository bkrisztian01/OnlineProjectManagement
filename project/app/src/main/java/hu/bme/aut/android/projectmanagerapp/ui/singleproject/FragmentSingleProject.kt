package hu.bme.aut.android.projectmanagerapp.ui.singleproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingleprojectBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.milestone.FragmentMilestoneDirections
import hu.bme.aut.android.projectmanagerapp.ui.projects.FragmentProjectDirections
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksArgs
import java.util.*

class FragmentSingleProject : Fragment(),NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentSingleprojectBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var project: Project
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentSingleprojectBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentSingleProjectArgs by navArgs()
            project = args.project
            user=args.user

        }
        binding.toolbarsingleproject.inflateMenu(R.menu.menu_singleproject_toolbar)
        binding.toolbarsingleproject.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        loadProject()
        return view
    }
    override fun onResume() {
        super.onResume()
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
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
                        .setMessage("Here is some information on the project!")
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
    private fun loadProject(){
        binding.tvProjectName.setText(project.name+" info")
        val sdate= project.startDate.substring(   0,10)+" "+project.startDate.substring(11,19)
        val edate=project.endDate.substring(   0,10)+" "+project.endDate.substring(11,19)
        binding.tStartDate.setText(sdate)
        binding.tEndDate.setText(edate)
        binding.tDesc.setText(project.description)
        binding.tLength.setText(project.estimatedTime.toString()+ " days")
        binding.tvsDate.text=unfinishedtasks().toString()
    }
    private fun unfinishedtasks(): Int{
        val itr1 = project.milestones.listIterator()
        var num=0
        while (itr1.hasNext()) {
            val itr2=itr1.next().tasks.listIterator()
            while(itr2.hasNext()){
                if(itr2.next().status!="Finished")
                    num++
            }

        }
        return num
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentSingleProjectDirections.actionFragmentSingleProjectToFragmentUser(user))
                return true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentSingleProjectDirections.actionFragmentSingleProjectToFragmentProject(user))
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