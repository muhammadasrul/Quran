package com.acun.quranicplus.data.remote.response.prayer

import com.google.gson.annotations.SerializedName

data class PrayerTimeData(
    @SerializedName("date")
    val date: Date,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("timings")
    val timings: Timings
)