package com.acun.quranicplus.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
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
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location = it
            viewModel.getPrayer(lat = it.latitude, long = it.longitude)
            viewModel.setLocation(getLocationName(it.latitude, it.longitude))
        }

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

        viewModel.setKaabaDegree(direction)
        viewModel.setCompassDegree(currentDegree)

        currentDegree = -degree.toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) = Unit

    override fun onFlushCompleted(sensor: Sensor?) = Unit
}