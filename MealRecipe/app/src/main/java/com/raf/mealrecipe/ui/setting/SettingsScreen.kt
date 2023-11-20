package com.raf.mealrecipe.ui.setting

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.raf.mealrecipe.R

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = hiltViewModel()) {

    val isDynamicColorEnabled by settingsViewModel.dynamicColor.collectAsState()
    val isDarkThemeEnabled by settingsViewModel.darkTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.dark_theme))
            Switch(
                checked = isDarkThemeEnabled,
                onCheckedChange = {
                   settingsViewModel.settingDarkTheme(it)
                }
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.dynamic_color))
                Switch(
                    checked = isDynamicColorEnabled,
                    onCheckedChange = {
                        settingsViewModel.settingDynamicColor(it)
                    }
                )
            }
        }
    }
}
