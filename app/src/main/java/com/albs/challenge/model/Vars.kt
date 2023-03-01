package com.albs.challenge.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Vars(

    @SerializedName("lf") var lf: String,
    @SerializedName("freq") var freq: Int,
    @SerializedName("since") var since: Int

)