package com.acun.quranicplus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.acun.quranicplus.R
import com.acun.quranicplus.databinding.ActivityMainBinding
import com.acun.quranicplus.util.toGone
import com.acun.quranicplus.util.toVisible
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.surahDetailFragment,
                R.id.juzDetailFragment,
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