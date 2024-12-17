package com.example.ransomsensei

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ransomsensei.data.RansomSenseiDataStoreManager
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RansomSenseiDataStoreManagerTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testDataStoreManager = RansomSenseiDataStoreManager(testContext)

    @Test
    fun writeHomeActivityTest() =
        runTest {
            testDataStoreManager.saveHomeActivity("test_value")
            assertEquals(testDataStoreManager.getHomeActivity().first(), "test_value")
        }
}