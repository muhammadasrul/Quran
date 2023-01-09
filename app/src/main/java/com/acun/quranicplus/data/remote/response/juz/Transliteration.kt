package com.acun.quranicplus.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Transliteration(
    @SerializedName("en")
    val en: String
)