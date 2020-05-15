package com.driveinto.ladyj.skin

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface SkinApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("LadyJ/ReadSkin")
    fun read(@Query("key") id: Int): Call<DataSourceResponse<Skin>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("LadyJ/ModifySkin")
    suspend fun modify(@Query("key") id: Int, @FieldMap skin: Map<String, String>): DataSourceResponse<Skin>
}