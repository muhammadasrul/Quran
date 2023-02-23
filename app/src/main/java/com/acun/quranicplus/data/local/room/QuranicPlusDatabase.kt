package com.acun.quranicplus.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PrayerEntity::class], version = 1)
abstract class QuranicPlusDatabase(): RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
}