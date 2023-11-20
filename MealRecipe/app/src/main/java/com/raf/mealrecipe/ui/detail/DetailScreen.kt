package com.raf.mealrecipe.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.raf.mealrecipe.R
import com.raf.mealrecipe.data.network.response.DetailMealsItem
import com.raf.mealrecipe.ui.home.HomeViewModel
import com.raf.mealrecipe.ui.home.ShowEmptyScreen
import com.valentinilk.shimmer.shimmer

@Composable
fun DetailScreen(
    navController: NavController,
    foodId: String?,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    if (foodId != null) {
        val foodDetail by homeViewModel.getFoodDetail(foodId).collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                SubcomposeAsyncImage(
                    model = foodDetail.meals?.firstOrNull()?.strMealThumb,
                    contentDescription = foodDetail.meals?.firstOrNull()?.strMeal,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
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
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .padding(8.dp)
                        .size(32.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = foodDetail.meals?.firstOrNull()?.strMeal.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )


                Text(
                    text = "Description: ${foodDetail.meals?.firstOrNull()?.strInstructions.orEmpty()}",
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = buildIngredientsText(foodDetail.meals?.firstOrNull()),
                    modifier = Modifier.padding(top = 8.dp)
                )

                val youtubeLink = foodDetail.meals?.firstOrNull()?.strYoutube.orEmpty()
                if (youtubeLink.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AndroidView(factory = {
                        val view = YouTubePlayerView(it)
                        view.addYouTubePlayerListener(
                            object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    super.onReady(youTubePlayer)
                                    youTubePlayer.loadVideo(youtubeLink, 0f)
                                }
                            }
                        )
                        view
                    })
                }
            }
        }
    } else {
        ShowEmptyScreen()
    }
}

@Composable
private fun buildIngredientsText(meal: DetailMealsItem?): String {
    val ingredients = mutableListOf<String>()

    repeat(20) { index ->
        val ingredient = meal?.getIngredient(index)
        val measure = meal?.getMeasure(index)

        if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
            ingredients.add("$ingredient: $measure")
        }
    }

    return ingredients.joinToString("\n")
}

fun DetailMealsItem?.getIngredient(index: Int): String? =
    when (index) {
        1 -> this?.strIngredient1
        2 -> this?.strIngredient2
        3 -> this?.strIngredient3
        4 -> this?.strIngredient4
        5 -> this?.strIngredient5
        6 -> this?.strIngredient6
        7 -> this?.strIngredient7
        8 -> this?.strIngredient8
        9 -> this?.strIngredient9
        10 -> this?.strIngredient10
        11 -> this?.strIngredient11
        12 -> this?.strIngredient12
        13 -> this?.strIngredient13
        14 -> this?.strIngredient14
        15 -> this?.strIngredient15
        16 -> this?.strIngredient16
        17 -> this?.strIngredient17
        18 -> this?.strIngredient18
        19 -> this?.strIngredient19
        20 -> this?.strIngredient20
        else -> null
    }

fun DetailMealsItem?.getMeasure(index: Int): String? =
    when (index) {
        1 -> this?.strMeasure1
        2 -> this?.strMeasure2
        3 -> this?.strMeasure3
        4 -> this?.strMeasure4
        5 -> this?.strMeasure5
        6 -> this?.strMeasure6
        7 -> this?.strMeasure7
        8 -> this?.strMeasure8
        9 -> this?.strMeasure9
        10 -> this?.strMeasure10
        11 -> this?.strMeasure11
        12 -> this?.strMeasure12
        13 -> this?.strMeasure13
        14 -> this?.strMeasure14
        15 -> this?.strMeasure15
        16 -> this?.strMeasure16
        17 -> this?.strMeasure17
        18 -> this?.strMeasure18
        19 -> this?.strMeasure19
        20 -> this?.strMeasure20
        else -> null
    }