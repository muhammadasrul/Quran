package com.acun.quranicplus.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acun.quranicplus.data.remote.response.surah.Audio
import com.acun.quranicplus.data.remote.response.surah.Id
import com.acun.quranicplus.data.remote.response.surah.Meta
import com.acun.quranicplus.data.remote.response.surah.Number
import com.acun.quranicplus.data.remote.response.surah.Sajda
import com.acun.quranicplus.data.remote.response.surah.TafsirX
import com.acun.quranicplus.data.remote.response.surah.Text
import com.acun.quranicplus.data.remote.response.surah.TransliterationX
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah.Translation

@Entity
data class VerseEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo val name: String,
    @ColumnInfo val textArab: String,
    @ColumnInfo val translationEn: String,
    @ColumnInfo val transliterationEn: String,
    @ColumnInfo val number: Int? = null,
    @ColumnInfo val numberInSurah: Int,
    @ColumnInfo val numberInQuran: Int,
    @ColumnInfo val audio: String,
    @ColumnInfo val juz: Int
) {
    fun mapToVerseResponse() = Verse(
        audio = Audio(audio, listOf(audio)),
        meta = Meta(
            hizbQuarter = -1,
            juz = juz,
            manzil = -1,
            page = -1,
            ruku = -1,
            sajda = Sajda(obligatory = false, recommended = false)
        ),
        number = Number(numberInQuran, numberInSurah),
        tafsir = TafsirX(Id(translationEn, translationEn)),
        text = Text(textArab, TransliterationX(transliterationEn)),
        translation = Translation(translationEn, translationEn)
    )
}
