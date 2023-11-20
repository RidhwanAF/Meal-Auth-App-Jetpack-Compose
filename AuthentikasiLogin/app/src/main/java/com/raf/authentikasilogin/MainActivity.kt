package com.raf.authentikasilogin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.raf.authentikasilogin.ui.main.HomeScreen
import com.raf.authentikasilogin.ui.main.LoginScreen
import com.raf.authentikasilogin.ui.main.MainViewModel
import com.raf.authentikasilogin.ui.setting.SettingsViewModel
import com.raf.authentikasilogin.ui.theme.AuthentikasiLoginTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel = hiltViewModel<MainViewModel>()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()

            val token by mainViewModel.getToken().collectAsState("")
            val dynamicColor by settingsViewModel.dynamicColor.collectAsState()
            val darkTheme by settingsViewModel.darkTheme.collectAsState()
            Log.d("token", "token: $token")
            AuthentikasiLoginTheme(
                dynamicColor = dynamicColor,
                darkTheme = darkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (token != "") {
                        HomeScreen()
                    } else {
                        LoginScreen()
                    }
                }
            }
        }
    }
}