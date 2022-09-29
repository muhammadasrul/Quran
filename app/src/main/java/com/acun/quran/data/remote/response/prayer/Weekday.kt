package com.acun.quran.data.remote.response.prayer

import com.google.gson.annotations.SerializedName

data class Weekday(
    @SerializedName("en")
    val en: String
)