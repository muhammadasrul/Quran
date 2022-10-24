package com.acun.quran.data.remote.response.surah


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Verse(
    @SerializedName("audio")
    val audio: Audio,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("number")
    val number: Number,
    @SerializedName("tafsir")
    val tafsir: TafsirX,
    @SerializedName("text")
    val text: Text,
    @SerializedName("translation")
    val translation: Translation,
    val surahName: String = "",
    val headerName: String = ""
): Parcelable