package com.acun.quranicplus.di

import android.app.Application
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.remote.PrayerApi
import com.acun.quranicplus.data.remote.QuranApi
import com.acun.quranicplus.data.remote.repository.QuranRepositoryImpl
import com.acun.quranicplus.repository.QuranRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object QuranModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideQuranApiService(client: OkHttpClient): QuranApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.QURAN_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuranApi::class.java)
    }

    @Provides
    @Singleton
    fun providePrayerApiService(client: OkHttpClient): PrayerApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PRAYER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PrayerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(app: Application ,quranApi: QuranApi, prayerApi: PrayerApi): QuranRepository {
        return QuranRepositoryImpl(app, quranApi, prayerApi)
    }

    @Provides
    @Singleton
    fun provideDataStore(app: Application): QuranDataStore {
        return QuranDataStore(app)
    }
}