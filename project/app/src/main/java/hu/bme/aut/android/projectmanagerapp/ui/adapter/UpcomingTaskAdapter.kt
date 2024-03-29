package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.ItemTaskBinding
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentUpcomingTasksDirections
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

class UpcomingTaskAdapter(private val token: String, private var tasks: ArrayList<Task>, private val projects: ArrayList<Project>) : RecyclerView.Adapter<UpcomingTaskAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        val taskbutton = itemView.findViewById<Button>(R.id.btntask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingTaskAdapter.ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    fun filterList(filterList: ArrayList<Task>?) {
        if (filterList != null)
            tasks = filterList
        else
            tasks.clear()
        notifyDataSetChanged()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: UpcomingTaskAdapter.ViewHolder, position: Int) {
        val task: Task = tasks[position]
        val button = viewHolder.taskbutton

        button.setOnClickListener {
            if (task.project != null)
                viewHolder.binding.root.findNavController().navigate(FragmentUpcomingTasksDirections.actionFragmentUpcomingTasksToFragmentSingleTask(token, task.project.id, task.id))
        }
        val daynow = LocalDateTime.now().dayOfMonth
        val monthnow = LocalDateTime.now().month.value
        val yearnow = LocalDateTime.now().year
        if (task.deadline != null) {
            val year = Integer.parseInt(task.deadline.substring(0, 4))
            val month = Integer.parseInt(task.deadline.substring(5, 7))
            val day = Integer.parseInt(task.deadline.substring(8, 10))
            val deadline = Date(year, month, day)
            val timenow = Date(yearnow, monthnow, daynow)
            val difference = deadline.time - timenow.time
            val days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
            if (days < 3) {
                button.setBackgroundColor(Color.parseColor("#EA9999"))
                button.setTextColor(Color.parseColor("#540C04"))
            }
            else if (days < 7){
                button.setBackgroundColor(Color.parseColor("#F9CB9C"))
                button.setTextColor(Color.parseColor("#663300"))
            }
            else{
                button.setBackgroundColor(Color.parseColor("#B6D7A8"))
                button.setTextColor(Color.parseColor("#05600F"))
            }
            val text = task.name + ":     " + days + " days left"
            button.text = text
        }
        button.isEnabled = true

    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}