package com.acun.quran.data.remote.response.surah


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransliterationX(
    @SerializedName("en")
    val en: String
): Parcelable