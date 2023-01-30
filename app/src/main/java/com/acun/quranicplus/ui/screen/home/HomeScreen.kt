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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.prayer.getNearestPrayer
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.data.remote.response.prayer.model.hour
import com.acun.quranicplus.data.remote.response.prayer.model.minute
import com.acun.quranicplus.data.remote.response.prayer.toPrayerList
import com.acun.quranicplus.ui.component.TopBarComponent
import com.acun.quranicplus.ui.theme.poppins
import com.acun.quranicplus.util.shimmer
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    var locationState by remember { mutableStateOf(Location("my_location")) }
    var compassDegreeState by remember { mutableStateOf(0F) }
    var kaabaDegreeState by remember { mutableStateOf(0F) }

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensorListener = object: SensorEventListener2 {
        override fun onSensorChanged(event: SensorEvent) {
            val degree = event.values[0].roundToInt()
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
    ) { return }

    LaunchedEffect(key1 = true) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            locationState = it
            val addresses = try {
                Geocoder(context).getFromLocation(it.latitude, it.longitude, 1)?.firstOrNull()
            } catch (e: Exception) {
                null
            }

            viewModel.getPrayer(lat = it.latitude, long = it.longitude)
            viewModel.setLocation(addresses?.locality ?: context.getString(R.string.error_location_not_found))
        }
    }

    val prayer = viewModel.prayer.observeAsState()
    val location = viewModel.locationString.observeAsState()
    val isTimerStarted = viewModel.isTimerStarted.observeAsState()
    val timeString = viewModel.timeString.observeAsState()

    var prayerName by remember { mutableStateOf("") }
    var prayerTime by remember { mutableStateOf("") }
    var prayerList by remember { mutableStateOf(listOf<Prayer>()) }
    var isLoading by remember { mutableStateOf(false) }

    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    when(prayer.value) {
        is Resource.Loading -> isLoading = true
        is Resource.Success -> {
            isLoading = false
            prayer.value?.data?.get(day)?.timings?.let { time ->
                prayerList = time.toPrayerList()
                val nearestPrayerTime = time.getNearestPrayer()
                prayerName = stringResource(R.string.next_prayer, nearestPrayerTime?.name ?: "")
                prayerTime = nearestPrayerTime?.time ?: ""
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
                if (isTimerStarted.value == false) {
                    viewModel.setInitialTime(nearest-now)
                    viewModel.startTimer()
                }
            }
        }
        is Resource.Failed -> isLoading = false
        else -> Unit
    }

    Scaffold(
        topBar = {
            TopBarComponent(title = "Home")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

            HomeCard(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                nextPrayerName = prayerName,
                nextPrayerTime = prayerTime,
                nextPrayerCounter = timeString.value ?: ""
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
                    tint = colorResource(id = R.color.primary_blue)
                )
                Text(
                    text = location.value ?: stringResource(id = R.string.error_location_not_found),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppins,
                    fontSize = 13.sp
                )
            }
            Divider(thickness = 6.dp, color = Color.Transparent)
            PrayerTimesComponent(prayerList = prayerList, isLoading = isLoading)
            Divider(thickness = 8.dp, color = Color.Transparent)
            QiblaFinderComponent(
                kaabaDegree = kaabaDegreeState,
                compassDegree = compassDegreeState
            )
        }
    }
}


@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    nextPrayerName: String,
    nextPrayerTime: String,
    nextPrayerCounter: String
) {

    val background = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 19 .. 24, in 1 .. 5 -> painterResource(id = R.drawable.malam)
        in 5..7 -> painterResource(id = R.drawable.pagi)
        in 7..15 -> painterResource(id = R.drawable.siang)
        in 15 .. 19 -> painterResource(id = R.drawable.sore)
        else -> painterResource(id = R.drawable.pagi)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .height(164.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = background,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            Text(
                text = nextPrayerName,
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = colorResource(id = R.color.white)
            )
            Text(
                text = nextPrayerTime,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = colorResource(id = R.color.white)
            )
            Text(
                text = nextPrayerCounter,
                fontFamily = poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = colorResource(id = R.color.white)
            )
        }
    }
}

@Composable
fun PrayerItem(
    prayer: Prayer
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .wrapContentSize()
                .background(Color(0xFFF2F5FE))
                .border(
                    width = if (prayer.isNowPrayer) 1.5.dp else 0.dp,
                    color = colorResource(id = R.color.primary_blue),
                    shape = CircleShape
                )
                .size(70.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = prayer.time
                    .replace("[()]".toRegex(), "")
                    .replace(" ", "\n"),
                fontFamily = poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_black),
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = prayer.name,
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = colorResource(id = R.color.text_black_light),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PrayerTimesComponent(
    prayerList: List<Prayer>,
    isLoading: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val prayerListState = rememberLazyListState()
    Box(
        modifier = Modifier
            .background(Color(0xFFFBFCFF))
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 18.dp),
                text = "Prayer Times",
                fontFamily = poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_black),
                textAlign = TextAlign.Center
            )
            Divider(thickness = 8.dp, color = Color.Transparent)
            LazyRow(state = prayerListState) {
                if (isLoading) {
                    items(5) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .wrapContentSize()
                                    .shimmer(colorResource(id = R.color.primary_blue_extra_light))
                                    .size(70.dp)
                            )
                            Divider(color = Color.Transparent, thickness = 8.dp)
                            Box(modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .width(50.dp)
                                .height(14.dp)
                                .shimmer(colorResource(id = R.color.primary_blue_extra_light))
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                    }
                } else {
                    itemsIndexed(items = prayerList) { index, item ->
                        if (index == 0) {
                            Spacer(modifier = Modifier.width(14.dp))
                        }
                        PrayerItem(item)
                        Spacer(modifier = Modifier.width(14.dp))
                    }
                }
            }
            prayerList.firstOrNull { it.isNowPrayer }?.let {
                coroutineScope.launch {
                    delay(100)
                    prayerListState.animateScrollToItem(prayerList.indexOf(it))
                }
            }
        }
    }
}

@Composable
fun QiblaFinderComponent(
    kaabaDegree: Float,
    compassDegree: Float
) {
    Column {
        Text(
            modifier = Modifier.padding(start = 18.dp),
            text = "Prayer Times",
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = colorResource(id = R.color.text_black),
            textAlign = TextAlign.Center
        )
        Divider(thickness = 8.dp, color = Color.Transparent)
        Box(
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(compassDegree),
                painter = painterResource(id = R.drawable.compass),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(kaabaDegree),
                painter = painterResource(id = R.drawable.kaaba_directioin),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}