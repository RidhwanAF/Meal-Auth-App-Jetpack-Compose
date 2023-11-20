package com.raf.mealrecipe.ui.bookmark

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.raf.mealrecipe.ui.home.HomeViewModel
import com.raf.mealrecipe.ui.home.ShowEmptyScreen
import com.raf.mealrecipe.utility.Constants
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val bookmarkedMeal = homeViewModel.bookmarkedMeal.collectAsState(initial = emptyList())
    val areaCategories by homeViewModel.areaCategories.collectAsState()
    val foodCategories by homeViewModel.foodCategories.collectAsState()
    if (areaCategories.isEmpty() || foodCategories.isEmpty()) {
        homeViewModel.getApiCategoriesData()
    } else {
        homeViewModel.getLocalCategoriesData()
    }

    Column(
        Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.bookmark),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
        )
        if (bookmarkedMeal.value.isEmpty()) {
            ShowEmptyScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(bookmarkedMeal.value) { items ->
                    Surface(
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                            horizontal = 16.dp
                        )
                    ) {
                        BookmarkedMealItemContent(items, homeViewModel, navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkedMealItemContent(
    food: BookmarkEntity,
    homeViewModel: HomeViewModel,
    navController: NavController
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = { navController.navigate("${Constants.DETAIL_ROUTE}/${food.id}") }
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
                    model = food.thumbnail,
                    contentDescription = food.meal,
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
                        homeViewModel.deleteBookmarkedMeal(food.id)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bookmark),
                        contentDescription = stringResource(R.string.bookmark),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
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
                Text(text = food.meal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}