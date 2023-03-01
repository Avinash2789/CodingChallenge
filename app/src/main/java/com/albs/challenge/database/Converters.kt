package com.albs.challenge.database

import androidx.room.TypeConverter
import com.albs.challenge.model.MeaningsResponse
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun toMeaningsResponse(name: String): MeaningsResponse {
        return Gson().fromJson(name, MeaningsResponse::class.java)
    }

    @TypeConverter
    fun fromMeaningsResponse(name: MeaningsResponse): String {
        return Gson().toJson(name)
    }
}