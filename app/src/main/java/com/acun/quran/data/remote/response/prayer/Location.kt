package com.acun.muslimplus.data.remote.response.prayer_time


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)