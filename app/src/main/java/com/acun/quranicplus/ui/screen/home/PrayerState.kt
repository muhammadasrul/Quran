package com.acun.quranicplus.ui.screen.home

import com.acun.quranicplus.data.remote.response.prayer.model.Prayer

data class PrayerState(
    val prayerList: List<Prayer> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
