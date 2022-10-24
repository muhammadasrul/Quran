package com.acun.quran.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Verse(
    @SerializedName("audio")
    val audio: Audio,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("number")
    val number: Number,
    @SerializedName("tafsir")
    val tafsir: Tafsir,
    @SerializedName("text")
    val text: Text,
    @SerializedName("translation")
    val translation: Translation
)

//fun Verse.toSurahVerse(): com.acun.quran.data.remote.response.surah.Verse {
//    return com.acun.quran.data.remote.response.surah.Verse(
//        audio = com.acun.quran.data.remote.response.surah.Audio(
//            primary = audio.primary,
//            secondary = audio.secondary
//        ),
//        meta = com.acun.quran.data.remote.response.surah.Meta(
//            hizbQuarter = meta.hizbQuarter,
//            juz = meta.juz,
//            manzil = meta.manzil,
//            page = meta.page,
//            ruku = meta.ruku,
//            sajda = Sajda(
//                obligatory = meta.sajda.obligatory,
//                recommended = meta.sajda.recommended
//            )
//        ),
//        number = com.acun.quran.data.remote.response.surah.Number(
//            inQuran = number.inQuran,
//            inSurah = number.inSurah
//        ),
//        tafsir = TafsirX(
//            id = Id(
//                long = tafsir.id.long,
//                short = tafsir.id.short
//            )
//        ),
//        text = com.acun.quran.data.remote.response.surah.Text(
//            arab = text.arab,
//            transliteration = TransliterationX(en = text.transliteration.en)
//        ),
//        translation = com.acun.quran.data.remote.response.surah.Translation(
//            en = translation.en,
//            id = translation.id
//        )
//    )
//}