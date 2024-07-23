package com.acun.quranicplus.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acun.quranicplus.data.remote.response.surah_list.Name
import com.acun.quranicplus.data.remote.response.surah_list.Revelation
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.data.remote.response.surah_list.Tafsir
import com.acun.quranicplus.data.remote.response.surah_list.Translation
import com.acun.quranicplus.data.remote.response.surah_list.Transliteration

@Entity
data class SurahEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo val name: String,
    @ColumnInfo val number: Int,
    @ColumnInfo val translationEn: String,
    @ColumnInfo val translationId: String,
    @ColumnInfo val transliterationEn: String,
    @ColumnInfo val transliterationId: String,
    @ColumnInfo val numberOfVerses: Int,
) {
    fun mapToSurahResponse() = Surah(
        name = Name(
            long = name,
            short = name,
            translation = Translation(translationEn, translationId),
            transliteration = Transliteration(transliterationEn, transliterationId)
        ),
        number = number,
        numberOfVerses = numberOfVerses,
        revelation = Revelation(arab = translationEn, en = translationEn, id = translationId),
        sequence = -1,
        tafsir = Tafsir(id = translationId)
    )
}
