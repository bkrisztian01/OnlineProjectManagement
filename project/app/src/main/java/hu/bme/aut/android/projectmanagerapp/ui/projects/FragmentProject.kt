package hu.bme.aut.android.projectmanagerapp.ui.projects


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentProjectBinding
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.ui.adapter.ProjectAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class FragmentProject : Fragment() {
    private val projects: ArrayList<Project> = ArrayList<Project>()
    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.toolbar.inflateMenu(R.menu.menu_project_toolbar)
        binding.toolbar.setOnMenuItemClickListener {
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
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Signing out?")
                        .setMessage(R.string.are_you_sure_want_to_sign_out)
                        .setIcon(R.drawable.ic_account_circle)
                        .setPositiveButton(R.string.yes) { _, _ ->
                            Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT).show()
                            binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToFragmentWelcome()) }
                        .setNegativeButton(R.string.no, null)

                        .show()

                }


                return true
                }
            R.id.menu_help->{
                if (context != null) {
                    AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setIcon(R.drawable.ic_help_outline)
                        .setMessage(R.string.pick)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated(view,savedInstanceState)
        val recyclerView = activity?.findViewById(R.id.rvProjects) as RecyclerView

        //projects = Project.createProjectList(10)
        val proj: Project = Project(1,"Project1","desc","client", Date(2002,12,21,23,59),Date(2002,12,21,23,59),12) ;
        projects.add(proj)
        val adapter = ProjectAdapter(projects)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.activity)

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(context)
                        .setTitle("Logging out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton(R.string.yes) { _, _ ->
                            Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT).show()
                            binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToFragmentWelcome()) }
                        .setNegativeButton(R.string.no, null)

                        .show()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }



}