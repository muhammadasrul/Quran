package com.acun.quranicplus.data.remote.response.surah_list


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transliteration(
    @SerializedName("en")
    val en: String,
    @SerializedName("id")
    val id: String
): Parcelable