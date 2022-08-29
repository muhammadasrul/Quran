package com.acun.quran.data.remote.response.juz_list


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Juz(
    @SerializedName("juz")
    val juz: Int,
    @SerializedName("surah")
    val surah: List<JuzSurah>,
    @SerializedName("totalVerses")
    val totalVerses: Int
): Parcelable