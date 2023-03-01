package com.albs.challenge.repository

import android.app.Application
import com.albs.challenge.R
import com.albs.challenge.database.AppDao
import com.albs.challenge.model.MeaningData
import com.albs.challenge.model.MeaningsResponse
import com.albs.challenge.network.ApiService
import com.albs.challenge.utils.Resource
import com.albs.challenge.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private var apiService: ApiService,
    private var dao: AppDao,
    private val application: Application
) {

    private suspend fun getMeaningsFromServer(searchText: String): Boolean {
        val response = apiService.getMeanings(searchText)
        if (!response.body().isNullOrEmpty()) {
            val meaningData = MeaningData(searchText, response.body()!![0])
            insertResponse(meaningData)
            return true
        }
        return false
    }

    suspend fun getAllMeanings(sf: String): Resource<MeaningsResponse> {
        if (dao.fetch(sf) == null) {
            if (Utils.checkForInternet(application)) {
                if (!getMeaningsFromServer(sf)) {
                    return Resource.Error(application.getString(R.string.no_data_found), null)
                }
            } else {
                return Resource.Error(
                    application.getString(R.string.not_connected_to_internet),
                    null
                )
            }
        }
        return handleResponse(dao.fetch(sf)!!.meaningsResponse)
    }

    private suspend fun insertResponse(meaningData: MeaningData): Long {
        return dao.insert(meaningData)
    }

    fun handleResponse(response: MeaningsResponse?): Resource<MeaningsResponse> {
        if (response != null) {
            return Resource.Success(response)
        }
        return Resource.Error(application.getString(R.string.no_data_found), null)
    }

}