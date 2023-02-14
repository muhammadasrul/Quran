package com.acun.quranicplus.ui.screen.quran.detail

import com.acun.quranicplus.data.remote.response.surah.SurahDetail

data class SurahDetailState(
    val surahDetail: SurahDetail? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
