package com.driveinto.ladyj.app

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController

import com.driveinto.ladyj.R
import com.driveinto.ladyj.login.LoginResult
import kotlinx.android.synthetic.main.fragment_default.view.*

class DefaultFragment : AbstractFragment() {

    private var startDestination: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = DefaultFragmentArgs.fromBundle(it)
            startDestination = args.startDestination
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_default, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(view.toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            view.drawer_layout,
            view.toolbar,
            R.string.nav_app_bar_open_drawer_description,
            R.string.nav_app_bar_navigate_up_description
        )
        view.drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        val startDestinationArgs = Bundle()
        startDestinationArgs.putParcelable(LoginResult.nameKey, getLoginResult())

        val navController = findNavController(requireActivity(), R.id.nav_master_controller)
        navController.graph.startDestination = startDestination
        navController.setGraph(navController.graph, startDestinationArgs)

        val configuration = AppBarConfiguration(
            setOf(
                R.id.nav_customer,
                R.id.nav_organization,
                R.id.nav_sales,
                R.id.nav_test,
                R.id.nav_login
            ), view.drawer_layout
        )
        NavigationUI.setupActionBarWithNavController(
            requireActivity() as AppCompatActivity,
            navController,
            configuration
        )
        view.nav_view.setupWithNavController(navController)
        view.nav_view.setNavigationItemSelectedListener {
            view.drawer_layout.closeDrawer(GravityCompat.START)

            when (it.itemId) {
                R.id.nav_customer -> {
                    val controller = findNavController(requireActivity(), R.id.nav_login_controller)
                    val action = DefaultFragmentDirections.actionNavDefaultToNavLogin(R.id.nav_customer)
                    controller.navigate(action)

                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_organization -> {
                    val controller = findNavController(requireActivity(), R.id.nav_login_controller)
                    val action = DefaultFragmentDirections.actionNavDefaultToNavLogin(R.id.nav_organization)
                    controller.navigate(action)

                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_sales -> {
                    val controller = findNavController(requireActivity(), R.id.nav_login_controller)
                    val action = DefaultFragmentDirections.actionNavDefaultToNavLogin(R.id.nav_sales)
                    controller.navigate(action)

                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_test -> {
                    showToast("testing...............")

                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_login -> {
                    val controller = findNavController(requireActivity(), R.id.nav_login_controller)
                    val action = DefaultFragmentDirections.actionNavDefaultToNavLogin(R.id.nav_login)
                    controller.navigate(action)

                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    return@setNavigationItemSelectedListener false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // todo: 沒作用
        inflater.inflate(R.menu.toolbar, menu)
    }
}
