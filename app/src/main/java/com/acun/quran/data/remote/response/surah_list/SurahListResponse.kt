package com.acun.quran.data.remote.response.surah_list


import com.google.gson.annotations.SerializedName

data class SurahListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Surah>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)