package com.acun.quran.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.acun.quran.R
import com.acun.quran.databinding.ActivityMainBinding
import com.acun.quran.util.toGone
import com.acun.quran.util.toVisible
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
                R.id.surahDetailFragment,
                R.id.juzDetailFragment,
                R.id.surahFragment,
                R.id.shareFragment,
                R.id.splashScreenFragment-> binding.bottomNavigation.toGone()
                else -> binding.bottomNavigation.toVisible()
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onNavigateUp()
    }
}