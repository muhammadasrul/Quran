package com.acun.quranicplus.ui.screen.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.ui.theme.Poppins

@Composable
fun SearchScreen(
    viewModel: QuranViewModel,
    onSurahDetailClicked: (Surah) -> Unit,
) {
    val preference = viewModel.preference.observeAsState()
    val surahState = viewModel.filteredSurahList.observeAsState()
    val surahList = mutableListOf<Surah>()
    val query = remember { mutableStateOf("") }

    surahState.value?.surahList?.let {
        surahList.clear()
        surahList.addAll(it)
    }

    LaunchedEffect(query.value) {
        viewModel.updateQuery(query.value)
    }

    Scaffold { paddingValue ->
        Column(
            modifier = Modifier.padding(paddingValue)
        ) {
            SearchBar(
                modifier = Modifier.padding(12.dp),
                query = query
            )
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
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: MutableState<String>
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32))
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        if (query.value.isEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    text = "Search",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontFamily = Poppins,
                fontSize = 15.sp,
            ),
            value = query.value,
            onValueChange = {
                query.value = it
            }
        )
    }
}