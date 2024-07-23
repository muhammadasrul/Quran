package com.acun.quranicplus.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.BuildConfig.PRAYER_BASE_URL
import com.acun.quranicplus.BuildConfig.QURAN_BASE_URL
import com.acun.quranicplus.data.local.datastore.QuranDataStore
import com.acun.quranicplus.data.local.room.QuranicPlusDatabase
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
            .baseUrl(QURAN_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuranApi::class.java)
    }

    @Provides
    @Singleton
    fun providePrayerApiService(client: OkHttpClient): PrayerApi {
        return Retrofit.Builder()
            .baseUrl(PRAYER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PrayerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): QuranicPlusDatabase {
        return Room.databaseBuilder(
            app,
            QuranicPlusDatabase::class.java,
            "quranic_plus"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(
        app: Application,
        quranApi: QuranApi,
        prayerApi: PrayerApi,
        db: QuranicPlusDatabase
    ): QuranRepository {
        return QuranRepositoryImpl(app, quranApi, prayerApi, db)
    }

    @Provides
    @Singleton
    fun provideDataStore(app: Application): QuranDataStore {
        return QuranDataStore(app)
    }
}