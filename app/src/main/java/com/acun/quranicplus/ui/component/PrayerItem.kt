package com.acun.quranicplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.alarm_shceduler.AdzanAlarmScheduler
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.ui.theme.HavelockBlue
import com.acun.quranicplus.ui.theme.Poppins

@Composable
fun PrayerItem(
    prayer: Prayer,
    onSoundIconClicked : (Prayer) -> Unit
) {
    val adzanScheduler = AdzanAlarmScheduler(LocalContext.current)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .wrapContentSize()
                    .background(MaterialTheme.colors.onBackground)
                    .border(
                        width = if (prayer.isNowPrayer) 1.3.dp else 0.dp,
                        color = HavelockBlue,
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
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .absoluteOffset(12.dp, 12.dp),
                onClick = {
                    if (!prayer.isNotificationOn) {
                        adzanScheduler.scheduler(prayer)
                    } else {
                        adzanScheduler.cancel(prayer)
                    }
                    prayer.isNotificationOn = !prayer.isNotificationOn
                    onSoundIconClicked(prayer)
                }
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFCBEAC4))
                ) {
                    Icon(
                        painter = painterResource(
                            if (prayer.isNotificationOn) R.drawable.ic_volume_on
                            else R.drawable.ic_volume_off
                        ),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = prayer.name,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = MaterialTheme.colors.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}