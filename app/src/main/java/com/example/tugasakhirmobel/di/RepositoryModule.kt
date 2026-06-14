package com.example.tugasakhirmobel.di

import android.content.Context
import androidx.room.Room
import com.example.tugasakhirmobel.data.local.dao.BarangDao
import com.example.tugasakhirmobel.data.local.database.AppDatabase
import com.example.tugasakhirmobel.data.repository.AuthRepository
import com.example.tugasakhirmobel.data.repository.BarangRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}
