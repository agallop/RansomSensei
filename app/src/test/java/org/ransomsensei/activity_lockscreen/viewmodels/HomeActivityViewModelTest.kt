package org.ransomsensei.activity_lockscreen.viewmodels

import kotlinx.coroutines.Dispatchers
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
import com.google.common.truth.Truth.assertThat

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeActivityViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: RansomSenseiDataRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(RansomSenseiDataRepository::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init sets isInOnboarding and isLoading`() = runTest(testDispatcher) {
        `when`(repository.getIsInOnboarding()).thenReturn(true)

        val viewModel = HomeActivityViewModel(repository, _ioDispatcher = testDispatcher)
        advanceUntilIdle()

        assertThat(viewModel.isInOnboarding).isTrue()
        assertThat(viewModel.isLoading).isFalse()
        verify(repository).getIsInOnboarding()
    }

    @Test
    fun `init sets isInOnboarding to false and isLoading to false`() = runTest(testDispatcher) {
        `when`(repository.getIsInOnboarding()).thenReturn(false)

        val viewModel = HomeActivityViewModel(repository, _ioDispatcher = testDispatcher)
        advanceUntilIdle()

        assertThat(viewModel.isInOnboarding).isFalse()
        assertThat(viewModel.isLoading).isFalse()
        verify(repository).getIsInOnboarding()
    }

    @Test
    fun `isLoading is true initially`() = runTest(testDispatcher) {
        `when`(repository.getIsInOnboarding()).thenReturn(false)

        val viewModel = HomeActivityViewModel(repository, _ioDispatcher = testDispatcher)

        assertThat(viewModel.isLoading).isTrue()
    }
}