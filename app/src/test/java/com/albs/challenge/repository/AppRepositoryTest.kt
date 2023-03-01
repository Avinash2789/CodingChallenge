package com.albs.challenge.repository

import android.app.Application
import com.albs.challenge.R
import com.albs.challenge.database.AppDao
import com.albs.challenge.model.Lfs
import com.albs.challenge.model.MeaningData
import com.albs.challenge.model.MeaningsResponse
import com.albs.challenge.network.ApiService
import com.albs.challenge.utils.Resource
import com.albs.challenge.utils.Utils
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class AppRepositoryTest {

    private val apiService = mockk<ApiService>(relaxed = true)
    private val dao = mockk<AppDao>(relaxed = true)
    private val appContext = mockk<Application>(relaxed = true)
    private lateinit var repository: AppRepository
    private val dispatcher = StandardTestDispatcher()
    private val sf = "as"
    private val lfs = Lfs("synovial fluid", 1123, 1973, arrayListOf())
    private val lfsArray: ArrayList<Lfs> = arrayListOf(lfs)
    private val meaningsResponse = MeaningsResponse(sf, lfsArray)
    private val errorResponse = Response.error<ArrayList<MeaningsResponse>>(
        500,
        "Test Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = AppRepository(apiService, dao, appContext)
    }

    @Test
    fun meaningsFromServer_success() {
        val meaningsResponseMockk = arrayListOf(meaningsResponse)
        val response = Response.success(meaningsResponseMockk)
        runBlocking {
            coEvery { apiService.getMeanings(sf) }.returns(response)
            val serverResponse = apiService.getMeanings(sf)
            Assertions.assertTrue(serverResponse.isSuccessful)
        }
    }

    @Test
    fun meaningsFromServer_unSuccess() {
        runBlocking {
            coEvery { apiService.getMeanings(sf) }.returns(errorResponse)
            val serverResponse = apiService.getMeanings(sf)
            Assertions.assertFalse(serverResponse.isSuccessful)
        }
    }

    @Test
    fun handleResponse_returns_success() {
        val response = repository.handleResponse(meaningsResponse)
        Assertions.assertTrue(response is Resource.Success)
    }

    @Test
    fun handleResponse_returns_error() {
        val response = repository.handleResponse(null)
        Assertions.assertTrue(response is Resource.Error)
    }

    @Test
    fun getAllMeanings_returns_success() {
        val serverResponse: Response<ArrayList<MeaningsResponse>> =
            Response.success(arrayListOf(meaningsResponse))
        val meaningData = MeaningData(sf, serverResponse.body()!![0])
        every { Utils.checkForInternet(appContext) }.returns(true)
        runBlocking {
            coEvery { dao.fetch(sf) }.answers { null }
            coEvery { dao.insert(meaningData) }.answers { 1 }
            coEvery { apiService.getMeanings(sf) }.returns(serverResponse)
            coEvery { dao.fetch(sf) }.answers { meaningData }
            val resourceResponse = repository.getAllMeanings(sf)
            Assertions.assertTrue(resourceResponse is Resource.Success)
        }
    }

    @Test
    fun getAllMeanings_returns_error() {
        every { Utils.checkForInternet(appContext) }.returns(true)
        every { appContext.getString(R.string.no_data_found) }.returns("No Data Found")

        runBlocking {
            coEvery { dao.fetch(sf) }.answers { null }
            coEvery { apiService.getMeanings(sf) }.returns(errorResponse)
            coEvery { dao.fetch(sf) }.answers { null }
            val resourceResponse = repository.getAllMeanings(sf)
            Assertions.assertTrue(resourceResponse is Resource.Error)
        }
    }
}