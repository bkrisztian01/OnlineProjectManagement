package hu.bme.aut.android.projectmanagerapp.ui.user

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentSingleprojectBinding
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentTasksBinding
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentUserBinding
import hu.bme.aut.android.projectmanagerapp.model.User
import hu.bme.aut.android.projectmanagerapp.ui.projects.FragmentProjectDirections
import hu.bme.aut.android.projectmanagerapp.ui.singleproject.FragmentSingleProjectDirections
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksArgs
import hu.bme.aut.android.projectmanagerapp.ui.tasks.FragmentTasksDirections

class FragmentUser: Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?{
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        setUp()
        val context=this.context
        if(context!=null) {
            binding.signoutbutton.setOnClickListener {
                androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Signing out?")
                    .setMessage(R.string.are_you_sure_want_to_sign_out)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        Toast.makeText(context, "You signed out!", Toast.LENGTH_SHORT).show()
                        findNavController(this).navigate(FragmentUserDirections.actionFragmentUserToFragmentWelcome())
                    }
                    .setNegativeButton(R.string.no, null)

                    .show()
            }
        }
        binding.toolbar2.inflateMenu(R.menu.menu_project_toolbar)
        binding.toolbar2.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val context = this.activity
        return when (item.itemId){

            R.id.menu_help->{
                if (context != null) {
                    androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setMessage("Here you can find information about your profile!")
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            R.id.menu_item->{
                val drawer = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
                drawer.open()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onResume() {
        super.onResume()
        val navigationView= activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setCheckedItem(R.id.accountpage)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setUp(){
        if (arguments!=null) {
            val args: FragmentUserArgs by navArgs()
            user=args.user
            binding.tvtUser2.text=user.username
            binding.tvtName.text=user.fullname
            binding.tvEmail.text=user.email
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.accountpage->{
                //binding.root.findNavController().navigate(FragmentSingleProjectDirections.actionFragmentSingleProjectToFragmentUser(user))
                return true
            }
            R.id.homepage->{
                binding.root.findNavController().navigate(FragmentUserDirections.actionFragmentUserToFragmentProject(user))
                return true
            }
            R.id.taskspage->{
                binding.root.findNavController().navigate(FragmentUserDirections.actionFragmentUserToFragmentUpcomingTasks(user))
                return true
            }
            else->{
                return false
            }
        }
    }



}
