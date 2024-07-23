package com.acun.quranicplus.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.acun.quranicplus.data.local.room.entity.PrayerEntity

@Dao
interface PrayerDao {

    @Query("SELECT * FROM PRAYERENTITY")
    suspend fun getAllPrayer() : List<PrayerEntity>

    @Insert(PrayerEntity::class, OnConflictStrategy.REPLACE)
    suspend fun insertAllPrayer(prayerList: List<PrayerEntity>)

    @Update(PrayerEntity::class, OnConflictStrategy.REPLACE)
    suspend fun updatePrayer(prayer: PrayerEntity)
}