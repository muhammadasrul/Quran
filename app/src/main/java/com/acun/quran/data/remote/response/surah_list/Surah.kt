package com.acun.quran.data.remote.response.surah_list


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Surah(
    @SerializedName("name")
    val name: Name,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfVerses")
    val numberOfVerses: Int,
    @SerializedName("revelation")
    val revelation: Revelation,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("tafsir")
    val tafsir: Tafsir
): Parcelable