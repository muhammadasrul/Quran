package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.prayer.getNearestPrayer
import com.acun.quranicplus.data.remote.response.prayer.getNowPrayer
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.data.remote.response.prayer.model.hour
import com.acun.quranicplus.data.remote.response.prayer.model.minute
import com.acun.quranicplus.data.remote.response.prayer.toPrayerList
import com.acun.quranicplus.ui.compose.theme.poppins
import com.acun.quranicplus.ui.home.HomeViewModel
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    Scaffold(
        topBar = {
            TopBarComponent(title = "Home")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            val prayer = viewModel.prayer.observeAsState()
            val location = viewModel.location.observeAsState()

            var prayerName by remember { mutableStateOf("") }
            var prayerTime by remember { mutableStateOf("") }

            var prayerList by remember { mutableStateOf(listOf<Prayer>()) }

            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            prayer.value?.data?.get(day)?.timings?.let { time ->
                prayerList = time.toPrayerList()

                val nowPrayerTime = time.getNowPrayer()
                if (nowPrayerTime != null) {
                    // TODO: Scroll to now prayer
//                    binding.rvPrayerTime.scrollToPosition(prayerList.indexOf(nowPrayerTime))
                }

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

                val diff = nearest-now
                viewModel.setInitialTime(diff)
                viewModel.startTimer()
            }

            val time = viewModel.timeString.observeAsState()

            HomeCard(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                nextPrayerName = prayerName,
                nextPrayerTime = prayerTime,
                nextPrayerCounter = time.value ?: ""
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
                    text = "Kecamatan Sewon",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppins,
                    fontSize = 13.sp
                )
            }
            Divider(thickness = 6.dp, color = Color.Transparent)
            PrayerTimesComponent(prayerList)
            Divider(thickness = 8.dp, color = Color.Transparent)
            QiblaFinderComponent()
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
            painter = painterResource(id = R.drawable.malam),
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
    prayerList: List<Prayer>
) {
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
            LazyRow {
                itemsIndexed(items = prayerList) { index, item ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(14.dp))
                    }
                    PrayerItem(item)
                    Spacer(modifier = Modifier.width(14.dp))
                }
            }
        }
    }
}

@Composable
fun QiblaFinderComponent() {
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
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.compass),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.kaaba_directioin),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}