package com.acun.quranicplus.ui.screen.quran

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.datastore.VersePreference
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.juz_list.JuzSurah
import com.acun.quranicplus.data.remote.response.surah_list.Name
import com.acun.quranicplus.data.remote.response.surah_list.Revelation
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.data.remote.response.surah_list.Tafsir
import com.acun.quranicplus.data.remote.response.surah_list.Translation
import com.acun.quranicplus.data.remote.response.surah_list.Transliteration
import com.acun.quranicplus.ui.component.LoadingComponent
import com.acun.quranicplus.ui.component.TabComponent
import com.acun.quranicplus.ui.component.TopBarComponent
import com.acun.quranicplus.ui.theme.AliceBlue
import com.acun.quranicplus.ui.theme.HavelockBlue
import com.acun.quranicplus.ui.theme.Mariner
import com.acun.quranicplus.ui.theme.Misbah
import com.acun.quranicplus.ui.theme.Poppins
import com.acun.quranicplus.util.Language

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuranScreen(
    viewModel: QuranViewModel,
    onSurahDetailClicked: (Surah) -> Unit,
    onJuzDetailClicked: (Juz, Int) -> Unit,
    onSearchClicked: () -> Unit,
    onLastReadClicked: (Surah, Int) -> Unit
) {
    val tabTitle = arrayOf(stringResource(id = R.string.surah_title), stringResource(id = R.string.juz_title))
    val lastRead = viewModel.lastRead.observeAsState()
    val preference = viewModel.preference.observeAsState()

    val lastSurah = lastRead.value?.surah ?: ""
    val surahState = viewModel.surahList.observeAsState()
    val surahList = mutableListOf<Surah>()
    val juzState = viewModel.juzList.observeAsState()
    val juzList = mutableListOf<Juz>()

    surahState.value?.surahList?.let { surahList.addAll(it) }
    juzState.value?.juzList?.let { juzList.addAll(it) }

    Scaffold(
        topBar = { TopBarComponent(title = "Quran") },
        backgroundColor = MaterialTheme.colors.surface
    ) { paddingValues ->
        BoxWithConstraints {
            val screenHeight = maxHeight
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                val lastAyah = if (lastRead.value?.numberInSurah != 0) {
                    stringResource(R.string.last_read_value, lastRead.value?.numberInSurah ?: 0 )
                } else "You haven't read anything, yet."

                QuranCard(
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                        .clickable {
                            onLastReadClicked(
                                Surah(
                                    name = Name(
                                        long = "",
                                        short = "",
                                        translation = Translation(
                                            en = "",
                                            id = ""
                                        ),
                                        transliteration = Transliteration(
                                            en = "",
                                            id = ""
                                        )
                                    ),
                                    number = lastRead.value?.number ?: 0,
                                    numberOfVerses = 0,
                                    revelation = Revelation(
                                        arab = "",
                                        en = "",
                                        id = ""
                                    ),
                                    sequence = 0,
                                    tafsir = Tafsir(id = "")
                                ),
                                lastRead.value?.numberInSurah ?: 0
                            )
                        },
                    lastAyah = lastAyah,
                    lastSurah = lastSurah
                )

                Column(
                    modifier = Modifier.height(screenHeight)
                ) {
                    val pagerState = rememberPagerState()
                    TabComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.surface)
                            .padding(horizontal = 18.dp),
                        tabTitle = tabTitle,
                        pagerState = pagerState
                    )
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxHeight()
                            .nestedScroll(
                                remember {
                                    object : NestedScrollConnection {
                                        override fun onPreScroll(
                                            available: Offset,
                                            source: NestedScrollSource
                                        ): Offset {
                                            return if (available.y > 0) Offset.Zero else Offset(
                                                x = 0f,
                                                y = -scrollState.dispatchRawDelta(-available.y)
                                            )
                                        }
                                    }
                                }
                            ),
                        pageCount = tabTitle.size,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> {
                                if (surahState.value?.isLoading == true) {
                                    LoadingComponent(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(.7f)
                                    )
                                } else {
                                    LazyColumn {
                                        itemsIndexed(items = surahList) { index, surah ->
                                            SurahItem(
                                                modifier = Modifier.padding(horizontal = 24.dp),
                                                surah = surah,
                                                preference = preference,
                                                isDividerEnabled = (index != surahList.lastIndex)
                                            ) { surahData, _ ->
                                                surahData?.let(onSurahDetailClicked)
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {
                                if (juzState.value?.isLoading == true) {
                                    LoadingComponent(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(.7f)
                                    )
                                } else {
                                    LazyColumn {
                                        juzList.forEachIndexed { index, juz ->
                                            item {
                                                ItemHeader(
                                                    modifier = Modifier
                                                        .padding(horizontal = 24.dp),
                                                    juz = juz
                                                )
                                            }
                                            itemsIndexed(items = juz.surah) {i, juzData ->
                                                SurahItem(
                                                    modifier = Modifier.padding(horizontal = 24.dp),
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
                                                        } },
                                                    isDividerEnabled = !(index == juzList.lastIndex && i == juz.surah.lastIndex),
                                                    juz = juzData,
                                                    preference = preference
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .padding(18.dp)
                    .align(Alignment.BottomEnd),
                backgroundColor = MaterialTheme.colors.surface,
                onClick = {
                    onSearchClicked()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = MaterialTheme.colors.primary,
                    contentDescription = null
                )
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
                    if (isSystemInDarkTheme()) {
                        listOf(Mariner.copy(.9f), HavelockBlue.copy(.9f))
                    } else {
                        listOf(Mariner, HavelockBlue)
                    }
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
                    text = stringResource(id = R.string.last_read),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = AliceBlue
                )
            }
            Column {
                Text(
                    text = lastSurah,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = AliceBlue
                )
                Text(
                    text = lastAyah,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = AliceBlue
                )
            }
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
    preference: State<VersePreference?>,
    isDividerEnabled: Boolean = true,
    onItemClicked: (Surah?, JuzSurah?) -> Unit
) {
    val isEng = preference.value?.languagePos == Language.ENG
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
                        text = (if (isEng) surah?.name?.transliteration?.en else surah?.name?.transliteration?.id) ?: juz?.name ?: "",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.onSurface
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        text = surah?.numberOfVerses?.let { stringResource(id = R.string.number_of_verses, it) } ?: "${juz?.start} - ${juz?.end}",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = surah?.name?.short ?: juz?.name_arab ?: "",
                fontFamily = Misbah,
                fontSize = 22.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        if (isDividerEnabled) {
            Divider(
                modifier = Modifier.alpha(0.15f),
                thickness = 1.dp,
                color = MaterialTheme.colors.onSecondary
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
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = Mariner
        )
    }
}

@Composable
fun ItemHeader(
    modifier: Modifier = Modifier,
    juz: Juz
) {
    Row(
        modifier = modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(top = 1.dp),
            text = stringResource(id = R.string.juz, juz.juz),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(id = R.string.juz_number_of_verses, juz.totalVerses),
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Mariner
        )
    }
}