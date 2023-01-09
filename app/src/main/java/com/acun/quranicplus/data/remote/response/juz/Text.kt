package com.acun.quranicplus.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Text(
    @SerializedName("arab")
    val arab: String,
    @SerializedName("transliteration")
    val transliteration: Transliteration
)