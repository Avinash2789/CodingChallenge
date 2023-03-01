package com.albs.challenge.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "meaningsResponse")
data class MeaningsResponse(

    @SerializedName("sf") var sf: String = "",
    @SerializedName("lfs") var lfs: ArrayList<Lfs> = arrayListOf()

)