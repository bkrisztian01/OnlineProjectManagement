package hu.bme.aut.android.projectmanagerapp.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentProjectPickPathBinding
import hu.bme.aut.android.projectmanagerapp.ui.milestone.FragmentMilestoneArgs

class FragmentNav: Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentProjectPickPathBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectname: String
    private lateinit var token: String
    private var projid=-1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentProjectPickPathBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentMilestoneArgs by navArgs()
            projectname=args.projname
            token=args.token
            projid=args.projid
        }
        binding.milestonebtn.setOnClickListener(){
            binding.root.findNavController().navigate(FragmentNavDirections.actionFragmentNavToFragmentMilestone(token,projid,projectname))
        }
        binding.taskbtn.setOnClickListener(){
            binding.root.findNavController().navigate(FragmentNavDirections.actionFragmentNavToFragmentTasks(token,projid,-1,projectname))
        }
        binding.tvProject.text=projectname
        binding.toolbarmilestones.inflateMenu(R.menu.menu_milestone_toolbar)
        binding.toolbarmilestones.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val navigationView = activity?.findViewById(R.id.nav_view) as NavigationView
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
                        .setMessage("Ezt írd még át<3")
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            R.id.menu_project->{
                binding.root.findNavController().navigate(FragmentNavDirections.actionFragmentNavToFragmentSingleProject(token,projid))
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
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.accountpage->{
                binding.root.findNavController().navigate(FragmentNavDirections.actionFragmentNavToFragmentUser(token))
                return true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentNavDirections.actionFragmentNavToFragmentProject(token))
                return true
            }
            R.id.taskspage->{
                //binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentUpcomingTasks(user))
                return true
            }
            else->{
                return false
            }
        }
    }
}