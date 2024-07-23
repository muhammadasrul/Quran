package com.acun.quranicplus.data.remote.repository

import android.app.Application
import android.util.Log
import com.acun.quranicplus.R
import com.acun.quranicplus.data.local.room.QuranicPlusDatabase
import com.acun.quranicplus.data.local.room.entity.PrayerEntity
import com.acun.quranicplus.data.remote.PrayerApi
import com.acun.quranicplus.data.remote.QuranApi
import com.acun.quranicplus.data.remote.response.Resource
import com.acun.quranicplus.data.remote.response.Resource.Failed
import com.acun.quranicplus.data.remote.response.Resource.Loading
import com.acun.quranicplus.data.remote.response.Resource.Success
import com.acun.quranicplus.data.remote.response.juz.JuzDetail
import com.acun.quranicplus.data.remote.response.juz_list.Juz
import com.acun.quranicplus.data.remote.response.juz_list.JuzListResponse
import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.data.remote.response.prayer.toPrayerList
import com.acun.quranicplus.data.remote.response.surah.Name
import com.acun.quranicplus.data.remote.response.surah.Revelation
import com.acun.quranicplus.data.remote.response.surah.SurahDetail
import com.acun.quranicplus.data.remote.response.surah.Tafsir
import com.acun.quranicplus.data.remote.response.surah.Translation
import com.acun.quranicplus.data.remote.response.surah.Transliteration
import com.acun.quranicplus.data.remote.response.surah.Verse
import com.acun.quranicplus.data.remote.response.surah_list.Surah
import com.acun.quranicplus.repository.QuranRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import java.util.Calendar
import javax.inject.Inject

