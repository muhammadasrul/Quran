package com.acun.quranicplus.ui.screen.quran.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.VersePreference
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.ui.component.LoadingComponent
import com.acun.quranicplus.ui.component.TopBarComponent
import com.acun.quranicplus.ui.theme.Mariner
import com.acun.quranicplus.ui.theme.Misbah
import com.acun.quranicplus.ui.theme.Poppins
import com.acun.quranicplus.util.Language.ENG
import kotlinx.coroutines.launch

@Composable
fun QuranDetailScreen(
    surahNavArgs: Surah? = null,
    juzNavArgs: Juz? = null,
    juzPos: Int = 0,
    viewModel: DetailViewModel,
    onBackPressed: () -> Unit,
    onShareClicked: (Verse) -> Unit
) {
    var verseList by remember { mutableStateOf(listOf<Verse>()) }
    val verseListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val versePreference = viewModel.versePreference.observeAsState()
    val lastReadVerseState = viewModel.lastRead.observeAsState()
    val surahVerseState = viewModel.surahDetail.observeAsState()
    val juzVerseState = viewModel.juzDetail.observeAsState()

    val indexOfCurrentPlay = viewModel.currentPlayIndex.observeAsState()
    val isPlay = viewModel.isPlay.observeAsState()
    val playbackState = viewModel.playbackState.observeAsState()

    surahNavArgs?.number?.let {
        LaunchedEffect(key1 = true) {
            viewModel.getSurah(it)
        }
    }
    juzNavArgs?.juz?.let {
        LaunchedEffect(key1 = true) {
            viewModel.getJuzDetail(it)
        }
    }

    // surah verse
    surahVerseState.value?.surahDetail?.let { surah ->
        verseList =
            surah.verses.map { it.copy(surahName = surahNavArgs?.name?.transliteration?.en ?: "") }
        if (verseList.isNotEmpty()) {
            verseList[0].headerName = surahNavArgs?.name?.transliteration?.en ?: ""
            verseList[0].surahNameTranslation =
                stringResource(R.string.surah_detail_verse_name, surah.name.translation.en)
            verseList[0].numberOfVerse =
                stringResource(R.string.number_of_verses, surah.numberOfVerses)
        }
        verseList.firstOrNull { it.number.inQuran == lastReadVerseState.value?.numberInQuran }?.isBookmark =
            true

        LaunchedEffect(true) {
            verseListState.scrollToItem(juzPos)
        }
    }

    // juz verse
    juzVerseState.value?.juzDetail?.let { juz ->
        verseList = juz.verses
        val pos = mutableListOf<Int>()
        juz.verses.forEachIndexed { i, verse ->
            if ((verse.number.inSurah != 1 && i == 0) || verse.number.inSurah == 1) {
                pos.add(pos.size)
                verseList[i].headerName = juzNavArgs?.surah?.get(pos.lastIndex)?.name ?: ""
                verseList[i].numberOfVerse = ""
                verseList[i].surahNameTranslation = ""
                verseList[i].surahName = juzNavArgs?.surah?.get(pos.lastIndex)?.name ?: ""
            } else {
                verseList[i].headerName = ""
                verseList[i].numberOfVerse = ""
                verseList[i].surahNameTranslation = ""
                verseList[i].surahName = juzNavArgs?.surah?.get(pos.lastIndex)?.name ?: ""
            }
        }

        verseList.firstOrNull { it.number.inQuran == lastReadVerseState.value?.numberInQuran }?.isBookmark = true

        LaunchedEffect(true) {
            verseListState.scrollToItem(juzPos)
        }
    }
    isLoading = surahVerseState.value?.isLoading == true || juzVerseState.value?.isLoading == true

    Scaffold(
        topBar = {
            val title = surahNavArgs?.name?.transliteration?.en ?: juzNavArgs?.juz?.let {stringResource(id = R.string.juz,  it) } ?: ""
            TopBarComponent(
                title = title,
                leftIcon = R.drawable.ic_arrow_left,
                onLeftIconClicked = { onBackPressed() }
            )
        },
        backgroundColor = MaterialTheme.colors.surface
    ) { paddingValues ->
        LaunchedEffect(key1 = indexOfCurrentPlay.value) {
            coroutineScope.launch {
                if (verseListState.canScrollForward) {
                    indexOfCurrentPlay.value?.let { verseListState.animateScrollToItem(it) }
                }
            }
        }

        if (isLoading) {
            LoadingComponent(
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues),
                state = verseListState
            ) {
                itemsIndexed(items = verseList) { index, verse ->
                    val isHeaderVisible = juzNavArgs?.let {
                        verse.headerName.isNotEmpty()
                    } ?: surahNavArgs?.let {
                        verse.headerName.isNotEmpty()
                    } ?: false

                    if (isHeaderVisible) {
                        VerseHeader(
                            verse = if (juzNavArgs != null) verse else verseList.firstOrNull(),
                            isHeaderOnly = juzNavArgs != null
                        )
                        Divider(color = Color.Transparent, thickness = 12.dp)
                    }
                    VerseItem(
                        versePreference = versePreference.value,
                        verse = verse,
                        isDividerVisible = index != verseList.lastIndex,
                        isPlay = index == indexOfCurrentPlay.value && isPlay.value == true,
                        playbackState = playbackState,
                        onPlayClicked = {
                            if (isPlay.value == true && index == indexOfCurrentPlay.value) {
                                viewModel.stop()
                            } else {
                                viewModel.play(it.audio.primary)
                            }
                        },
                        onBookmarkClicked = { v ->
                            val s = verseList.firstOrNull { it.surahName.isNotEmpty() }?.surahName ?: ""
                            val temp = lastReadVerseState.value
                            viewModel.setLastRead(
                                LastReadVerse(
                                    surah = s,
                                    numberInSurah = v.number.inSurah,
                                    numberInQuran = v.number.inQuran,
                                    number = surahNavArgs?.number ?: 0
                                )
                            )

                            val itemInList = verseList.firstOrNull { it.number.inQuran == v.number.inQuran }
                            val lastInList = verseList.firstOrNull { it.number.inQuran == temp?.numberInQuran }
                            itemInList?.let {
                                verseList[verseList.indexOf(it)].isBookmark = true
                            }
                            lastInList?.let {
                                verseList[verseList.indexOf(it)].isBookmark = false
                            }
                        },
                        onShareClicked = { onShareClicked(it) },
                    )
                }
            }
        }
    }
}

