package com.acun.quran.data.remote.response.surah


import com.google.gson.annotations.SerializedName

data class Revelation(
    @SerializedName("arab")
    val arab: String,
    @SerializedName("en")
    val en: String,
    @SerializedName("id")
    val id: String
)