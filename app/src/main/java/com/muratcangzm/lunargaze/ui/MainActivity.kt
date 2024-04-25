package com.muratcangzm.lunargaze.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.ActivityMainBinding
import com.muratcangzm.lunargaze.ui.fragments.HomeFragmentDirections
import com.muratcangzm.lunargaze.utils.NetworkChecking
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private var searchItem: MenuItem? = null

    @Inject
    lateinit var networkChecking: NetworkChecking

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Timber.tag("DataSpeed1: ").d("" + networkChecking.getMobileSpeed().first)
        Timber.tag("DataSpeed2: ").d("" + networkChecking.getMobileSpeed().second)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.navController)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener { menuItem ->

            when(menuItem){
                binding.navView.menu.findItem(R.id.googleSignIn) -> {
                    //navHostFragment.navController.navigate(R.id.homeFragment)
                    Toast.makeText(this, "Google sign in clicked", Toast.LENGTH_SHORT).show()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

            true
        }

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            currentFocus?.clearFocus()

            when (destination.id) {
                R.id.displayFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE

                }

                R.id.favoritesFragment -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.bottomNavigation.visibility = View.VISIBLE
                    searchItem?.isVisible = false

                }

                R.id.searchDisplayFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                    binding.toolbar.visibility = View.INVISIBLE
                }

                R.id.homeFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    searchItem?.isVisible = true

                }

                R.id.fullScreenImageFragment -> {
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.GONE
                }

                R.id.favoritedImageFragment -> {

                    binding.toolbar.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.GONE
                }

                else -> throw Exception("There isn't any destination")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_icons, menu)


        searchItem = menu!!.findItem(R.id.search_icon)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Search whatever you want"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (!query.isNullOrEmpty()) {

                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                    val navController = navHostFragment.navController

                    val action =
                        HomeFragmentDirections.actionHomeFragmentToSearchDisplayFragment(query)
                    navController.navigate(action)

                    searchItem?.collapseActionView()
                    searchView.clearFocus()
                    return true
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}