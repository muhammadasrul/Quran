package com.acun.quranicplus.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.acun.quranicplus.data.local.room.entity.JuzEntity
import com.acun.quranicplus.data.local.room.entity.PrayerEntity
import com.acun.quranicplus.data.local.room.entity.SurahEntity
import com.acun.quranicplus.data.local.room.entity.VerseEntity

@Database(entities = [PrayerEntity::class, SurahEntity::class, VerseEntity::class, JuzEntity::class], version = 2, exportSchema = false)
abstract class QuranicPlusDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
    abstract fun quranDao(): QuranDao
}