package com.albs.challenge.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "MeaningData")
data class MeaningData(

    @PrimaryKey @SerializedName("sf") var sf: String,
    @SerializedName("meaningsResponse") var meaningsResponse: MeaningsResponse

)
