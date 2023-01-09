package com.acun.quranicplus.data.remote

import com.acun.quranicplus.data.remote.response.juz.JuzResponse
import com.acun.quranicplus.data.remote.response.surah.SurahResponse
import com.acun.quranicplus.data.remote.response.surah_list.SurahListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApi {

    @GET("surah")
    suspend fun getAllSurah(): SurahListResponse

    @GET("surah/{number}")
    suspend fun getSurah(
        @Path("number") surahNumber: Int
    ): SurahResponse

    @GET("juz/{number}")
    suspend fun getJuz(
        @Path("number") juzNumber: Int
    ): JuzResponse
}