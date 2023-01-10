package com.acun.quranicplus.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.acun.quranicplus.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(), ActivityResultCallback<Map<String, Boolean>> {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private val currentDestination by lazy {
        findNavController().currentDestination
    }
    private var permissionDialog: AlertDialog? = null
    private var isPermissionLaunched = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    SplashScreen()
                }
            }
        }

        locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), this)
        locationPermissionLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION))
        isPermissionLaunched = true

        permissionDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Location permission is required")
            setMessage("This app need permission to access this device's location")
            setPositiveButton("Open Setting") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireActivity().packageName}"))
                startActivity(intent)
            }
            setNegativeButton("Cancel") { _, _ ->
                requireActivity().finishAffinity()
            }
            setCancelable(false)
        }.create()
    }

    override fun onActivityResult(result: Map<String, Boolean>) {
        when {
            result[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    && result[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
            -> navigateToHome()
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    && shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
            -> {
                if (!isPermissionLaunched) {
                    permissionDialog?.show()
                }
            }
            else -> {
                if (!isPermissionLaunched) {
                    permissionDialog?.show()
                }
            }
        }
        isPermissionLaunched = false
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && requireActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                navigateToHome()
            } else {
                if (!isPermissionLaunched) {
                    permissionDialog?.show()
                }
            }
        }
    }

    private fun navigateToHome() {
        lifecycleScope.launch {
            delay(1000)
            if (findNavController().currentDestination == currentDestination) {
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
            }
        }
    }
}