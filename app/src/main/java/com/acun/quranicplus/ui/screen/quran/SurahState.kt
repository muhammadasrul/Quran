package com.acun.quranicplus.ui.screen.quran

import com.acun.quranicplus.data.remote.response.surah_list.Surah

data class SurahState(
    val surahList: List<Surah> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
