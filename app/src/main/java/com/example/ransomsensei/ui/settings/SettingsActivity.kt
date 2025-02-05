package com.example.ransomsensei.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ransomsensei.theme.AppTheme
import com.example.ransomsensei.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = koinViewModel<SettingsViewModel>()
            AppTheme {
                Column {

                }
            }
        }
    }

    @Preview
    @Composable
    fun SettingsPreview() {
        AppTheme {
            Column {
                Text(text = "Settings")
            }
        }
    }
}