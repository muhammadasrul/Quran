package com.acun.quran.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.acun.quran.R
import com.acun.quran.databinding.ActivityMainBinding
import com.acun.quran.util.hide
import com.acun.quran.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.juzDetailFragment -> binding.bottomNavigation.hide()
                R.id.surahDetailFragment -> binding.bottomNavigation.hide()
                R.id.surahFragment -> binding.bottomNavigation.hide()
                else -> binding.bottomNavigation.show()
            }
        }
    }


    override fun onNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onNavigateUp()
    }
}