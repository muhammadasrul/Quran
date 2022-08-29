package com.acun.quran.data.remote.response.surah


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Translation(
    @SerializedName("en")
    val en: String,
    @SerializedName("id")
    val id: String
): Parcelable