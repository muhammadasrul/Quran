package com.acun.quranicplus.ui.screen.quran.detail

import com.acun.quranicplus.data.remote.response.juz.JuzDetail

data class JuzDetailState(
    val juzDetail: JuzDetail? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
