package com.acun.quranicplus.data.remote.response.prayer

import com.google.gson.annotations.SerializedName

data class PrayerTimeResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<PrayerTimeData>,
    @SerializedName("status")
    val status: String
)