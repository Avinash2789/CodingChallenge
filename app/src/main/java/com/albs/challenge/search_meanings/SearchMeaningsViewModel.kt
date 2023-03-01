package com.albs.challenge.search_meanings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albs.challenge.R
import com.albs.challenge.model.MeaningsResponse
import com.albs.challenge.repository.AppRepository
import com.albs.challenge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchMeaningsViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val application: Application
) :
    ViewModel() {

    val meaningsData: MutableLiveData<Resource<MeaningsResponse>> = MutableLiveData()

    fun getMeanings(searchText: String) {
        if (searchText.length > 1) {
            viewModelScope.launch {
                fetchMeanings(searchText)
            }
        } else {
            meaningsData.postValue(Resource.Error(this.application.getString(R.string.minimum_2_character), null))
        }
    }

    suspend fun fetchMeanings(searchText: String) {
        val response = appRepository.getAllMeanings(searchText)
        meaningsData.postValue(response)
    }

}