package com.acun.quranicplus.repository

import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.juz.JuzDetail
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.data.remote.response.surah.SurahDetail
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    fun getAllSurah(): Flow<Resource<List<Surah>>>
    fun getSurah(number: Int): Flow<Resource<SurahDetail>>
    fun getAllJuz(): Flow<Resource<List<Juz>>>
    fun getJuz(number: Int): Flow<Resource<JuzDetail>>

    fun getPrayer(
        latitude: Double,
        longitude: Double,
        methode: Int,
        month: Int,
        year: Int
    ): Flow<Resource<List<Prayer>>>

    suspend fun updateLocalPrayer(prayer: Prayer)
}