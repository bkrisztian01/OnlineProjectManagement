package hu.bme.aut.android.projectmanagerapp.ui.projects


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentProjectBinding
import hu.bme.aut.android.projectmanagerapp.model.Milestone
import hu.bme.aut.android.projectmanagerapp.model.Project
import hu.bme.aut.android.projectmanagerapp.model.Task
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.adapter.ProjectAdapter
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksArgs
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class FragmentProject : Fragment() {
    private val projects: ArrayList<Project> = ArrayList<Project>()
    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private val projectViewModel : ProjectViewModel by viewModels()
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val view = binding.root
        if (arguments!=null) {
            val args: FragmentProjectArgs by navArgs()
            user = args.user
        }

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
                binding.root.findNavController().navigate(FragmentProjectDirections.actionFragmentProjectToDialogFragmentUser(user))
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

    override fun onResume() {
        super.onResume()
        if(!projects.isEmpty())
            projects.clear()
        projectViewModel.getProjects()?.observe(this, {
                projectsViewState->render(projectsViewState)
        })
    }
    private fun render(result: ProjectsViewState) {
        when (result) {
            is InProgress -> {}
            is ProjectsResponseSuccess ->{
                val itr = result.data?.listIterator()
                if (itr != null) {
                    while (itr.hasNext()) {
                        projects.add(itr.next())

                    }

                }
                val recyclerView = activity?.findViewById(R.id.rvProjects) as RecyclerView
                val adapter = ProjectAdapter(user,projects)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this.activity)

            }
            is ProjectsResponseError ->{
                Toast.makeText(context, "halál", Toast.LENGTH_SHORT).show()
            }

        }
    }



}