package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.ItemTaskBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksDirections

class TaskAdapter (private val tasks: List<Task>,private val project: Project) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        val taskbutton = itemView.findViewById<Button>(R.id.btntask)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: TaskAdapter.ViewHolder, position: Int) {
        val task: Task = tasks[position]
        val button = viewHolder.taskbutton

        button.setOnClickListener {
            viewHolder.binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentSingleTask( project,tasks[position]
                ))
        }
        button.text = task.name
        button.isEnabled = true
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

}