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
import org.ransomsensei.activity_lockscreen.model.DiffFragment
import org.ransomsensei.activity_lockscreen.model.DiffFragmentList
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.entity.Card
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
    fun `loading with no active cards doesn't show question`() = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(null)
        `when`(repository.getHomePackage()).thenReturn(flow { emit("") })
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

    @Test
    fun `editDistance for empty response`() = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(Card(englishValue = "Test Card"))
        `when`(repository.getHomePackage()).thenReturn(flow { emit("") })
        `when`(repository.getLastInteraction()).thenReturn(calendar.time.time)
        calendar.add(Calendar.DAY_OF_MONTH, 9)

        val viewModel =
            LockScreenViewModel(repository, calendar.time, _ioDispatcher = testDispatcher)
        viewModel.loadQuestion()
        advanceUntilIdle()

        viewModel.calculateDifference()
        assertThat(viewModel.difference).isEqualTo(
            DiffFragmentList(
                diffFragments = listOf(
                    DiffFragment(type = DiffFragment.Type.DELETION,
                        inputSubstring = "",
                        targetSubstring = "Test Card")
                ), editDistance = 9
            )
        )
    }

    @Test
    fun `editDistance for match`() = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(Card(englishValue = "answer"))
        `when`(repository.getHomePackage()).thenReturn(flow { emit("") })
        `when`(repository.getLastInteraction()).thenReturn(calendar.time.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val viewModel =
            LockScreenViewModel(repository, calendar.time, _ioDispatcher = testDispatcher)
        viewModel.loadQuestion()
        advanceUntilIdle()
        viewModel.setAnswer("answer")

        viewModel.calculateDifference()
        assertThat(viewModel.difference).isEqualTo(
            DiffFragmentList(
                diffFragments = listOf(
                    DiffFragment(inputSubstring = "answer", targetSubstring = "answer", type = DiffFragment.Type.MATCH)
                ), editDistance = 0
            )
        )
    }

    @Test
    fun `editDistance with replacements`() = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(Card(englishValue = "answer"))
        `when`(repository.getHomePackage()).thenReturn(flow { emit("") })
        `when`(repository.getLastInteraction()).thenReturn(calendar.time.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val viewModel =
            LockScreenViewModel(repository, calendar.time, _ioDispatcher = testDispatcher)
        viewModel.loadQuestion()
        advanceUntilIdle()
        viewModel.setAnswer("onswel")

        viewModel.calculateDifference()
        assertThat(viewModel.difference).isEqualTo(
            DiffFragmentList(
                diffFragments = listOf(
                    DiffFragment(type = DiffFragment.Type.REPLACEMENT, inputSubstring = "o", targetSubstring = "a"),
                    DiffFragment(type = DiffFragment.Type.MATCH, inputSubstring = "nswe", targetSubstring = "nswe", ),
                    DiffFragment(type = DiffFragment.Type.REPLACEMENT, inputSubstring = "l", targetSubstring = "r")
                ), editDistance = 2
            )
        )
    }

    @Test
    fun `editDistance with additions`() = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(Card(englishValue = "answer"))
        `when`(repository.getHomePackage()).thenReturn(flow { emit("") })
        `when`(repository.getLastInteraction()).thenReturn(calendar.time.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val viewModel =
            LockScreenViewModel(repository, calendar.time, _ioDispatcher = testDispatcher)
        viewModel.loadQuestion()
        advanceUntilIdle()
        viewModel.setAnswer("answer and then some")
        viewModel.calculateDifference()


        assertThat(viewModel.difference).isEqualTo(
            DiffFragmentList(
                diffFragments = listOf(
                    DiffFragment(type = DiffFragment.Type.MATCH, inputSubstring = "answer", targetSubstring = "answer"),
                    DiffFragment(type = DiffFragment.Type.ADDITION, inputSubstring= " and then some", targetSubstring = "")
                ), editDistance = 14
            )
        )
    }

    @Test
    fun `editDistance with deletions`() = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(Card(englishValue = "Right answer"))
        `when`(repository.getHomePackage()).thenReturn(flow { emit("") })
        `when`(repository.getLastInteraction()).thenReturn(calendar.time.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val viewModel =
            LockScreenViewModel(repository, calendar.time, _ioDispatcher = testDispatcher)
        viewModel.loadQuestion()
        advanceUntilIdle()
        viewModel.setAnswer("Right")

        viewModel.calculateDifference()
        assertThat(viewModel.difference).isEqualTo(
            DiffFragmentList(
                diffFragments = listOf(
                    DiffFragment(type = DiffFragment.Type.MATCH, inputSubstring = "Right", targetSubstring = "Right"),
                    DiffFragment(type = DiffFragment.Type.DELETION, inputSubstring = "", targetSubstring = " answer")
                ), editDistance = 7
            )
        )
    }

    @Test
    fun `editDistance ignores case`() = runTest(testDispatcher) {
        `when`(repository.getRandomActiveCard()).thenReturn(Card(englishValue = "uppercase"))
        `when`(repository.getHomePackage()).thenReturn(flow { emit("") })
        `when`(repository.getLastInteraction()).thenReturn(calendar.time.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val viewModel =
            LockScreenViewModel(repository, calendar.time, _ioDispatcher = testDispatcher)
        viewModel.loadQuestion()
        advanceUntilIdle()
        viewModel.setAnswer("UPPERCASE")

        viewModel.calculateDifference()
        assertThat(viewModel.difference).isEqualTo(
            DiffFragmentList(
                diffFragments = listOf(
                    DiffFragment(type = DiffFragment.Type.MATCH, inputSubstring = "UPPERCASE", targetSubstring = "uppercase")
                ), editDistance = 0
            )
        )
    }
}