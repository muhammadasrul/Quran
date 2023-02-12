package com.acun.quranicplus.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.ui.theme.AliceBlue
import com.acun.quranicplus.ui.theme.Poppins
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabComponent(
    modifier: Modifier = Modifier,
    tabTitle: Array<String>,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        modifier = modifier
            .wrapContentHeight()
            .padding(4.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
        ,
        indicator = { Box{} },
        backgroundColor = MaterialTheme.colors.background,
        selectedTabIndex = pagerState.currentPage,
        divider = { Box {} }
    ) {
        tabTitle.forEachIndexed { index, title ->
            val isSelected = pagerState.currentPage == index
            Tab(
                selected = isSelected,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(4.dp)
                        .background(
                            if (isSelected) {
                                Brush.linearGradient(listOf(
                                    MaterialTheme.colors.primary,
                                    MaterialTheme.colors.primaryVariant))
                            } else {
                                Brush.linearGradient(listOf(
                                    MaterialTheme.colors.background,
                                    MaterialTheme.colors.background))
                            },
                            RoundedCornerShape(14.dp)
                        )
                        .padding(vertical = 8.dp),
                    text = title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = if (isSelected) AliceBlue else MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}