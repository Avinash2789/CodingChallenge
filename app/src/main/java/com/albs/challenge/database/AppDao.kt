package com.albs.challenge.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.albs.challenge.model.MeaningData

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meaningData: MeaningData) :Long

    @Query("Select * from MeaningData where sf = :sf")
    suspend fun fetch(sf: String): MeaningData?
}