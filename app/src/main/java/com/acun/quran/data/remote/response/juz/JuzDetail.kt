package com.acun.quran.data.remote.response.juz

import com.acun.quran.data.remote.response.juz_list.Juz
import com.acun.quran.data.remote.response.juz_list.JuzSurah
import com.acun.quran.data.remote.response.surah.*
import com.acun.quran.data.remote.response.surah.Audio
import com.acun.quran.data.remote.response.surah.Id
import com.acun.quran.data.remote.response.surah.Meta
import com.acun.quran.data.remote.response.surah.Number
import com.acun.quran.data.remote.response.surah.Sajda
import com.acun.quran.data.remote.response.surah.Text
import com.acun.quran.data.remote.response.surah.Translation
import com.acun.quran.data.remote.response.surah.Verse
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
)

//fun JuzDetail.verseToSurahVerse(): List<com.acun.quran.data.remote.response.surah.Verse> {
//    return verses.map {
//        Verse(
//            audio = Audio(
//                primary = it.audio.primary,
//                secondary = it.audio.secondary
//            ),
//            meta = Meta(
//                hizbQuarter = it.meta.hizbQuarter,
//                juz = it.meta.juz,
//                manzil = it.meta.manzil,
//                page = it.meta.page,
//                ruku = it.meta.ruku,
//                sajda = Sajda(
//                    obligatory = it.meta.sajda.obligatory,
//                    recommended = it.meta.sajda.recommended
//                )
//            ),
//            number = Number(
//                inQuran = it.number.inQuran,
//                inSurah = it.number.inSurah
//            ),
//            tafsir = TafsirX(
//                id = Id(
//                    long = it.tafsir.id.long,
//                    short = it.tafsir.id.short
//                )
//            ),
//            text = Text(
//                arab = it.text.arab,
//                transliteration = TransliterationX(en = it.text.transliteration.en)
//            ),
//            translation = Translation(en = it.translation.en, id = it.translation.id)
//        )
//    }
//}