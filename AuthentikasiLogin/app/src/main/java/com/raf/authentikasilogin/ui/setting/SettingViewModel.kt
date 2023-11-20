package com.raf.authentikasilogin.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.authentikasilogin.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository) :
    ViewModel() {

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme = _darkTheme.asStateFlow()

    private val _dynamicColor = MutableStateFlow(false)
    val dynamicColor = _dynamicColor.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreRepository.getDynamicColorState().collect { state ->
                _dynamicColor.value = state
            }
        }
        viewModelScope.launch {
            dataStoreRepository.getDarkThemeState().collect { state ->
                _darkTheme.value = state
            }
        }
    }

    fun settingDynamicColor(value: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setDynamicColorState(value)
        }
    }

    fun settingDarkTheme(value: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setDarkThemeState(value)
        }
    }
}