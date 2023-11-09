package com.acun.quranicplus.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.theme.AliceBlue
import com.acun.quranicplus.ui.theme.GrayChateau
import com.acun.quranicplus.ui.theme.Poppins
import com.acun.quranicplus.util.shimmer
import java.util.Calendar

@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    nextPrayerName: String,
    nextPrayerTime: String,
    nextPrayerCounter: String,
    isLoading: Boolean
) {

    val background = if (isSystemInDarkTheme()) {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 19 .. 24, in 1 .. 5 -> painterResource(id = R.drawable.malam_dark)
            in 5..7 -> painterResource(id = R.drawable.pagi_dark)
            in 7..15 -> painterResource(id = R.drawable.siang_dark)
            in 15 .. 19 -> painterResource(id = R.drawable.sore_dark)
            else -> painterResource(id = R.drawable.pagi_dark)
        }
    } else {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 19 .. 24, in 1 .. 5 -> painterResource(id = R.drawable.malam)
            in 5..7 -> painterResource(id = R.drawable.pagi)
            in 7..15 -> painterResource(id = R.drawable.siang)
            in 15 .. 19 -> painterResource(id = R.drawable.sore)
            else -> painterResource(id = R.drawable.pagi)
        }
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
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmer(GrayChateau.copy(alpha = .3f))
                )
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(32.dp)
                        .width(152.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmer(GrayChateau.copy(alpha = .3f))
                )
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(12.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmer(GrayChateau.copy(alpha = .3f))
                )
            } else {
                Text(
                    text = nextPrayerName,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = AliceBlue
                )
                Text(
                    text = nextPrayerTime,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = AliceBlue
                )
                Text(
                    text = nextPrayerCounter,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = AliceBlue
                )
            }
        }
    }
}