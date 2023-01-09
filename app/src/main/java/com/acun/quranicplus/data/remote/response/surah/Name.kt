package com.acun.quranicplus.data.remote.response.surah


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("long")
    val long: String,
    @SerializedName("short")
    val short: String,
    @SerializedName("translation")
    val translation: Translation,
    @SerializedName("transliteration")
    val transliteration: Transliteration
)