package com.driveinto.ladyj.login

import retrofit2.http.*

interface LoginApi {
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("LadyJ/Login")
    suspend fun login(@FieldMap info: Map<String, String>): LoginResponse
}