package com.acun.quranicplus.data.remote

import com.acun.quranicplus.data.remote.response.prayer.PrayerTimeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PrayerApi {

    @GET("calendar")
    suspend fun getPrayerTime(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") methode: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): PrayerTimeResponse
}