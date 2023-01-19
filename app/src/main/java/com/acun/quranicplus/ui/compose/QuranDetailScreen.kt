package com.acun.quranicplus.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.datastore.LastReadVerse
import com.acun.quranicplus.data.local.datastore.VersePreference
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.surah.SurahDetail
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.ui.compose.theme.misbah
import com.acun.quranicplus.ui.compose.theme.poppins
import com.acun.quranicplus.ui.quran.surah_detail.SurahDetailViewModel

@Composable
fun QuranDetailScreen(
    surahNavArgs: Surah,
    viewModel: SurahDetailViewModel,
    onBackPressed: () -> Unit,
    onShareClicked: (Verse) -> Unit
) {
    var verseList by remember { mutableStateOf(listOf<Verse>()) }

    val lastReadVerseState = viewModel.lastRead.observeAsState()
    verseList.ifEmpty { viewModel.getSurah(surahNavArgs.number) }
    val verseState = viewModel.surahDetail.observeAsState()

    val versePreference = viewModel.versePreference.observeAsState()

    Scaffold(
        topBar = {
            TopBarComponent(
                title = surahNavArgs.name.transliteration.en,
                rightIcon = R.drawable.ic_arrow_left,
                onRightIconClicked = { onBackPressed() }
            )
        }
    ) { paddingValues ->
        when (verseState.value) {
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                (verseState.value as Resource.Success<SurahDetail>).data?.let { surah ->
                    verseList = surah.verses
                    if (verseList.isNotEmpty()) {
                        verseList[0].surahName = surahNavArgs.name.transliteration.en
                        verseList[0].surahNameTranslation = stringResource(R.string.surah_detail_verse_name, surah.name.translation.en)
                        verseList[0].numberOfVerse = stringResource(R.string.number_of_verses, surah.numberOfVerses)
                    }
                    verseList.firstOrNull { it.number.inQuran == lastReadVerseState.value?.numberInQuran }?.isBookmark = true

                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues)
                    ) {
                        item {
                            VerseHeader(verseList.firstOrNull())
                            Divider(color = Color.Transparent, thickness = 12.dp)
                        }
                        itemsIndexed(items = verseList) {index, verse ->
                            VerseItem(
                                versePreference = versePreference.value,
                                verse = verse,
                                isDividerVisible = index != verseList.lastIndex,
                                isBookmarked = verse.isBookmark,
                                onBookmarkClicked = { v ->
                                    val s = verseList.firstOrNull { it.surahName.isNotEmpty() }?.surahName ?: ""
                                    val temp = lastReadVerseState.value
                                    viewModel.setLastRead(LastReadVerse(
                                        surah = s,
                                        numberInSurah = v.number.inSurah,
                                        numberInQuran = v.number.inQuran
                                    ))

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
            is Resource.Failed -> {

            }
            else -> { }
        }
    }
}

@Composable
fun VerseItem(
    verse: Verse,
    versePreference: VersePreference?,
    isDividerVisible: Boolean,
    isBookmarked: Boolean,
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = if (isBookmarked) R.drawable.ic_bookmark_active else R.drawable.ic_bookmark),
                        contentDescription = null,
                        tint = colorResource(id = R.color.primary_blue)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { onShareClicked(verse) }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_share_24),
                        contentDescription = null,
                        tint = colorResource(id = R.color.primary_blue)
                    )
                }
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = verse.text.arab,
            fontFamily = misbah,
            fontSize = arabFontSize,
            color = colorResource(id = R.color.text_black),
            textAlign = TextAlign.End
        )
        if (versePreference?.transliteration == true) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = verse.text.transliteration.en,
                fontFamily = poppins,
                fontSize = fontSize,
                color = colorResource(id = R.color.text_black_light)
            )
        }
        if (versePreference?.translation == true) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = verse.translation.en,
                fontFamily = poppins,
                fontSize = fontSize,
                color = colorResource(id = R.color.text_black)
            )
        }

        Divider(
            modifier = Modifier
                .padding(top = 8.dp)
                .alpha(.3f),
            color = if (isDividerVisible) colorResource(id = R.color.text_black_light) else Color.Transparent,
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
                color = colorResource(id = R.color.primary_blue),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = number,
            textAlign = TextAlign.Center,
            fontFamily = poppins,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.primary_blue)
        )
    }
}


@Composable
fun VerseHeader(verse: Verse?) {
    Box(
        modifier = Modifier
            .padding(18.dp)
            .border(
                width = .7.dp,
                color = colorResource(id = R.color.primary_blue),
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .height(112.dp)
            .background(color = colorResource(id = R.color.blue_background))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = verse?.surahName ?: "Surah Name",
                fontFamily = poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.text_black)
            )
            Text(
                text = verse?.surahNameTranslation ?: "Surah Name Translation",
                fontFamily = poppins,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.text_black)
            )
            Text(
                text = verse?.numberOfVerse ?: "Number of Verse",
                fontFamily = poppins,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.text_black)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.fg_prayer),
            contentDescription = null,
            alignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth(),
            colorFilter = ColorFilter.tint(color = colorResource(id = R.color.primary_blue))
        )
    }
}

@Preview
@Composable
fun VerseHeaderPreview() {
    VerseHeader(verse = null)
}