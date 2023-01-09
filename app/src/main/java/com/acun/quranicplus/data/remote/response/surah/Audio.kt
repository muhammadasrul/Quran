package com.acun.quranicplus.data.remote.response.surah


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Audio(
    @SerializedName("primary")
    val primary: String,
    @SerializedName("secondary")
    val secondary: List<String>
): Parcelable