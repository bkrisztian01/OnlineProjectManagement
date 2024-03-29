package hu.bme.aut.android.projectmanagerapp.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.projectmanagerapp.R
import hu.bme.aut.android.projectmanagerapp.databinding.FragmentUserBinding
import hu.bme.aut.android.projectmanagerapp.data.user.User
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseError
import hu.bme.aut.android.projectmanagerapp.ui.login.LoginResponseSuccess

class FragmentUser : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private lateinit var token: String
    private val userViewModel: UserViewModel by viewModels()
    private val loginViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        setUp()
        val context = this.context
        if (context != null) {
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
        binding.toolbar2.inflateMenu(R.menu.menu_singleproject_toolbar)
        binding.toolbar2.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val context = this.activity
        return when (item.itemId) {

            R.id.menu_help -> {
                if (context != null) {
                    androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Help")
                        .setMessage("Here you can find information about your profile!")
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                }
                return true
            }
            R.id.menu_item -> {
                val drawer = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
                drawer.open()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        val navigationView = activity?.findViewById(R.id.nav_view) as NavigationView
        navigationView.setCheckedItem(R.id.accountpage)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setUp() {
        if (arguments != null) {
            val args: FragmentUserArgs by navArgs()
            token = args.token
            userViewModel.getUser(token)?.observe(this) { userViewState ->
                render(userViewState)
            }
        }
    }

    private fun load() {
        userViewModel.getUser(token)?.observe(this) { userViewState ->
            render(userViewState)
        }
    }

    private fun render(result: UserViewState?) {
        when (result) {
            is InProgress -> {
                binding.loading.bringToFront()
                binding.loading.show()

            }
            is UserResponseSuccess -> {
                binding.loading.hide()
                user = result.data
                binding.tvtUser2.text = user.username
                binding.tvtName.text = user.fullname
                binding.tvEmail.text = user.email

            }
            is UserResponseError -> {
                binding.loading.hide()
                if (result.exceptionMsg == "401") {
                    loginViewModel.getRefreshToken().observe(this) { loginViewState ->
                        run {
                            when (loginViewState) {
                                is hu.bme.aut.android.projectmanagerapp.ui.login.InProgress -> {}
                                is LoginResponseSuccess -> {
                                    token = loginViewState.data.accessToken
                                    load()
                                }
                                is LoginResponseError -> {
                                    this.view?.let {
                                        Snackbar.make(
                                            it,
                                            R.string.server_error,
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    this.view?.let {
                        Snackbar.make(it, R.string.server_error, Snackbar.LENGTH_LONG).show()
                    }
                }

            }


            else -> {}
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accountpage -> {
                return true
            }
            R.id.homepage -> {
                binding.root.findNavController()
                    .navigate(FragmentUserDirections.actionFragmentUserToFragmentProject(token))
                return true
            }
            R.id.taskspage -> {
                binding.root.findNavController()
                    .navigate(FragmentUserDirections.actionFragmentUserToFragmentUpcomingTasks(token))
                return true
            }
            else -> {
                return false
            }
        }
    }


}
