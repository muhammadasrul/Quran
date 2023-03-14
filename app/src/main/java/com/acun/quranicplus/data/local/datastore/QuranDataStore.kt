package com.acun.quranicplus.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.lastReadDataStore by preferencesDataStore(name = "last_read")
val Context.preferenceDataStore by preferencesDataStore("preference")
class QuranDataStore @Inject constructor(private val context: Context) {

    val lastRead: Flow<LastReadVerse> = context.lastReadDataStore.data.map {
        LastReadVerse(
            surah = it[SURAH] ?: "",
            numberInSurah = it[NUMBER_IN_SURAH] ?: 0,
            numberInQuran = it[NUMBER_IN_QURAN] ?: 0,
            number = it[NUMBER] ?: 0
        )
    }

    suspend fun setLastRead(lastRead: LastReadVerse) {
        context.lastReadDataStore.edit {
            it[SURAH] = lastRead.surah
            it[NUMBER_IN_SURAH] = lastRead.numberInSurah
            it[NUMBER_IN_QURAN] = lastRead.numberInQuran
            it[NUMBER] = lastRead.number
        }
    }

    val versePreference: Flow<VersePreference> = context.preferenceDataStore.data.map {
        VersePreference(
            it[TRANSLITERATION] ?: true,
            it[TRANSLATION] ?: true,
            it[TEXT_SIZE] ?: 1,
            it[LANGUAGE] ?: 0
        )
    }

    suspend fun setVersePreference(versePreference: VersePreference) {
        context.preferenceDataStore.edit {
            it[TRANSLITERATION] = versePreference.transliteration
            it[TRANSLATION] = versePreference.translation
            it[TEXT_SIZE] = versePreference.textSizePos
            it[LANGUAGE] = versePreference.languagePos
        }
    }

    companion object {
        val SURAH = stringPreferencesKey("surah")
        val NUMBER_IN_SURAH = intPreferencesKey("number_in_surah")
        val NUMBER_IN_QURAN = intPreferencesKey("number_in_quran")
        val NUMBER = intPreferencesKey("number")

        val TRANSLITERATION = booleanPreferencesKey("transliteration")
        val TRANSLATION = booleanPreferencesKey("translation")
        val TEXT_SIZE = intPreferencesKey("text_size")
        val LANGUAGE = intPreferencesKey("language")
    }
}