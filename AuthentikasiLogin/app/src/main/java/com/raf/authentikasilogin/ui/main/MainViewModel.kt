package com.raf.authentikasilogin.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.flatMap
import com.raf.authentikasilogin.network.api.ApiResult
import com.raf.authentikasilogin.network.response.DataItem
import com.raf.authentikasilogin.network.response.ListUserResponse
import com.raf.authentikasilogin.network.response.LoginRequestBody
import com.raf.authentikasilogin.repository.ApiRepository
import com.raf.authentikasilogin.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val dataStoreRepository: DataStoreRepository,
    pager: Pager<Int, DataItem>,
) : ViewModel() {

    private val _loginResult = MutableStateFlow("")
    val loginResult: StateFlow<String> get() = _loginResult

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> get() = _isError

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> get() = _isSuccess

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    val listUserPager = pager
        .flow
        .cachedIn(viewModelScope)

    fun loginUser(request: LoginRequestBody) {
        viewModelScope.launch {
            _isError.value = false
            _isSuccess.value = false
            _isLoading.value = true

            when (val result = apiRepository.loginUser(request)) {
                is ApiResult.Success -> {
                    val token = result.data.token
                    _isLoading.value = false
                    _isSuccess.value = true
                    _loginResult.value = token
                    saveToken(result.data.token)
                }

                is ApiResult.Error -> {
                    _isLoading.value = false
                    _isError.value = true
                    _loginResult.value = result.message
                }

                is ApiResult.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun resetLogin() {
        _isLoading.value = false
        _isError.value = false
        _loginResult.value = ""
        _isSuccess.value = false
    }

    private fun saveToken(token: String) {
        viewModelScope.launch {
            dataStoreRepository.setUserTokenState(token)
        }
    }

    fun getToken(): Flow<String> {
        return dataStoreRepository.getUserTokenState()
    }
}