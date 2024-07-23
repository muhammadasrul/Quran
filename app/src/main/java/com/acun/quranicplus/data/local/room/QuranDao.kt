package com.acun.quranicplus.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.acun.quranicplus.data.local.room.entity.JuzEntity
import com.acun.quranicplus.data.local.room.entity.PrayerEntity
import com.acun.quranicplus.data.local.room.entity.SurahEntity
import com.acun.quranicplus.data.local.room.entity.VerseEntity

@Dao
interface QuranDao {

    @Query("SELECT * FROM SURAHENTITY")
    suspend fun getAllSurah() : List<SurahEntity>

    @Insert(SurahEntity::class, OnConflictStrategy.REPLACE)
    suspend fun insertAllSurah(surahList: List<SurahEntity>)

    @Query("SELECT * FROM VERSEENTITY WHERE number = :number")
    suspend fun getVerseBySurahNumber(number: Int) : List<VerseEntity>

    @Query("SELECT * FROM VERSEENTITY WHERE juz = :number")
    suspend fun getVerseByJuzNumber(number: Int) : List<VerseEntity>

    @Insert(VerseEntity::class, OnConflictStrategy.REPLACE)
    suspend fun insertAllVerse(surahList: List<VerseEntity>)

    @Query("SELECT * FROM JUZENTITY WHERE juz = :number")
    suspend fun getJuzDetailByNumber(number: Int): JuzEntity?

    @Insert(JuzEntity::class, OnConflictStrategy.REPLACE)
    suspend fun insertJuzDetail(juzEntity: JuzEntity)
}