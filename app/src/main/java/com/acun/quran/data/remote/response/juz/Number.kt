package com.acun.quran.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Number(
    @SerializedName("inQuran")
    val inQuran: Int,
    @SerializedName("inSurah")
    val inSurah: Int
)