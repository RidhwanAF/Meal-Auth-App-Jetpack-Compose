package com.raf.authentikasilogin.network.response

import com.google.gson.annotations.SerializedName

data class LoginSuccessResponse(
    @field:SerializedName("token")
    val token: String
)

data class LoginFailedResponse(
    @field:SerializedName("error")
    val error: String
)

data class LoginRequestBody(
    val email: String,
    val password: String
)
