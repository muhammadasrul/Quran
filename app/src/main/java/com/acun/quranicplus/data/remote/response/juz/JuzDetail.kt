package com.acun.quranicplus.data.remote.response.juz

import com.acun.quranicplus.data.local.room.entity.JuzEntity
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.google.gson.annotations.SerializedName

data class JuzDetail(
    @SerializedName("juz")
    val juz: Int,
    @SerializedName("juzEndInfo")
    val juzEndInfo: String,
    @SerializedName("juzEndSurahNumber")
    val juzEndSurahNumber: Int,
    @SerializedName("juzStartInfo")
    val juzStartInfo: String,
    @SerializedName("juzStartSurahNumber")
    val juzStartSurahNumber: Int,
    @SerializedName("totalVerses")
    val totalVerses: Int,
    @SerializedName("verses")
    val verses: List<Verse>
) {
    fun mapToJuzEntity() = JuzEntity(
        juz = juz,
        juzEndSurahNumber = juzEndSurahNumber,
        juzEndInfo = juzEndInfo,
        juzStartInfo = juzStartInfo,
        juzStartSurahNumber = juzStartSurahNumber,
        totalVerses = totalVerses
    )
}