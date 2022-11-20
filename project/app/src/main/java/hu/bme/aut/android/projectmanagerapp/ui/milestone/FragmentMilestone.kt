package hu.bme.aut.android.projectmanagerapp.ui.milestone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentMilestoneBinding
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.MilestoneAdapter
import hu.bme.aut.android.projectmanagerapp.ui.projects.FragmentProjectDirections
import java.util.*
import kotlin.collections.ArrayList

class FragmentMilestone : Fragment() ,NavigationView.OnNavigationItemSelectedListener{
    private val milestones: ArrayList<Milestone> = ArrayList<Milestone>()
    private lateinit var project: Project
    private var _binding: FragmentMilestoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var adapter: MilestoneAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentMilestoneBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentMilestoneArgs by navArgs()
            user=args.user
            project = args.project
            binding.tvMilestones.setText("Milestones in "+ project.name)
        }
        binding.toolbarmilestones.inflateMenu(R.menu.menu_milestone_toolbar)
        binding.toolbarmilestones.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        /*
        val searchView=activity!!.findViewById(R.id.menu_search_ms) as SearchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return false
            }
        })*/
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val context = this.activity
        return when (item.itemId){
            R.id.menu_help_ms->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setMessage(R.string.pick_project)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                    return true
            }
            R.id.menu_project_ms->{
                binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentSingleProject(project,user))
                return true
            }

            R.id.menu_item_ms->{
                val drawer = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
                drawer.open()
                return true
            }


            else -> super.onOptionsItemSelected(item)

        }
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
            super.onViewCreated(view,savedInstanceState)
            val recyclerView = activity?.findViewById(R.id.rvMilestones) as RecyclerView

            if(!milestones.isEmpty())
                milestones.clear()
            val itr = project.milestones.listIterator()
            while (itr.hasNext()) {
                milestones.add(itr.next())
            }
            adapter = MilestoneAdapter(milestones,project,user)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this.activity)

        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId){
                R.id.accountpage->{
                    binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentUser(user))
                    return true
                }
                R.id.homepage->{
                    binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentProject(user))
                    return true
                }
                R.id.taskspage->{
                    binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentUpcomingTasks(user))
                    return true
                }
                else->{
                    return false
                }
            }
        }
    /*
        private fun filter(text: String) {
            val filteredlist = java.util.ArrayList<Milestone>()
            for (item in milestones) {
                if (item.name.lowercase().contains(text.lowercase(),true)) {
                    filteredlist.add(item)
                }
            }
            if (filteredlist.isEmpty()) {
                adapter.filterList(null)
            } else {
                adapter.filterList(filteredlist)
            }
        }*/


    }