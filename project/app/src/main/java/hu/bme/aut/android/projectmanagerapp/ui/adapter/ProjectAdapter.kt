package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.ItemProjectBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.ui.projects.FragmentProjectDirections

class ProjectAdapter (private val projects: List<Project>) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        val projectbutton = itemView.findViewById<Button>(R.id.btnproject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectAdapter.ViewHolder {
        return ViewHolder(
            ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ProjectAdapter.ViewHolder, position: Int) {
        val project: Project = projects[position]
        val button = viewHolder.projectbutton
        button.setOnClickListener {
            viewHolder.binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToFragmentMilestone(project))
        }
        button.text = project.name
        button.isEnabled = true
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}