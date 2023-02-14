package com.acun.quranicplus.ui.screen.quran.share

import com.acun.quranicplus.data.remote.response.juz_list.Juz

data class JuzState(
    val juzList: List<Juz> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
