package com.raf.authentikasilogin.network.api

import com.raf.authentikasilogin.network.response.ListUserResponse
import com.raf.authentikasilogin.network.response.LoginRequestBody
import com.raf.authentikasilogin.network.response.LoginSuccessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun loginUser(
        @Body request: LoginRequestBody
    ): Response<LoginSuccessResponse>

    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
    ): ListUserResponse

}