class QuranRepositoryImpl @Inject constructor(
    private val application: Application,
    private val quranApi: QuranApi,
    private val prayerApi: PrayerApi,
    private val db: QuranicPlusDatabase
) : QuranRepository {

    override fun getAllSurah(): Flow<Resource<List<Surah>>> = flow {
        emit(Loading())

        val surahDb = db.quranDao().getAllSurah()

        if (surahDb.isNotEmpty()) {
            emit(Success(data = surahDb.map { it.mapToSurahResponse() }, message = ""))
        } else {
            try {
                val surah = quranApi.getAllSurah()
                if (surah.code == 200) {
                    emit(Success(data = surah.data, message = surah.message))
                    db.quranDao().insertAllSurah(surah.data.map { it.mapToEntity() })
                } else {
                    emit(Failed(message = surah.message))
                }
            } catch (e: HttpException) {
                emit(Failed(message = e.localizedMessage ?: unexpectedError))
            } catch (e: IOException) {
                emit(Failed(message = e.localizedMessage ?: unexpectedError))
            }
        }
    }

    override fun getSurah(number: Int): Flow<Resource<SurahDetail>> = flow {
        emit(Loading())

        val surahDb = db.quranDao().getVerseBySurahNumber(number)

        if (surahDb.isNotEmpty()) {
            surahDb.firstOrNull()?.let {
                emit(Success(data = SurahDetail(
                    name = Name(
                        long = it.name,
                        short = it.name,
                        translation = Translation(en = it.translationEn, id = it.translationEn),
                        transliteration = Transliteration(en = it.transliterationEn, id = it.transliterationEn)
                    ),
                    number = it.numberInQuran,
                    numberOfVerses = it.numberInQuran,
                    preBismillah = "",
                    revelation = Revelation(arab = it.textArab, en = it.translationEn, id = it.translationEn),
                    sequence = -1,
                    tafsir = Tafsir(id = it.translationEn),
                    verses = surahDb.map { verseEntity -> verseEntity.mapToVerseResponse() }
                ), message = ""))

            }
        } else {
            try {
                val surah = quranApi.getSurah(number)
                if (surah.code == 200) {
                    db.quranDao().insertAllVerse(surah.data.verses.map { it.mapToVerseEntity().copy(number = number) })
                    emit(Success(data = surah.data, message = surah.message))
                } else {
                    emit(Failed(message = surah.message))
                }
            } catch (e: HttpException) {
                emit(Failed(message = e.localizedMessage ?: unexpectedError))
            } catch (e: IOException) {
                emit(Failed(message = e.localizedMessage ?: unexpectedError))
            }
        }

    }

    override fun getAllJuz(): Flow<Resource<List<Juz>>> = flow {
        emit(Loading())

        try {
            val json =
                InputStreamReader(application.applicationContext.resources.openRawResource(R.raw.juz))
            val juz = Gson().fromJson(json, JuzListResponse::class.java)
            emit(Success(data = juz.data, message = juz.message))
        } catch (e: IOException) {
            emit(Failed(message = e.localizedMessage ?: unexpectedError))
        }
    }

    override fun getJuz(number: Int): Flow<Resource<JuzDetail>> = flow {
        emit(Loading())

        val juzDetailDb = db.quranDao().getJuzDetailByNumber(number)
        val juzVerse = db.quranDao().getVerseByJuzNumber(number)

        if (juzDetailDb?.juz == number) {
            emit(Success(data = juzDetailDb.mapToJuzDetail().copy(verses = juzVerse.map { it.mapToVerseResponse() }), message = ""))
        } else {
            try {
                val juz = quranApi.getJuz(number)
                if (juz.code == 200) {
                    db.quranDao().insertJuzDetail(juz.data.mapToJuzEntity())
                    db.quranDao().insertAllVerse(juz.data.verses.map { j -> j.mapToVerseEntity() })
                    emit(Success(data = juz.data, message = juz.message))
                } else {
                    emit(Failed(message = juz.message))
                }
            } catch (e: HttpException) {
                emit(Failed(message = e.localizedMessage ?: unexpectedError))
            } catch (e: IOException) {
                emit(Failed(message = e.localizedMessage ?: unexpectedError))
            }
        }
    }

    override fun getPrayer(
        latitude: Double,
        longitude: Double,
        methode: Int,
        month: Int,
        year: Int
    ): Flow<Resource<List<Prayer>>> = flow {
        emit(Loading())

        val prayerReminder = db.prayerDao().getAllPrayer()

        try {
            val prayer = prayerApi.getPrayerTime(
                latitude = latitude,
                longitude = longitude,
                methode = methode,
                month = month,
                year = year
            )

            if (prayer.code == 200) {
                val date = Calendar.getInstance().get(Calendar.DATE)
                try {
                    var prayerList = prayer.data[date - 1].timings.toPrayerList()

                    if (prayerReminder.isEmpty()) db.prayerDao().insertAllPrayer(prayerList.map {
                        PrayerEntity(it.id, it.isNotificationOn)
                    }) else {
                        prayerList = prayerList.mapIndexed { i, p ->
                            Prayer(
                                id = p.id,
                                name = p.name,
                                time = p.time,
                                isNowPrayer = p.isNowPrayer,
                                isNearestPrayer = p.isNearestPrayer,
                                isNotificationOn = prayerReminder[i].isNotificationOn
                            )
                        }
                    }

                    emit(Success(data = prayerList, message = "Success"))
                } catch (e: IndexOutOfBoundsException) {
                    emit(Failed(message = e.localizedMessage ?: unexpectedError))
                } catch (e: Exception) {
                    emit(Failed(message = e.localizedMessage ?: unexpectedError))
                }
            } else {
                emit(Failed(message = prayer.status))
            }
        } catch (e: HttpException) {
            emit(Failed(message = e.localizedMessage ?: unexpectedError))
        } catch (e: IOException) {
            emit(Failed(message = e.localizedMessage ?: unexpectedError))
        }
    }

    override suspend fun updateLocalPrayer(prayer: Prayer) {
        db.prayerDao().updatePrayer(PrayerEntity(prayer.id, prayer.isNotificationOn))
    }

    companion object {
        private const val unexpectedError = "An unexpected error occurred."
    }
}