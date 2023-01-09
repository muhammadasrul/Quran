package com.acun.muslimplus.data.remote.response.prayer_time


import com.google.gson.annotations.SerializedName

data class Params(
    @SerializedName("Fajr")
    val fajr: Int,
    @SerializedName("Isha")
    val isha: Int
)