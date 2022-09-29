package com.acun.quran.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.acun.quran.R
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.prayer.getNearestPrayer
import com.acun.quran.data.remote.response.prayer.model.hour
import com.acun.quran.data.remote.response.prayer.model.minute
import com.acun.quran.data.remote.response.prayer.toPrayerList
import com.acun.quran.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                Toast.makeText(requireContext(), "Permission Deny", Toast.LENGTH_SHORT).show()
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            return
        } else {
            getLocation()
        }

        observePrayer()
    }

    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc ->
                binding.textViewCurrentLocation.text = getLocationName(loc.latitude, loc.longitude)
                viewModel.getPrayer(lat = loc.latitude, long = loc.longitude)
            }
    }

    private fun getLocationName(lat: Double, long: Double): String {
        val addresses = Geocoder(requireContext()).getFromLocation(lat, long, 1)
        return addresses?.let { it[0].locality } ?: requireContext().getString(R.string.error_location_not_found)
    }

    private fun observePrayer() {
        val day = Calendar.getInstance(TimeZone.getTimeZone("Jakarta/Asia"), Locale.getDefault()).get(Calendar.DAY_OF_MONTH)
        viewModel.prayer.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Loading -> Unit
                is Resource.Success -> {
                    resources.data?.get(day)?.timings?.let { time ->
                        binding.rvPrayerTime.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvPrayerTime.adapter = PrayerTimeAdapter(time.toPrayerList())
                        binding.textViewNextPrayerName.text = "next: ${time.getNearestPrayer()?.name}"
                        binding.textViewNextPrayerTime.text = time.getNearestPrayer()?.time

                        val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"), Locale.getDefault()).time.time
                        val nearest = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"), Locale.getDefault())
                            .apply {
                                set(Calendar.YEAR, get(Calendar.YEAR))
                                set(Calendar.MONTH, get(Calendar.MONTH))
                                if (get(Calendar.HOUR_OF_DAY) >= 20) {
                                    set(Calendar.DATE, get(Calendar.DATE)+1)
                                } else {
                                    set(Calendar.DATE, get(Calendar.DATE))
                                }
                                time.getNearestPrayer()?.let {
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

    fun TextView.prayerTimeCounter(diff: Long) {
        object : CountDownTimer(diff, 1000) {
            override fun onTick(timeInMiles: Long) {
                val hour = timeInMiles / (1000 * 60 * 60)
                val minute = timeInMiles / (1000 * 60) % 60
                val second = (timeInMiles / 1000) % 60
                fun timeFormat(time: Long) = String.format("%02d", time)
                this@prayerTimeCounter.text = "in ${timeFormat(hour)}h ${timeFormat(minute)}m ${timeFormat(second)}s"
            }

            override fun onFinish() {
                getLocation()
            }
        }.start()
    }
}