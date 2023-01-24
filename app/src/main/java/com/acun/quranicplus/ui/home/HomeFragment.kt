package com.acun.quranicplus.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.compose.HomeScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(), SensorEventListener2 {

    private lateinit var composeView: ComposeView

    private val viewModel: HomeViewModel by viewModels()
    private var currentDegree = 0F
    private var location = Location("my_location")
    private val  sensorManager by lazy {
        requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                viewModel.setLocation(it)
            }
        }

        observeLocation()

        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }

    private fun getLocationName(lat: Double, long: Double): String {
        val addresses = try {
            Geocoder(requireContext()).getFromLocation(lat, long, 1)?.firstOrNull()
        } catch (e: Exception) {
            null
        }
        return addresses?.locality ?: requireContext().getString(R.string.error_location_not_found)
    }


    private fun observeLocation() {
        viewModel.location.observe(viewLifecycleOwner) {
            location = it
//            binding.textViewCurrentLocation.text = getLocationName(it.latitude, it.longitude)
            viewModel.getPrayer(lat = it.latitude, long = it.longitude)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        )
    }

    override fun onSensorChanged(event: SensorEvent) {
        val degree = event.values[0].roundToInt()
        val kaabaLocation = Location("kaaba_location").apply {
            latitude = 21.4234756
            longitude = 39.8246424
        }
        val bearingToKaaba = location.bearingTo(kaabaLocation)
        val direction = bearingToKaaba-degree

        val kaabaRotateAnimation = RotateAnimation(
            direction,
            -degree.toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        kaabaRotateAnimation.duration = 210
        kaabaRotateAnimation.fillAfter = true
//        binding.kaabaImage.startAnimation(kaabaRotateAnimation)

        val compassRotateAnimation = RotateAnimation(
            currentDegree,
            (-degree).toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        compassRotateAnimation.duration = 210
        compassRotateAnimation.fillAfter = true
//        binding.compassImage.startAnimation(compassRotateAnimation)

        currentDegree = -degree.toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) {
        Log.d("onAccuracyChanged", "${sensor?.name} $p1")
    }

    override fun onFlushCompleted(sensor: Sensor?) {
        Log.d("onFlushCompleted", sensor?.name.toString())
    }
}