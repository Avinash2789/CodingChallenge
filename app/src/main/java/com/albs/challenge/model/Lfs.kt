package com.albs.challenge.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Lfs(

    @SerializedName("lf") var lf: String,
    @SerializedName("freq") var freq: Int,
    @SerializedName("since") var since: Int,
    @SerializedName("vars") var vars: ArrayList<Vars> = arrayListOf()

)