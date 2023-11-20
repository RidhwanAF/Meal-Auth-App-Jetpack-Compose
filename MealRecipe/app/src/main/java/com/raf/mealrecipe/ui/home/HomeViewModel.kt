package com.raf.mealrecipe.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.mealrecipe.data.local.entity.AreaCategoriesEntity
import com.raf.mealrecipe.data.local.entity.BookmarkEntity
import com.raf.mealrecipe.data.local.entity.FoodCategoriesEntity
import com.raf.mealrecipe.data.network.response.GetFoodDetailResponse
import com.raf.mealrecipe.data.network.response.GetFoodListByCategoryResponse
import com.raf.mealrecipe.data.repository.ApiRepository
import com.raf.mealrecipe.data.repository.LocalDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localDbRepository: LocalDbRepository,
) : ViewModel() {

    private val _searchInputValue = MutableLiveData<String>()
    val searchInputValue: LiveData<String> = _searchInputValue

    private val _foodCategories = MutableStateFlow(listOf(""))
    val foodCategories: StateFlow<List<String>> = _foodCategories

    private val _areaCategories = MutableStateFlow(listOf(""))
    val areaCategories: StateFlow<List<String>> = _areaCategories

    private val _foodListByCategory = MutableStateFlow(GetFoodListByCategoryResponse(emptyList()))
    val foodListByCategory: StateFlow<GetFoodListByCategoryResponse> = _foodListByCategory

    private val _foodListByArea = MutableStateFlow(GetFoodListByCategoryResponse(emptyList()))
    val foodListByArea: StateFlow<GetFoodListByCategoryResponse> = _foodListByArea

    private val _foodDetail = MutableStateFlow(GetFoodDetailResponse(emptyList()))
    val foodDetail: StateFlow<GetFoodDetailResponse> = _foodDetail

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    init {
        getFoodListByCategory("Beef")
        getFoodListByArea("American")
    }

    fun getApiCategoriesData() {
        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                val getMealCategoryByArea = apiRepository.getMealCategoriesByArea()
                val getMealCategoryByFood = apiRepository.getMealCategoriesByFood()

                getMealCategoryByArea.meals.forEach {
                    localDbRepository.insertAreaCategories(AreaCategoriesEntity(categoryArea = it.strArea))
                }
                getMealCategoryByFood.meals.forEach {
                    localDbRepository.insertFoodCategories(FoodCategoriesEntity(categoryFood = it.strCategory))
                }

                _foodCategories.value =
                    localDbRepository.getAllCategories().first.map { it.categoryFood }
                _areaCategories.value =
                    localDbRepository.getAllCategories().second.map { it.categoryArea }

                _isLoading.value = false
                _isError.value = false
            } catch (e: Exception) {
                _isError.value = true
                _isLoading.value = false
                Log.e(TAG, "onError: $e")
            }
        }
    }

    fun getLocalCategoriesData() {
        viewModelScope.launch {
            _foodCategories.value =
                localDbRepository.getAllCategories().first.map { it.categoryFood }
            _areaCategories.value =
                localDbRepository.getAllCategories().second.map { it.categoryArea }
            _isLoading.value = false
        }
    }

    fun getFoodListByCategory(category: String) {
        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                _foodListByCategory.value =
                    apiRepository.getFoodListByCategory(category)
                _isError.value = false
                _isLoading.value = false
            } catch (e: Exception) {
                _isError.value = true
                _isLoading.value = false
                Log.e(TAG, "onError: $e")
            }
        }
    }

    fun getFoodListByArea(category: String) {
        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                _foodListByArea.value = apiRepository.getFoodListByArea(category)
                _isError.value = false
                _isLoading.value = false
            } catch (e: Exception) {
                _isError.value = true
                _isLoading.value = false
                Log.e(TAG, "onError: $e")
            }
        }
    }

    fun insertBookmarkMeal(bookmarkEntity: BookmarkEntity) {
        viewModelScope.launch {
            localDbRepository.insertBookmarkMeal(bookmarkEntity)
        }
    }

    fun deleteBookmarkedMeal(id: String) {
        viewModelScope.launch {
            localDbRepository.deleteBookmarkedMeal(id)
        }
    }

    val bookmarkedMeal = localDbRepository.getBookmarkedMeal()

    fun getFoodDetail(id: String): StateFlow<GetFoodDetailResponse> {
        _isLoading.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                val detailFood = apiRepository.getFoodDetail(id)
                _foodDetail.value = detailFood
                _isError.value = false
                _isLoading.value = false
            } catch (e: Exception) {
                _isError.value = true
                _isLoading.value = false
                Log.e(TAG, "onError: $e")
            }
        }
        return foodDetail
    }

    fun setSearchInput(value: String) {
        _searchInputValue.value = value
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}