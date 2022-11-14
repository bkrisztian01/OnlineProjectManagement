package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.ItemTaskBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksDirections

class TaskAdapter (private val tasks: List<Task>, private val project: Project, private val user: User,) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
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
            viewHolder.binding.root.findNavController().navigate(FragmentTasksDirections.actionFragmentTasksToFragmentSingleTask( project,tasks[position],user
                ))
        }
        when(position%10){
            0 ->{ button.setBackgroundColor(Color.parseColor("#5a90ed")) }
            1 ->{ button.setBackgroundColor(Color.parseColor("#5185e4")) }
            2 ->{ button.setBackgroundColor(Color.parseColor("#487ada")) }
            3 ->{ button.setBackgroundColor(Color.parseColor("#3f6fd0")) }
            4 ->{ button.setBackgroundColor(Color.parseColor("#3765c7")) }
            5 ->{ button.setBackgroundColor(Color.parseColor("#2e5abd")) }
            6 ->{ button.setBackgroundColor(Color.parseColor("#2550b3")) }
            7 ->{ button.setBackgroundColor(Color.parseColor("#1b45aa")) }
            8 ->{ button.setBackgroundColor(Color.parseColor("#0f3ba0")) }
            9 ->{ button.setBackgroundColor(Color.parseColor("#003196")) }

        }
        button.text = task.name
        button.isEnabled = true
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

}