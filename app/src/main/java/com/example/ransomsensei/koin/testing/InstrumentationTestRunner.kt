package com.example.ransomsensei.koin.testing

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/** [InstrumentationTestRunner] using [KoinTestApplication] */
@Suppress("unused")
class InstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, KoinTestApplication::class.java.name, context)
    }
}