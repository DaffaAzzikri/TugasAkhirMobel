package com.example.tugasakhirmobel.di

import android.content.Context
import androidx.room.Room
import com.example.tugasakhirmobel.data.local.dao.BarangDao
import com.example.tugasakhirmobel.data.local.database.AppDatabase
import com.example.tugasakhirmobel.data.remote.api.LogApiService
import com.example.tugasakhirmobel.data.repository.AuthRepository
import com.example.tugasakhirmobel.data.repository.BarangRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepository()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mobin_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBarangDao(appDatabase: AppDatabase): BarangDao {
        return appDatabase.barangDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            // TODO: Ganti dengan URL backend Anda (misal: "http://10.0.2.2:8000/" untuk emulator Android ke localhost)
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLogApiService(retrofit: Retrofit): LogApiService {
        return retrofit.create(LogApiService::class.java)
    }
}
