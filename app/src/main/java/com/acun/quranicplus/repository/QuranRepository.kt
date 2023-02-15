package com.acun.quranicplus.repository

import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.juz.JuzDetail
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.prayer.PrayerTimeData
import com.acun.quranicplus.data.remote.response.surah.SurahDetail
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    suspend fun getAllSurah(): Flow<Resource<List<Surah>>>
    suspend fun getSurah(number: Int): Flow<Resource<SurahDetail>>
    suspend fun getAllJuz(): Flow<Resource<List<Juz>>>
    suspend fun getJuz(number: Int): Flow<Resource<JuzDetail>>

    suspend fun getPrayer(
        latitude: Double,
        longitude: Double,
        methode: Int,
        month: Int,
        year: Int
    ): Flow<Resource<List<PrayerTimeData>>>
}