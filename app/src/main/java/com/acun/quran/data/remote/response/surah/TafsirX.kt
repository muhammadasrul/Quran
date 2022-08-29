package com.acun.quran.data.remote.response.surah


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TafsirX(
    @SerializedName("id")
    val id: Id
): Parcelable