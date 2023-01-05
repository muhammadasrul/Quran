package com.acun.quran.data.local.datastore

data class LastReadVerse(
    val surah: String,
    val numberInSurah: Int,
    val numberInQuran: Int
)
