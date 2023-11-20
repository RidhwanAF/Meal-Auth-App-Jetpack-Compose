package com.raf.mealrecipe.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.raf.mealrecipe.R
import com.raf.mealrecipe.utility.Constants.Companion.BOOKMARK_ROUTE
import com.raf.mealrecipe.utility.Constants.Companion.HOME_ROUTE
import com.raf.mealrecipe.utility.Constants.Companion.SETTINGS_ROUTE

sealed class BottomNavMenu(
    val route: String,
    @StringRes val label: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Home : BottomNavMenu(
        HOME_ROUTE,
        R.string.home,
        Icons.Outlined.Home,
        Icons.Filled.Home
    )

    data object Settings : BottomNavMenu(
        SETTINGS_ROUTE,
        R.string.settings,
        Icons.Outlined.Settings,
        Icons.Filled.Settings
    )


    data object Bookmark : BottomNavMenu(
        BOOKMARK_ROUTE,
        R.string.bookmark,
        Icons.Outlined.List,
        Icons.Filled.Menu
    )
}