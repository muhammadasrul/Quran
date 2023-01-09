package com.acun.quranicplus.data.remote.response.surah


import com.google.gson.annotations.SerializedName

data class SurahResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: SurahDetail,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)