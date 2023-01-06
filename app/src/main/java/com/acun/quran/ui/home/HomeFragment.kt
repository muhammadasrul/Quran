package com.acun.quran.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quran.R
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.prayer.getNearestPrayer
import com.acun.quran.data.remote.response.prayer.getNowPrayer
import com.acun.quran.data.remote.response.prayer.model.hour
import com.acun.quran.data.remote.response.prayer.model.minute
import com.acun.quran.data.remote.response.prayer.toPrayerList
import com.acun.quran.databinding.FragmentHomeBinding
import com.acun.quran.util.toGone
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment(), SensorEventListener2 {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                viewModel.setLocation(it)
            }
        }

        val now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.bannerBackground.background = when (now) {
            in 19 .. 24, in 1 .. 5 -> ContextCompat.getDrawable(requireContext(), R.drawable.malam)
            in 5..7 -> ContextCompat.getDrawable(requireContext(), R.drawable.pagi)
            in 7..15 -> ContextCompat.getDrawable(requireContext(), R.drawable.siang)
            in 15 .. 19 -> ContextCompat.getDrawable(requireContext(), R.drawable.sore)
            else -> ContextCompat.getDrawable(requireContext(), R.drawable.pagi)
        }

        observeLocation()
        observePrayer()

    }

    private fun getLocationName(lat: Double, long: Double): String {
        val addresses = Geocoder(requireContext()).getFromLocation(lat, long, 1)?.firstOrNull()
        return addresses?.locality ?: requireContext().getString(R.string.error_location_not_found)
    }

    private fun observePrayer() {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        viewModel.prayer.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    binding.shimmerContainer.toGone()
                    resources.data?.get(day)?.timings?.let { time ->
                        val prayerList = time.toPrayerList()

                        binding.rvPrayerTime.setHasFixedSize(true)
                        binding.rvPrayerTime.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.rvPrayerTime.adapter = PrayerTimeAdapter(prayerList)

                        val nowPrayerTime = time.getNowPrayer()
                        if (nowPrayerTime != null) binding.rvPrayerTime.scrollToPosition(prayerList.indexOf(nowPrayerTime))

                        val nearestPrayerTime = time.getNearestPrayer()
                        binding.textViewNextPrayerName.text = getString(R.string.next_prayer, nearestPrayerTime?.name)
                        binding.textViewNextPrayerTime.text = nearestPrayerTime?.time

                        val now = Calendar.getInstance().time.time
                        val nearest = Calendar.getInstance().apply {
                                if (get(Calendar.HOUR_OF_DAY) >= prayerList.last().hour()) {
                                    add(Calendar.DATE, 1)
                                }
                                nearestPrayerTime?.let {
                                    set(Calendar.HOUR_OF_DAY, it.hour())
                                    set(Calendar.MINUTE, it.minute())
                                }
                            }.time.time

                        val diff = nearest-now
                        binding.textViewNextPrayerCounter.prayerTimeCounter(diff)
                    }
                }
                is Resource.Failed -> Unit
            }
        }
    }

    private fun observeLocation() {
        viewModel.location.observe(viewLifecycleOwner) {
            location = it
            binding.textViewCurrentLocation.text = getLocationName(it.latitude, it.longitude)
            viewModel.getPrayer(lat = it.latitude, long = it.longitude)
        }
    }

    fun TextView.prayerTimeCounter(diff: Long) {
        object : CountDownTimer(diff, 1000) {
            override fun onTick(timeInMiles: Long) {
                val hour = timeInMiles / (1000 * 60 * 60)
                val minute = timeInMiles / (1000 * 60) % 60
                val second = (timeInMiles / 1000) % 60
                fun timeFormat(time: Long) = String.format("%02d", time)
                this@prayerTimeCounter.text = getString(R.string.prayer_time_counter, timeFormat(hour), timeFormat(minute), timeFormat(second))
            }

            override fun onFinish() {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        viewModel.setLocation(it)
                    }
                }
            }
        }.start()
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