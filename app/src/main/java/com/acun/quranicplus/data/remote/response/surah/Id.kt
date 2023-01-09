package com.acun.quranicplus.data.remote.response.surah


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Id(
    @SerializedName("long")
    val long: String,
    @SerializedName("short")
    val short: String
): Parcelable