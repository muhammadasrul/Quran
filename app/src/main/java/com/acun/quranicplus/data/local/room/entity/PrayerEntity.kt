package com.acun.quranicplus.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PrayerEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo var isNotificationOn: Boolean = false
)
