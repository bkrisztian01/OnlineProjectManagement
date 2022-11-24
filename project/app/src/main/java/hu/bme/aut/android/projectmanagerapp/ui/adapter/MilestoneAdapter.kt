package hu.bme.aut.android.projectmanagerapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.ItemMilestoneBinding
import hu.bme.aut.android.projectmanagerapp.data.milestone.Milestone
import hu.bme.aut.android.projectmanagerapp.data.project.Project
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.milestone.FragmentMilestoneDirections
import java.util.ArrayList

class MilestoneAdapter (private var milestones: ArrayList<Milestone>, private val projectid: Int, private val token: String) : RecyclerView.Adapter<MilestoneAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMilestoneBinding) : RecyclerView.ViewHolder(binding.root) {
        val milestonebutton = itemView.findViewById<Button>(R.id.btnmilestone)
    }


    fun filterList(filterList: ArrayList<Milestone>?) {
        if(filterList!=null)
            milestones = filterList
        else
            milestones.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneAdapter.ViewHolder {
        return ViewHolder(
            ItemMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: MilestoneAdapter.ViewHolder, position: Int) {
        val milestone: Milestone = milestones[position]
        val button = viewHolder.milestonebutton
        button.setOnClickListener {
            viewHolder.binding.root.findNavController().navigate(FragmentMilestoneDirections.actionFragmentMilestoneToFragmentTasks(token,projectid,milestone.id,milestone.name))
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
        button.text = milestone.name
        button.isEnabled = true
    }

    override fun getItemCount(): Int {
        return milestones.size
    }
}