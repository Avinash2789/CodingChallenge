package com.albs.challenge.network

import com.albs.challenge.model.MeaningsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("dictionary.py")
    suspend fun getMeanings(@Query("sf") sf: String): Response<ArrayList<MeaningsResponse>>

}