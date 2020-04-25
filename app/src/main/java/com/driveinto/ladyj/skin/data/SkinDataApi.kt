package com.driveinto.ladyj.skin.data

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface SkinDataApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @GET("api/Skins")
    fun read(@QueryMap request: Map<String, String>): Call<DataSourceResponse<SkinData>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("api/Skins")
    suspend fun create(@FieldMap skinData: Map<String, String>): DataSourceResponse<SkinData>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @PUT("api/Skins")
    suspend fun update(
        @Query("dateMillis") dateMillis: Long,
        @Query("skinId") skinId: Int,
        @FieldMap skinData: Map<String, String>
    ): DataSourceResponse<SkinData>

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @DELETE("api/Skins")
    suspend fun destroy(
        @Query("dateMillis") dateMillis: Long,
        @Query("skinId") skinId: Int
    ): DataSourceResponse<SkinData>
}