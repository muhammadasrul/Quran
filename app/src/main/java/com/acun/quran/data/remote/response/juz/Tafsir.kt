package com.acun.quran.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Tafsir(
    @SerializedName("id")
    val id: Id
)