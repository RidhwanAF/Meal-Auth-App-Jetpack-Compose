package com.raf.authentikasilogin.repository

import com.google.gson.Gson
import com.raf.authentikasilogin.network.api.ApiResult
import com.raf.authentikasilogin.network.api.ApiService
import com.raf.authentikasilogin.network.response.LoginFailedResponse
import com.raf.authentikasilogin.network.response.LoginRequestBody
import com.raf.authentikasilogin.network.response.LoginSuccessResponse
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun loginUser(loginRequest: LoginRequestBody): ApiResult<LoginSuccessResponse> {
        return try {
            ApiResult.Loading

            val response = apiService.loginUser(loginRequest)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    ApiResult.Error("Empty response body")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (!errorBody.isNullOrBlank()) {
                    val errorJson = Gson().fromJson(errorBody, LoginFailedResponse::class.java)
                    errorJson.error
                } else {
                    "Error: ${response.code()}"
                }

                ApiResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message}")
        }
    }

    suspend fun getUsers(page: Int) {
        apiService.getUsers(page)
    }
}