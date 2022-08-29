package com.acun.quran.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("long")
    val long: String,
    @SerializedName("short")
    val short: String
)