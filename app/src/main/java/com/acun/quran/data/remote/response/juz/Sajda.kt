package com.acun.quran.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Sajda(
    @SerializedName("obligatory")
    val obligatory: Boolean,
    @SerializedName("recommended")
    val recommended: Boolean
)