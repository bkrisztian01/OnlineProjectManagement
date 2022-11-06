package hu.bme.aut.android.projectmanagerapp.ui.milestone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class FragmentMilestone : Fragment() {
    private val milestones: ArrayList<Milestone> = ArrayList<Milestone>()
    private lateinit var project: Project
    private var _binding: FragmentMilestoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentMilestoneBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentMilestoneArgs by navArgs()
            user=args.user
            project = args.project
            binding.tvMilestones.setText("Milestones in "+ project.name)
        }
        binding.toolbarmilestones.inflateMenu(R.menu.menu_task_toolbar)
        binding.toolbarmilestones.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val context = this.activity
        return when (item.itemId){
            R.id.menu_sign_out->{
                binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToDialogFragmentUser(user))
                return true

            }
            R.id.menu_help->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setMessage(R.string.pick_project)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            R.id.menu_project->{
                binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentSingleProject(project,user))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated(view,savedInstanceState)
        val recyclerView = activity?.findViewById(R.id.rvMilestones) as RecyclerView

        //milestones = Milestone.createMileStoneList(10)
        //milestones.add(Milestone(21,"Milestone36","random desc","Done", /*Date(*/"2022-11-17"/*)*/,/*1,*/ArrayList<Task>()))
        if(!milestones.isEmpty())
            milestones.clear()
        val itr = project.milestones.listIterator()
        if (itr != null) {
            while (itr.hasNext()) {
                milestones.add(itr.next())

            }
        }
        val adapter = MilestoneAdapter(milestones,project,user)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.activity)

    }


}