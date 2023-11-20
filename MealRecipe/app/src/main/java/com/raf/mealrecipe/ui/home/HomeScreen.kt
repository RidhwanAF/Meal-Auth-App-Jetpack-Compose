package com.raf.mealrecipe.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.raf.mealrecipe.R
import com.raf.mealrecipe.data.local.entity.BookmarkEntity
import com.raf.mealrecipe.data.network.response.MealsItem
import com.raf.mealrecipe.utility.Constants.Companion.DETAIL_ROUTE
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    isSearching: Boolean,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val bookmarkedMeal = homeViewModel.bookmarkedMeal.collectAsState(initial = emptyList())
    val areaCategories by homeViewModel.areaCategories.collectAsState()
    val foodCategories by homeViewModel.foodCategories.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val isError by homeViewModel.isError.collectAsState()

    if (areaCategories.isEmpty() || foodCategories.isEmpty()) {
        homeViewModel.getApiCategoriesData()
    } else {
        homeViewModel.getLocalCategoriesData()
    }

    if (isError) {
        Toast.makeText(
            context,
            stringResource(R.string.fetch_data_failed),
            Toast.LENGTH_SHORT
        ).show()
    }

    if (areaCategories.isEmpty() || foodCategories.isEmpty()) {
        LoadingShimmer()
    } else {
        ListOfFoodsContent(
            foodCategories,
            areaCategories,
            homeViewModel,
            bookmarkedMeal,
            isLoading,
            navController
        )
        if (isSearching) {
            SearchingContent(homeViewModel = homeViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchingContent(homeViewModel: HomeViewModel) {
    val searchValue by homeViewModel.searchInputValue.observeAsState("")

    Surface(Modifier.wrapContentHeight()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchValue,
                onValueChange = {
                    homeViewModel.setSearchInput(it)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                },
                label = {
                    Text(text = stringResource(R.string.search_food_by_name) + "...")
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListOfFoodsContent(
    foodCategories: List<String>,
    areaCategories: List<String>,
    homeViewModel: HomeViewModel,
    bookmarkedMeal: State<List<BookmarkEntity>>,
    isLoading: Boolean,
    navController: NavController
) {
    val searchValue by homeViewModel.searchInputValue.observeAsState("")
    val foodListByCategoryState = homeViewModel.foodListByCategory.collectAsState()
    val foodListByAreaState = homeViewModel.foodListByArea.collectAsState()

    var listFoodByCategory: List<MealsItem> by remember { mutableStateOf(emptyList()) }
    var listFoodByArea: List<MealsItem> by remember { mutableStateOf(emptyList()) }

    val listOfTabItem = listOf(
        TabItems(stringResource(R.string.food)),
        TabItems(stringResource(R.string.area)),
    )
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        listOfTabItem.size
    }
    var selectedChip by remember {
        mutableStateOf(foodCategories.firstOrNull() ?: "")
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    LaunchedEffect(foodListByCategoryState.value, searchValue) {
        val filteredList = foodListByCategoryState.value.meals.orEmpty()
            .filter { it.strMeal.contains(searchValue, ignoreCase = true) }
        listFoodByCategory = filteredList
    }

    LaunchedEffect(foodListByAreaState.value, searchValue) {
        val filteredList = foodListByAreaState.value.meals.orEmpty()
            .filter { it.strMeal.contains(searchValue, ignoreCase = true) }
        listFoodByArea = filteredList
    }

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            listOfTabItem.forEachIndexed { index, tabItem ->
                Tab(
                    selected = selectedTabIndex == pagerState.currentPage,
                    onClick = {
                        selectedTabIndex = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(text = tabItem.title, modifier = Modifier.padding(16.dp))
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) { pageIndex ->
            if (isLoading) {
                LoadingShimmer()
            } else {
                when (pageIndex) {
                    0 -> {
                        if (listFoodByCategory.isEmpty()) {
                            Column {
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer.copy(
                                                0.4f
                                            )
                                        )
                                ) {
                                    items(foodCategories) { category ->
                                        FilterChip(
                                            selected = selectedChip == category,
                                            onClick = {
                                                selectedChip = category
                                                homeViewModel.getFoodListByCategory(category)
                                            },
                                            label = { Text(category) },
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                                ShowEmptyScreen()
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                stickyHeader {
                                    LazyRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.primaryContainer.copy(
                                                    0.4f
                                                )
                                            )
                                    ) {
                                        items(foodCategories) { category ->
                                            FilterChip(
                                                selected = selectedChip == category,
                                                onClick = {
                                                    selectedChip = category
                                                    homeViewModel.getFoodListByCategory(category)
                                                },
                                                label = { Text(category) },
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }
                                items(listFoodByCategory) { items ->
                                    Surface(
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp
                                        )
                                    ) {
                                        FoodItemContent(items, bookmarkedMeal, homeViewModel, navController)
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        if (listFoodByArea.isEmpty()) {
                            Column {
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer.copy(
                                                0.4f
                                            )
                                        )
                                ) {
                                    items(areaCategories) { category ->
                                        FilterChip(
                                            selected = selectedChip == category,
                                            onClick = {
                                                selectedChip = category
                                                homeViewModel.getFoodListByArea(category)
                                            },
                                            label = { Text(category) },
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                                ShowEmptyScreen()
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                stickyHeader {
                                    LazyRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.primaryContainer.copy(
                                                    0.4f
                                                )
                                            )
                                    ) {
                                        items(areaCategories) { category ->
                                            FilterChip(
                                                selected = selectedChip == category,
                                                onClick = {
                                                    selectedChip = category
                                                    homeViewModel.getFoodListByArea(category)
                                                },
                                                label = { Text(category) },
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }
                                items(listFoodByArea) { items ->
                                    Surface(
                                        modifier = Modifier.padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp
                                        )
                                    ) {
                                        FoodItemContent(items, bookmarkedMeal, homeViewModel, navController)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItemContent(
    food: MealsItem,
    bookmarkedMeal: State<List<BookmarkEntity>>,
    homeViewModel: HomeViewModel,
    navController: NavController
) {

    val isBookmarked = bookmarkedMeal.value.any { meal ->
        meal.id == food.idMeal
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {
            navController.navigate("$DETAIL_ROUTE/${food.idMeal}")
        }
    ) {
        ConstraintLayout {
            val (imageRef, titleRef) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                SubcomposeAsyncImage(
                    model = food.strMealThumb,
                    contentDescription = food.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        Image(
                            painterResource(R.drawable.ic_image),
                            contentDescription = "Image",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .shimmer()
                        )
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    onClick = {
                        val bookmarkEntity = BookmarkEntity(
                            food.idMeal,
                            food.strMeal,
                            food.strMealThumb
                        )
                        if (isBookmarked) {
                            homeViewModel.deleteBookmarkedMeal(food.idMeal)
                        } else {
                            homeViewModel.insertBookmarkMeal(bookmarkEntity)
                        }

                    }
                ) {
                    this@Card.AnimatedVisibility(
                        visible = isBookmarked,
                        enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                        exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bookmark),
                            contentDescription = stringResource(R.string.bookmark),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    this@Card.AnimatedVisibility(
                        visible = !isBookmarked,
                        enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                        exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bookmark_border),
                            contentDescription = stringResource(R.string.bookmark),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(0.8f))
                    .constrainAs(titleRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = food.strMeal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LoadingShimmer() {
    val randomWidthChip = listOf(40..64).random()
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Column(
        Modifier
            .fillMaxSize()
            .shimmer(shimmerInstance)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            for (i in 1..10) {
                Box(
                    modifier = Modifier
                        .width(randomWidthChip.random().dp)
                        .height(32.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onPrimaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .shimmer()
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        for (i in 1..10) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
            ) {
                ConstraintLayout {
                    val (imageRef, titleRef) = createRefs()

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .constrainAs(imageRef) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Image(
                            painterResource(R.drawable.ic_image),
                            contentDescription = "Image",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(32.dp)
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    CircleShape
                                ),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(0.8f))
                            .constrainAs(titleRef) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                                .shimmer()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowEmptyScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.empty_list),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

data class TabItems(
    val title: String
)
