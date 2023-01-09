package com.acun.quranicplus.data.remote.response.surah


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Text(
    @SerializedName("arab")
    val arab: String,
    @SerializedName("transliteration")
    val transliteration: TransliterationX
): Parcelable