package com.acun.quranicplus.data.remote.response.surah


import com.google.gson.annotations.SerializedName

data class Transliteration(
    @SerializedName("en")
    val en: String,
    @SerializedName("id")
    val id: String
)