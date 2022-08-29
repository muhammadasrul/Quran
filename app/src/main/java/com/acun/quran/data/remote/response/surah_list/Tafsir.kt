package com.acun.quran.data.remote.response.surah_list


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tafsir(
    @SerializedName("id")
    val id: String
): Parcelable