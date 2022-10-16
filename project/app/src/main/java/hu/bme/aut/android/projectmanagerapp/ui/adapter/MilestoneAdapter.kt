package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.ItemMilestoneBinding
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.ui.milestone.FragmentMilestoneDirections

class MilestoneAdapter (private val milestones: List<Milestone>, private val project: Project) : RecyclerView.Adapter<MilestoneAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMilestoneBinding) : RecyclerView.ViewHolder(binding.root) {
        val milestonebutton = itemView.findViewById<Button>(R.id.btnmilestone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneAdapter.ViewHolder {
        return ViewHolder(
            ItemMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: MilestoneAdapter.ViewHolder, position: Int) {
        val buttoncolor = ArrayList<Color>()
        val milestone: Milestone = milestones[position]
        val button = viewHolder.milestonebutton
        button.setOnClickListener {
            viewHolder.binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentTasks(project))
        }
        button.text = milestone.name
        button.isEnabled = true
    }

    override fun getItemCount(): Int {
        return milestones.size
    }
}