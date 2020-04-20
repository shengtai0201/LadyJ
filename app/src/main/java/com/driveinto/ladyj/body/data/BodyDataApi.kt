package com.driveinto.ladyj.body.data

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface BodyDataApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @GET("api/Bodies")
    fun read(@QueryMap request: Map<String, String>): Call<DataSourceResponse<BodyData>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("api/Bodies")
    suspend fun create(@FieldMap bodyData: Map<String, String>): DataSourceResponse<BodyData>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @PUT("api/Bodies")
    suspend fun update(
        @Query("dateMillis") dateMillis: Long,
        @Query("bodyId") bodyId: Int,
        @FieldMap bodyData: Map<String, String>
    ): DataSourceResponse<BodyData>

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @DELETE("api/Bodies")
    suspend fun destroy(
        @Query("dateMillis") dateMillis: Long,
        @Query("bodyId") bodyId: Int
    ): DataSourceResponse<BodyData>
}