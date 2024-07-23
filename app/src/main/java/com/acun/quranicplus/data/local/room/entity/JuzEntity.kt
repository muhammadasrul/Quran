package com.acun.quranicplus.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acun.quranicplus.data.remote.response.juz.JuzDetail

@Entity
data class JuzEntity(
    @PrimaryKey val juz: Int,
    @ColumnInfo val juzEndSurahNumber: Int,
    @ColumnInfo val juzEndInfo: String,
    @ColumnInfo val juzStartInfo: String,
    @ColumnInfo val juzStartSurahNumber: Int,
    @ColumnInfo val totalVerses: Int
) {
    fun mapToJuzDetail() = JuzDetail(
        juz = juz,
        juzEndInfo = juzEndInfo,
        juzEndSurahNumber = juzEndSurahNumber,
        juzStartInfo = juzStartInfo,
        juzStartSurahNumber = juzStartSurahNumber,
        totalVerses = totalVerses,
        verses = emptyList()
    )
}
