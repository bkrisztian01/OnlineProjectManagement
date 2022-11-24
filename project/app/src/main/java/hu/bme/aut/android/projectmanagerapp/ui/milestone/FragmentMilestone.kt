package hu.bme.aut.android.projectmanagerapp.ui.milestone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentMilestoneBinding
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.ui.adapter.MilestoneAdapter
import kotlin.collections.ArrayList

class FragmentMilestone : Fragment() ,NavigationView.OnNavigationItemSelectedListener{
    private val milestones: ArrayList<Milestone> = ArrayList()
    private var _binding: FragmentMilestoneBinding? = null
    private val binding get() = _binding!!
    private val milestoneViewModel: MilestoneViewModel by viewModels()
    private lateinit var projectname: String
    private lateinit var adapter: MilestoneAdapter
    private lateinit var token: String
    private var projid=-1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentMilestoneBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentMilestoneArgs by navArgs()
            projectname=args.projname
            token=args.token
            projid=args.projid
            binding.tvMilestones.setText("Milestones in "+ projectname)
        }

        binding.loading.hide()
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
        val searchView=activity!!.findViewById(R.id.menu_search) as SearchView
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
        })
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
                        .setMessage(R.string.pick_milestone)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                    return true
            }
            R.id.menu_project->{
                binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentSingleProject(token,projid))
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated(view,savedInstanceState)
        if(milestones.isNotEmpty())
            milestones.clear()
        milestoneViewModel.getMilestones(token,projid)?.observe(this) { milestoneViewState ->
            render(milestoneViewState)
        }

    }


    private fun render(result: MilestonesViewState) {
        when (result) {
            is InProgress -> {
                binding.loading.show()
                binding.rvMilestones.visibility=View.GONE
            }
            is MilestoneResponseSuccess -> {
                binding.loading.hide()
                binding.rvMilestones.visibility=View.VISIBLE
                val itr = result.data?.listIterator()
                if (itr != null) {
                    while (itr.hasNext()) {
                        milestones.add(itr.next())
                    }
                }
                val recyclerView = activity?.findViewById(R.id.rvMilestones) as RecyclerView
                adapter = MilestoneAdapter(milestones,projid,token)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this.activity)
            }
            is MilestoneResponseError -> {
                binding.loading.hide()
                this.view?.let {
                    Snackbar.make(it, "Couldn't reach server!", Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId){
                R.id.accountpage->{
                    binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentUser(token))
                    return true
                }
                R.id.homepage->{
                    binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentProject(token))
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

        private fun filter(text: String) {
            val filteredlist = java.util.ArrayList<Milestone>()
            for (item in milestones) {
                if (item.name.lowercase().contains(text.lowercase(),true)) {
                    filteredlist.add(item)
                }
            }
            /*if (filteredlist.isEmpty()) {
                adapter.filterList(null)
            } else {*/
                adapter.filterList(filteredlist)
            //}
        }


    }