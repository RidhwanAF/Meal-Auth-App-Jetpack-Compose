package com.raf.mealrecipe.ui.navigation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raf.mealrecipe.ui.bookmark.BookmarkScreen
import com.raf.mealrecipe.ui.detail.DetailScreen
import com.raf.mealrecipe.ui.home.HomeScreen
import com.raf.mealrecipe.ui.setting.SettingsScreen
import com.raf.mealrecipe.utility.Constants.Companion.DETAIL_ROUTE

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    innerPaddingValues: PaddingValues,
    isSearching: Boolean,
) {

    NavHost(
        navController = navController,
        startDestination = BottomNavMenu.Home.route,
        modifier = modifier
            .padding(innerPaddingValues)
            .animateContentSize()
    ) {
        composable(BottomNavMenu.Home.route) {
            HomeScreen(navController, isSearching)
        }
        composable(BottomNavMenu.Bookmark.route) {
            BookmarkScreen(navController)
        }
        composable(BottomNavMenu.Settings.route) {
            SettingsScreen()
        }
        composable("$DETAIL_ROUTE/{foodId}") {
            val foodId = it.arguments?.getString("foodId")
            DetailScreen(navController, foodId)
        }
    }
}