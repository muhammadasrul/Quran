package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.juz_list.JuzSurah
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.ui.compose.theme.misbah
import com.acun.quranicplus.ui.compose.theme.poppins
import com.acun.quranicplus.ui.quran.QuranViewModel

@Composable
fun QuranScreen(
    viewModel: QuranViewModel,
    onSelectedTab: (Int) -> Unit,
    onSurahDetailClicked: (Surah) -> Unit,
    onJuzDetailClicked: (Juz, Int) -> Unit
) {
    val tabTitle = arrayOf("Surah", "Juz")
    val lastRead = viewModel.lastRead.observeAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { TopBarComponent(title = "Quran") }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            val lastSurah = lastRead.value?.surah ?: ""
            val lastAyah = if (lastRead.value?.numberInSurah != 0) "Ayah no ${lastRead.value?.numberInSurah}" else "you haven't read anything"
            QuranCard(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                lastAyah = lastAyah,
                lastSurah = lastSurah
            )
            TabComponent(
                modifier = Modifier
                    .padding(horizontal = 18.dp),
                tabTitle = tabTitle,
                selectedTab = selectedTab,
                onSelectedTab = {
                    selectedTab = it
                    onSelectedTab(it)
                }
            )
            val surahState = viewModel.surahList.observeAsState()
            val juzState = viewModel.juzList.observeAsState()
            when (selectedTab) {
                0 -> {
                    when (surahState.value) {
                        is Resource.Loading -> {
                            // TODO: Add Loading State
                        }
                        is Resource.Success -> { LazyColumn {
                            (surahState.value as Resource.Success<List<Surah>>).data?.let { surahList ->
                                itemsIndexed(items = surahList) { index, surah ->
                                    SurahItem(
                                        modifier = Modifier.padding(horizontal = 24.dp),
                                        surah = surah,
                                        isDividerEnabled = (index == surahList.lastIndex)
                                    ) { surahData, _ ->
                                        surahData?.let(onSurahDetailClicked)
                                    }
                                }
                            }
                        } }
                        is Resource.Failed -> {
                            // TODO: Add Error State
                        }
                        else -> { }
                    }
                }
                1 -> {
                    when (juzState.value) {
                        is Resource.Loading -> {
                            // TODO: Add Loading State
                        }
                        is Resource.Success -> {
                            LazyColumn(
                                modifier = Modifier.padding(horizontal = 24.dp),
                            ) {
                                (juzState.value as Resource.Success<List<Juz>>).data?.let { juzList ->
                                    juzList.forEachIndexed { index, juz ->
                                        item {
                                            ItemHeader(juz = juz)
                                        }

                                        itemsIndexed(items = juz.surah) {i, juzData ->
                                            SurahItem(
                                                onItemClicked = { _, _ ->
                                                    var pos = juz.surah[0].end-juz.surah[0].start+1
                                                    when (i) {
                                                        0 -> {
                                                            onJuzDetailClicked(juz, 0)
                                                        }
                                                        1 -> {
                                                            onJuzDetailClicked(juz, pos)
                                                        }
                                                        else -> {
                                                            for (j in 1 until i) {
                                                                pos+=juz.surah[i].end
                                                            }
                                                            onJuzDetailClicked(juz, pos)
                                                        }
                                                    }
                                                },
                                                isDividerEnabled = !(index == juzList.lastIndex && i == juz.surah.lastIndex),
                                                juz = juzData
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        is Resource.Failed -> {
                            // TODO: Add Error State
                        }
                        else -> { }
                    }
                }
            }
        }
    }
}

@Composable
fun QuranCard(
    modifier: Modifier = Modifier,
    lastAyah: String,
    lastSurah: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        colorResource(id = R.color.primary_blue),
                        colorResource(id = R.color.primary_blue_light)
                    )
                )
            )
            .height(164.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_bookmark_white_bg), 
                    contentDescription = "Last Read"
                )
                Text(
                    modifier = Modifier.padding(top = 1.dp, start = 8.dp),
                    text = "Last Read",
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.white)
                )
            }
            Text(
                text = "$lastSurah $lastAyah",
                fontFamily = poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = colorResource(id = R.color.white)
            )
        }
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                modifier = Modifier
                    .size(146.dp),
                painter = painterResource(id = R.drawable.fg_circle),
                contentDescription = "Quran Image",
                alignment = Alignment.TopEnd
            )
            Image(
                modifier = Modifier
                    .padding(top = 24.dp, end = 8.dp)
                    .size(182.dp),
                painter = painterResource(id = R.drawable.img_quran),
                contentDescription = "Quran Image"
            )
        }
    }
}

@Composable
fun SurahItem(
    modifier: Modifier = Modifier,
    surah: Surah? = null,
    juz: JuzSurah? = null,
    isDividerEnabled: Boolean = true,
    onItemClicked: (Surah?, JuzSurah?) -> Unit
) {
    Column(
        modifier = modifier.clickable {
            onItemClicked(surah, juz)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                surah?.number?.let {
                    Number(number = it.toString())
                } ?: juz?.no?.let {
                    Number(number = it.toString())
                }
                Column {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        text = surah?.name?.transliteration?.en ?: juz?.name ?: "",
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.text_black)
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        text = surah?.numberOfVerses?.let { stringResource(id = R.string.number_of_verses, it) } ?: "${juz?.start} - ${juz?.end}",
                        fontFamily = poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = colorResource(id = R.color.text_black_light)
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = surah?.name?.short ?: juz?.name_arab ?: "",
                fontFamily = misbah,
                fontSize = 22.sp,
                color = colorResource(id = R.color.text_black)
            )
        }
        if (isDividerEnabled) {
            Divider(
                modifier = Modifier.alpha(0.15f),
                thickness = 1.dp,
                color = colorResource(id = R.color.text_black_light)
            )
        }
    }
}

@Composable
fun Number(
    number: String
) {
    Box(
        modifier = Modifier
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(34.dp),
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "Number"
        )
        Text(
            modifier = Modifier.padding(top = 1.dp),
            text = number,
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = colorResource(id = R.color.primary_blue)
        )
    }
}

@Composable
fun ItemHeader(
    juz: Juz
) {
    Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(top = 1.dp),
            text = stringResource(id = R.string.juz, juz.juz),
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = colorResource(id = R.color.text_black)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(id = R.string.juz_number_of_verses, juz.totalVerses),
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = colorResource(id = R.color.primary_blue)
        )
    }
}