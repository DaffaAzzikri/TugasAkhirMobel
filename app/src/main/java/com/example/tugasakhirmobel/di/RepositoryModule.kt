package com.example.tugasakhirmobel.di

import android.content.Context
import androidx.room.Room
import com.example.tugasakhirmobel.data.local.dao.BarangDao
import com.example.tugasakhirmobel.data.local.database.AppDatabase
import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitClient.instance

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideBarangApiService(retrofit: Retrofit): BarangApiService = retrofit.create(BarangApiService::class.java)

    @Provides
    @Singleton
    fun provideDashboardApiService(retrofit: Retrofit): DashboardApiService = retrofit.create(DashboardApiService::class.java)

    @Provides
    @Singleton
    fun provideRiwayatApiService(retrofit: Retrofit): RiwayatApiService = retrofit.create(RiwayatApiService::class.java)

    @Provides
    @Singleton
    fun provideProfilApiService(retrofit: Retrofit): ProfilApiService = retrofit.create(ProfilApiService::class.java)

    @Provides
    @Singleton
    fun provideLogApiService(retrofit: Retrofit): LogApiService = retrofit.create(LogApiService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "mobin_database").build()
    }

    @Provides
    @Singleton
    fun provideBarangDao(appDatabase: AppDatabase): BarangDao = appDatabase.barangDao()
}