@Composable
fun VerseItem(
    verse: Verse,
    versePreference: VersePreference?,
    isDividerVisible: Boolean,
    isPlay: Boolean,
    playbackState: State<Int?>,
    onPlayClicked: (Verse) -> Unit,
    onBookmarkClicked: (Verse) -> Unit,
    onShareClicked: (Verse) -> Unit
) {
    val arabFontSize = when (versePreference?.textSizePos) {
        0 -> 18.sp
        1 -> 22.sp
        2 -> 24.sp
        else -> 18.sp
    }
    val fontSize = when (versePreference?.textSizePos) {
        0 -> 12.sp
        1 -> 13.sp
        2 -> 15.sp
        else -> 12.sp
    }
    val isEng = versePreference?.languagePos == ENG

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isPlay) MaterialTheme.colors.onBackground
                else Color.Transparent
            )
            .padding(vertical = 6.dp, horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleNumber(number = verse.number.inSurah.toString())
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBookmarkClicked(verse) }) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = if (verse.isBookmark) R.drawable.ic_bookmark_active else R.drawable.ic_bookmark),
                        contentDescription = null,
                        tint = Mariner
                    )
                }
                IconButton(onClick = { onPlayClicked(verse) }) {
                    if (isPlay && playbackState.value == Player.STATE_BUFFERING) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary,
                            strokeWidth = 2.dp
                        )
                    }
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = if (isPlay) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = null,
                        tint = Mariner
                    )
                }
                IconButton(onClick = { onShareClicked(verse) }) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_share_24),
                        contentDescription = null,
                        tint = Mariner
                    )
                }
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = verse.text.arab,
            fontFamily = Misbah,
            fontSize = arabFontSize,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.End
        )
        if (versePreference?.transliteration == true) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                text = verse.text.transliteration.en,
                fontFamily = Poppins,
                fontSize = fontSize,
                color = MaterialTheme.colors.onSecondary
            )
        }
        if (versePreference?.translation == true) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = if (isEng) verse.translation.en else verse.translation.id,
                fontFamily = Poppins,
                fontSize = fontSize,
                color = MaterialTheme.colors.onSurface
            )
        }

        Divider(
            modifier = Modifier
                .padding(top = 8.dp)
                .alpha(.12f),
            color = if (isDividerVisible) MaterialTheme.colors.onSecondary else Color.Transparent,
            thickness = 1.dp
        )
    }
}

@Composable
fun CircleNumber(number: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .border(
                width = .5.dp,
                color = Mariner,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = number,
            textAlign = TextAlign.Center,
            fontFamily = Poppins,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Mariner
        )
    }
}


@Composable
fun VerseHeader(
    verse: Verse?,
    isHeaderOnly: Boolean = false
) {
    Box(
        modifier = Modifier
            .padding(18.dp)
            .border(
                width = .7.dp,
                color = Mariner,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .height(if (isHeaderOnly) 42.dp else 112.dp)
            .background(color = MaterialTheme.colors.onBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = verse?.surahName ?: "Surah Name",
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface
            )
            if (!isHeaderOnly) {
                Text(
                    text = verse?.surahNameTranslation ?: "Surah Name Translation",
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = verse?.numberOfVerse ?: "Number of Verse",
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.fg_prayer),
            contentDescription = null,
            alignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth(),
            colorFilter = ColorFilter.tint(color = Mariner)
        )
    }
}

@Preview
@Composable
fun VerseHeaderPreview() {
    VerseHeader(verse = null, isHeaderOnly = true)
}