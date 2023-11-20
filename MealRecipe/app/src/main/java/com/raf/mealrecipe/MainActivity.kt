package com.raf.mealrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raf.mealrecipe.ui.navigation.BottomNavMenu
import com.raf.mealrecipe.ui.navigation.NavigationGraph
import com.raf.mealrecipe.ui.setting.SettingsViewModel
import com.raf.mealrecipe.ui.theme.MealRecipeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val dynamicColor by settingsViewModel.dynamicColor.collectAsState()
            val darkTheme by settingsViewModel.darkTheme.collectAsState()

            MealRecipeTheme(
                dynamicColor = dynamicColor,
                darkTheme = darkTheme
            ) {
                var isSearching by remember {
                    mutableStateOf(false)
                }
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val topAppBarState = rememberSaveable { mutableStateOf(true) }
                val bottomBatState = rememberSaveable { mutableStateOf(true) }

                when (currentDestination?.route) {
                    BottomNavMenu.Home.route -> {
                        bottomBatState.value = true
                        topAppBarState.value = true
                    }

                    BottomNavMenu.Settings.route -> {
                        topAppBarState.value = false
                        bottomBatState.value = true
                    }

                    BottomNavMenu.Bookmark.route -> {
                        topAppBarState.value = false
                        bottomBatState.value = true
                    }

                    else -> {
                        bottomBatState.value = false
                        topAppBarState.value = false
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        if (topAppBarState.value) {
                            TopAppBarContent(scrollBehavior) {
                                isSearching = it
                            }
                        }
                    },
                    bottomBar = { if (bottomBatState.value) BottomBarContent(navController) },
                ) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        innerPaddingValues = innerPadding,
                        isSearching = isSearching
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContent(scrollBehavior: TopAppBarScrollBehavior, isSearching: (Boolean) -> Unit) {
    var isSearchingEnabled by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(
        targetValue = if (isSearchingEnabled) 225f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "Icon Rotation Animation"
    )

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        actions = {
            IconButton(
                onClick = {
                    isSearchingEnabled = !isSearchingEnabled
                    isSearching(isSearchingEnabled)
                }
            ) {
                if (isSearchingEnabled) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        modifier = Modifier
                            .rotate(rotation)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        modifier = Modifier
                            .rotate(rotation)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )

}

@Composable
fun BottomBarContent(navController: NavController) {
    val bottomNavMenuItems = listOf(
        BottomNavMenu.Home,
        BottomNavMenu.Bookmark,
        BottomNavMenu.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        bottomNavMenuItems.forEach { menu ->
            NavigationBarItem(
                icon = {
                    if (currentDestination?.hierarchy?.any { it.route == menu.route } == true) {
                        Icon(
                            imageVector = menu.selectedIcon,
                            contentDescription = stringResource(menu.label)
                        )
                    } else {
                        Icon(
                            imageVector = menu.icon,
                            contentDescription = stringResource(menu.label)
                        )
                    }
                },
                label = { Text(stringResource(menu.label)) },
                selected = currentDestination?.hierarchy?.any { it.route == menu.route } == true,
                onClick = {
                    navController.navigate(menu.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class TabItems(
    val title: String
)
