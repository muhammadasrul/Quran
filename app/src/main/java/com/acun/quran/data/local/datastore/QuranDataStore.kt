package com.acun.quran.data.local.datastore

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
        LastReadVerse(it[AYAH] ?: 0, surah = it[SURAH] ?: "")
    }

    suspend fun setLastRead(lastRead: LastReadVerse) {
        context.lastReadDataStore.edit {
            it[AYAH] = lastRead.ayah
            it[SURAH] = lastRead.surah
        }
    }

    val versePreference: Flow<VersePreference> = context.preferenceDataStore.data.map {
        VersePreference(
            it[TRANSLITERATION] ?: true,
            it[TRANSLATION] ?: true,
            it[TEXT_SIZE] ?: 1
        )
    }

    suspend fun setVersePreference(versePreference: VersePreference) {
        context.preferenceDataStore.edit {
            it[TRANSLITERATION] = versePreference.transliteration
            it[TRANSLATION] = versePreference.translation
            it[TEXT_SIZE] = versePreference.textSizePos
        }
    }

    companion object {
        val AYAH = intPreferencesKey("ayah")
        val SURAH = stringPreferencesKey("surah")

        val TRANSLITERATION = booleanPreferencesKey("transliteration")
        val TRANSLATION = booleanPreferencesKey("translation")
        val TEXT_SIZE = intPreferencesKey("text_size")
    }
}