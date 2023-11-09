package com.acun.quranicplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.ui.theme.Mariner
import com.acun.quranicplus.ui.theme.Poppins
import com.acun.quranicplus.util.shimmer

@Composable
fun PrayerTimeList(
    prayerList: List<Prayer>,
    isLoading: Boolean,
    onSoundIconClicked: (Prayer) -> Unit
) {
    val prayerListState = rememberLazyListState()
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(id = R.string.prayer_times),
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )
            Divider(thickness = 8.dp, color = Color.Transparent)
            LazyRow(state = prayerListState) {
                if (isLoading) {
                    items(5) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val shimmerColor = Mariner.copy(.3f)
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clip(CircleShape)
                                    .shimmer(shimmerColor)
                                    .size(70.dp)
                            )
                            Divider(color = Color.Transparent, thickness = 8.dp)
                            Box(modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .width(50.dp)
                                .height(14.dp)
                                .shimmer(shimmerColor)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                    }
                } else {
                    itemsIndexed(items = prayerList) { index, item ->
                        if (index == 0) {
                            Spacer(modifier = Modifier.width(14.dp))
                        }
                        PrayerItem(item) {
                            onSoundIconClicked(it.copy())
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                    }
                }
            }
            prayerList.firstOrNull { it.isNowPrayer }?.let {
                LaunchedEffect(key1 = true) {
                    prayerListState.animateScrollToItem(prayerList.indexOf(it))
                }
            }
        }
    }
}