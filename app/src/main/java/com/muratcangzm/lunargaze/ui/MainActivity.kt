package com.muratcangzm.lunargaze.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.muratcangzm.lunargaze.R
import com.muratcangzm.lunargaze.databinding.ActivityMainBinding
import com.muratcangzm.lunargaze.ui.fragments.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.navController)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.displayFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE

                }

                R.id.favoritesFragment -> {
                    binding.toolbar.visibility = View.GONE

                }

                R.id.searchDisplayFragment -> {
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.GONE


                }

                R.id.homeFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE

                }

                else -> throw Exception("There isn't any destination")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_icons, menu)


        val searchItem = menu!!.findItem(R.id.search_icon)
        val searchView = searchItem.actionView as SearchView

        searchView.tooltipText = "Search whatever you want"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                if (query!!.isNotEmpty()){

                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                    val navController = navHostFragment.navController

                    val action =
                        HomeFragmentDirections.actionHomeFragmentToSearchDisplayFragment(query)
                    navController.navigate(action)
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