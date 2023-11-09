package com.acun.quranicplus.ui.screen.home

import com.acun.quranicplus.data.remote.response.prayer.model.Prayer

data class HomeState(
    val prayerList: List<Prayer> = listOf(),
    val time: PrayerTime = PrayerTime(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

data class PrayerTime(
    val hour: String = "",
    val minute : String = "",
    val second: String = ""
)
