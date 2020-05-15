package com.driveinto.ladyj.body

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface BodyApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("LadyJ/ReadBody")
    fun read(@Query("key") id: Int): Call<DataSourceResponse<Body>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("LadyJ/ModifyBody")
    suspend fun modify(@Query("key") id: Int, @FieldMap customer: Map<String, String>): DataSourceResponse<Body>
}