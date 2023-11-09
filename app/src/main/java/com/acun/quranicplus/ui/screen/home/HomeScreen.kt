package com.acun.quranicplus.ui.screen.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.data.remote.response.prayer.model.hour
import com.acun.quranicplus.data.remote.response.prayer.model.minute
import com.acun.quranicplus.ui.component.HomeCard
import com.acun.quranicplus.ui.component.PrayerTimeList
import com.acun.quranicplus.ui.component.QiblaFinderComponent
import com.acun.quranicplus.ui.component.TopBarComponent
import com.acun.quranicplus.ui.theme.Mariner
import com.acun.quranicplus.ui.theme.Poppins
import com.acun.quranicplus.util.DEFAULT_LAT
import com.acun.quranicplus.util.DEFAULT_LONG
import com.google.android.gms.location.LocationServices
import java.util.Calendar
import java.util.Calendar.MINUTE
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var locationState by remember { mutableStateOf(Location("my_location")) }
    var compassDegreeState by remember { mutableFloatStateOf(0f) }
    var kaabaDegreeState by remember { mutableFloatStateOf(0f) }

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensorListener = object: SensorEventListener2 {
        override fun onSensorChanged(event: SensorEvent) {
            val degree = event.values.firstOrNull()?.let {
                if (it.isNaN()) 0
                else it.roundToInt()
            } ?: 0
            val kaabaLocation = Location("kaaba_location").apply {
                latitude = 21.4234756
                longitude = 39.8246424
            }
            val bearingToKaaba = locationState.bearingTo(kaabaLocation)
            val direction = bearingToKaaba-degree

            kaabaDegreeState = direction
            compassDegreeState = -degree.toFloat()
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

        override fun onFlushCompleted(p0: Sensor?) = Unit

    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                sensorManager.registerListener(
                    sensorListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME
                )

                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) { return@LifecycleEventObserver }

                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        locationState = location
                        val addresses = try {
                            Geocoder(context).getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
                        } catch (_: Exception) { null }

                        viewModel.getPrayer(lat = location.latitude, long = location.longitude)
                        viewModel.setLocation(addresses?.locality ?: context.getString(R.string.error_location_not_found))
                    } else {
                        viewModel.getPrayer(lat = DEFAULT_LAT, long = DEFAULT_LONG)
                    }

                }
            } else if (event == Lifecycle.Event.ON_STOP) {
                sensorManager.unregisterListener(sensorListener)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            sensorManager.unregisterListener(sensorListener)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val location = viewModel.locationString.collectAsState().value
    val isTimerStarted = viewModel.isTimerStarted.collectAsState().value
    val state = viewModel.homeState.collectAsState().value

    var prayerName by remember { mutableStateOf("") }
    var prayerTime by remember { mutableStateOf("") }
    var prayerList by remember { mutableStateOf(listOf<Prayer>()) }

    val now = Calendar.getInstance().time.time
    state.prayerList.let { list ->
        prayerList = list
        val nearestPrayerTime = list.firstOrNull { it.isNearestPrayer }
        prayerName = stringResource(R.string.next_prayer, nearestPrayerTime?.name ?: "")
        prayerTime = nearestPrayerTime?.time ?: ""
        if (prayerList.isNotEmpty()) {
            val nearest = Calendar.getInstance().apply {
                if (get(Calendar.HOUR_OF_DAY) >= prayerList.last().hour()) {
                    add(Calendar.DATE, 1)
                }
                nearestPrayerTime?.let {
                    set(Calendar.HOUR_OF_DAY, it.hour())
                    set(MINUTE, it.minute())
                }
            }.time.time
            if (!isTimerStarted) {
                viewModel.setInitialTime(nearest-now)
                viewModel.startTimer()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarComponent(title = "Home")
        },
        backgroundColor = MaterialTheme.colors.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            HomeCard(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                nextPrayerName = prayerName,
                nextPrayerTime = prayerTime,
                nextPrayerCounter = stringResource(
                    R.string.prayer_time_counter,
                    state.time.hour,
                    state.time.minute,
                    state.time.second
                ),
                isLoading = state.isLoading
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = null,
                    tint = Mariner
                )
                Text(
                    text = location.ifEmpty { stringResource(id = R.string.error_location_not_found) },
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Divider(thickness = 6.dp, color = Color.Transparent)
            PrayerTimeList(prayerList = prayerList, isLoading = state.isLoading) {
                viewModel.updatePrayer(it)
            }
            Divider(thickness = 12.dp, color = Color.Transparent)
            QiblaFinderComponent(
                kaabaDegree = kaabaDegreeState,
                compassDegree = compassDegreeState
            )
        }
    }
}