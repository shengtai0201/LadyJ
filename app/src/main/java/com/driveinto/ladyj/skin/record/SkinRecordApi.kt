package com.driveinto.ladyj.skin.record

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface SkinRecordApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @GET("api/SkinRecords")
    fun read(@QueryMap request: Map<String, String>): Call<DataSourceResponse<SkinRecord>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("api/SkinRecords")
    suspend fun create(@FieldMap skinRecord: Map<String, String>): DataSourceResponse<SkinRecord>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @PUT("api/SkinRecords")
    suspend fun update(
        @Query("dateMillis") dateMillis: Long,
        @Query("skinId") skinId: Int,
        @FieldMap skinRecord: Map<String, String>
    ): DataSourceResponse<SkinRecord>

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @DELETE("api/SkinRecords")
    suspend fun destroy(
        @Query("dateMillis") dateMillis: Long,
        @Query("skinId") skinId: Int
    ): DataSourceResponse<SkinRecord>
}