package com.albs.challenge.di

import android.content.Context
import androidx.room.Room
import com.albs.challenge.R
import com.albs.challenge.database.AppDao
import com.albs.challenge.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            appContext.getString(R.string.meaning_data)
        ).build()
    }

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.appDao()
    }


}