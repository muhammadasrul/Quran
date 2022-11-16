package com.acun.quran.ui.compass

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import com.acun.quran.databinding.FragmentCompassBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.roundToInt

class CompassFragment : Fragment(), SensorEventListener2 {

    private var _binding: FragmentCompassBinding? = null
    private val binding get() = _binding!!
    private var currentDegree = 0F
    private val  sensorManager by lazy {
        requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location = Location("my_location")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompassBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                        && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                -> {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            location = it
                        }
                    }
                }
                else -> Unit
            }
        }

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        )
    }

    override fun onSensorChanged(event: SensorEvent) {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
            -> {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    location = it
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    && shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            -> {
                locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
            else -> {
                locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }

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
        binding.kaabaImage.startAnimation(kaabaRotateAnimation)

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
        binding.compassImage.startAnimation(compassRotateAnimation)

        currentDegree = -degree.toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) {
        Log.d("onAccuracyChanged", "${sensor?.name} $p1")
    }

    override fun onFlushCompleted(sensor: Sensor?) {
        Log.d("onFlushCompleted", sensor?.name.toString())
    }
}