package com.albs.challenge.search_meanings

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.albs.challenge.database.AppDao
import com.albs.challenge.network.ApiService
import com.albs.challenge.repository.AppRepository
import com.albs.challenge.utils.Utils
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchMeaningsViewModelTest {

    private lateinit var viewModel: SearchMeaningsViewModel
    private val appContext = mockk<Application>()
    private lateinit var repository: AppRepository
    private val apiService = mockk<ApiService>()
    private val dao = mockk<AppDao>()
    private val dispatcher = StandardTestDispatcher()
    private val sf = "as"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = AppRepository(apiService, dao, appContext)
        viewModel = SearchMeaningsViewModel(repository, appContext)
    }

    @Test
    fun getMeanings_success() {
        every { Utils.checkForInternet(appContext) }.returns(true)
        runBlocking {
            coEvery { dao.fetch(sf) }.answers { null }
            coEvery { viewModel.fetchMeanings(sf) }
        }
        viewModel.getMeanings(sf)
        assertNotNull(viewModel.meaningsData.value)
    }

    @Test
    fun getMeanings_unSuccess() {
        every { Utils.checkForInternet(appContext) }.returns(true)
        runBlocking {
            coEvery { dao.fetch(sf) }.answers { null }
        }
        viewModel.getMeanings(sf)
        assertEquals(true, viewModel.meaningsData.value == null)
    }

}