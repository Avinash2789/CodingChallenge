package com.albs.challenge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.albs.challenge.model.MeaningData

@Database(entities = [MeaningData::class], version = 1, exportSchema = false)
@TypeConverters(value = [Converters::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}