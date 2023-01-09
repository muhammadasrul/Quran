package com.acun.quranicplus.data.remote.response.juz_list


import android.os.Parcelable
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JuzSurah(
    @SerializedName("end")
    val end: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_arab")
    val name_arab: String,
    @SerializedName("no")
    val no: Int,
    @SerializedName("start")
    val start: Int,
    var verses: List<Verse>? = null
): Parcelable