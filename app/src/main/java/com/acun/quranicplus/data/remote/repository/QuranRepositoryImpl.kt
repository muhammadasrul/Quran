package com.acun.quranicplus.data.remote.repository

import android.app.Application
import com.acun.quranicplus.R
import com.acun.quranicplus.data.remote.PrayerApi
import com.acun.quranicplus.data.remote.QuranApi
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.juz.JuzDetail
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.juz_list.JuzListResponse
import com.acun.quranicplus.data.remote.response.prayer.PrayerTimeData
import com.acun.quranicplus.data.remote.response.surah.SurahDetail
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.repository.QuranRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val application: Application,
    private val quranApi: QuranApi,
    private val prayerApi: PrayerApi
): QuranRepository {
    override suspend fun getAllSurah(): Flow<Resource<List<Surah>>> = flow {
        emit(Resource.Loading())

        try {
            val surah = quranApi.getAllSurah()
            if (surah.code == 200) {
                emit(Resource.Success(data = surah.data, message = surah.message))
            } else {
                emit(Resource.Failed(message = surah.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        }
    }

    override suspend fun getSurah(number: Int): Flow<Resource<SurahDetail>> = flow {
        emit(Resource.Loading())

        try {
            val surah = quranApi.getSurah(number)
            if (surah.code == 200) {
                emit(Resource.Success(data = surah.data, message = surah.message))
            } else {
                emit(Resource.Failed(message = surah.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        }
    }

    override suspend fun getAllJuz(): Flow<Resource<List<Juz>>> = flow {
        emit(Resource.Loading())

        try {
            val json = InputStreamReader(application.applicationContext.resources.openRawResource(R.raw.juz))
            val juz = Gson().fromJson(json, JuzListResponse::class.java)
            emit(Resource.Success(data = juz.data, message = juz.message))
        } catch (e: IOException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        }
    }

    override suspend fun getJuz(number: Int): Flow<Resource<JuzDetail>> = flow {
        emit(Resource.Loading())

        try {
            val juz = quranApi.getJuz(number)
            if (juz.code == 200) {
                emit(Resource.Success(data = juz.data, message = juz.message))
            } else {
                emit(Resource.Failed(message = juz.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        }
    }

    override suspend fun getPrayer(
        latitude: Double,
        longitude: Double,
        methode: Int,
        month: Int,
        year: Int
    ): Flow<Resource<List<PrayerTimeData>>> = flow {
        emit(Resource.Loading())

        try {
            val prayer = prayerApi.getPrayerTime(
                latitude = latitude,
                longitude = longitude,
                methode = methode,
                month = month,
                year = year
            )
            if (prayer.code == 200) {
                emit(Resource.Success(data = prayer.data, message = "Success"))
            } else {
                emit(Resource.Failed(message = "Failed"))
            }
        } catch (e: HttpException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        } catch (e: IOException) {
            emit(Resource.Failed(message = e.localizedMessage ?: "An unexpected error occurred."))
        }
    }
}