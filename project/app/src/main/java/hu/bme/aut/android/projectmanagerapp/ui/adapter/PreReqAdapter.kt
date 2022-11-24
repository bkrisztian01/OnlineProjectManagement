package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.data.task.Task
import hu.bme.aut.android.projectmanagerapp.databinding.ItemMilestoneBinding
import hu.bme.aut.android.projectmanagerapp.databinding.ItemPrereqTasksBinding
import hu.bme.aut.android.projectmanagerapp.ui.singletask.FragmentSingleTaskDirections

class PreReqAdapter(private val token: String, private var tasks: ArrayList<Task>, private var projectid: Int):
    RecyclerView.Adapter<PreReqAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemPrereqTasksBinding) : RecyclerView.ViewHolder(binding.root) {
        val taskbutton = itemView.findViewById<Button>(R.id.prereqbtn)
        val image= itemView.findViewById<ImageView>(R.id.isitdoneimage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreReqAdapter.ViewHolder {
        return ViewHolder(
            ItemPrereqTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: Task=tasks[position]
        val button=holder.taskbutton

        val image=holder.image
        image.bringToFront()
        button.setOnClickListener {
            holder.binding.root.findNavController().navigate(FragmentSingleTaskDirections.actionFragmentSingleTaskSelf(token,projectid,task.id))
        }
        if(task.status=="Done")
            image.setImageResource(R.drawable.ic_assignment_turned_in)
        else
            image.setImageResource(R.drawable.ic_assignment_late)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}