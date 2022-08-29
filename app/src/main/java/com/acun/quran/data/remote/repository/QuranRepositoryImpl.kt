package com.acun.quran.data.remote.repository

import android.content.Context
import com.acun.quran.R
import com.acun.quran.data.remote.QuranApi
import com.acun.quran.data.remote.response.Resource
import com.acun.quran.data.remote.response.juz.JuzDetail
import com.acun.quran.data.remote.response.juz_list.Juz
import com.acun.quran.data.remote.response.juz_list.JuzListResponse
import com.acun.quran.data.remote.response.surah.SurahDetail
import com.acun.quran.data.remote.response.surah_list.Surah
import com.acun.quran.repository.QuranRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(private val quranApi: QuranApi): QuranRepository {
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

    override suspend fun getAllJuz(ctx: Context): Flow<Resource<List<Juz>>> = flow {
        emit(Resource.Loading())

        try {
            val json = InputStreamReader(ctx.resources.openRawResource(R.raw.juz))
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
}