package com.acun.quran.data.remote.response.surah_list


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Name(
    @SerializedName("long")
    val long: String,
    @SerializedName("short")
    val short: String,
    @SerializedName("translation")
    val translation: Translation,
    @SerializedName("transliteration")
    val transliteration: Transliteration
): Parcelable