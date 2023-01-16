package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.ui.compose.theme.poppins

@Composable
fun TabComponent(
    modifier: Modifier = Modifier,
    tabTitle: Array<String>,
    selectedTab: Int,
    onSelectedTab: (Int) -> Unit
) {
    TabRow(
        modifier = modifier
            .wrapContentHeight()
            .padding(4.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
        ,
        indicator = { Box{} },
        backgroundColor = colorResource(id = R.color.white),
        selectedTabIndex = selectedTab,
        divider = { Box {} }
    ) {
        tabTitle.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            Tab(
                selected = isSelected,
                onClick = {
                    onSelectedTab(index)
                }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(4.dp)
                        .background(
                            if (isSelected) {
                                Brush.linearGradient(
                                    listOf(
                                        colorResource(id = R.color.primary_blue),
                                        colorResource(id = R.color.primary_blue_light)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        colorResource(id = R.color.white),
                                        colorResource(id = R.color.white)
                                    )
                                )
                            },
                            RoundedCornerShape(14.dp)
                        )
                        .padding(vertical = 8.dp),
                    text = title,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = colorResource(
                        if (isSelected) R.color.white else R.color.primary_blue
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = android.graphics.Color.BLACK.toLong())
@Composable
fun TabComponentPreview() {
    TabComponent(tabTitle = arrayOf("Test"), selectedTab = 0, onSelectedTab = {})
}