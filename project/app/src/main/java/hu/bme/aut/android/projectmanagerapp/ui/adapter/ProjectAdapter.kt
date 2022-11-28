package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.ItemProjectBinding
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.projects.FragmentProjectDirections
import java.util.*

class ProjectAdapter(private var projects: ArrayList<Project>, private val token: String) :
    RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val projectbutton = itemView.findViewById<Button>(R.id.btnproject)
    }

    fun filterList(filterList: ArrayList<Project>?) {
        if (filterList != null)
            projects = filterList
        else
            projects.clear()
        notifyDataSetChanged()
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
            viewHolder.binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToFragmentNav(token, project.id, project.name))
        }

        when (position % 10) {
            0 -> {
                button.setBackgroundColor(Color.parseColor("#5a90ed"))
            }
            1 -> {
                button.setBackgroundColor(Color.parseColor("#5185e4"))
            }
            2 -> {
                button.setBackgroundColor(Color.parseColor("#487ada"))
            }
            3 -> {
                button.setBackgroundColor(Color.parseColor("#3f6fd0"))
            }
            4 -> {
                button.setBackgroundColor(Color.parseColor("#3765c7"))
            }
            5 -> {
                button.setBackgroundColor(Color.parseColor("#2e5abd"))
            }
            6 -> {
                button.setBackgroundColor(Color.parseColor("#2550b3"))
            }
            7 -> {
                button.setBackgroundColor(Color.parseColor("#1b45aa"))
            }
            8 -> {
                button.setBackgroundColor(Color.parseColor("#0f3ba0"))
            }
            9 -> {
                button.setBackgroundColor(Color.parseColor("#003196"))
            }

        }
        button.text = project.name
        button.isEnabled = true
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}