package org.ransomsensei.activity_lockscreen.viewmodels

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import org.ransomsensei.data.RansomSenseiDataRepository
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class LockScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: RansomSenseiDataRepository
    private val calendar = Calendar.getInstance()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        calendar.set(2025, 1, 1)
        repository = mock(RansomSenseiDataRepository::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loading with no active cards doesn't show question` () = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(null)
        `when`(repository.getHomePackage()).thenReturn(flow { emit ("")})
        `when`(repository.getLastInteraction()).thenReturn(calendar.time.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val viewModel =
            LockScreenViewModel(repository, calendar.time, _ioDispatcher = testDispatcher)
        viewModel.loadQuestion()
        advanceUntilIdle()

        assertThat(viewModel.isLoading).isFalse()
        assertThat(viewModel.showQuestion).isFalse()
        verify(repository).getRandomActiveCard()
        verify(repository).getHomePackage()
        verify(repository).getLastInteraction()
    }
}