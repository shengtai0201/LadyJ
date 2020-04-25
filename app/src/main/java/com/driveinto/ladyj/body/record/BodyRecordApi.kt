package com.driveinto.ladyj.body.record

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface BodyRecordApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @GET("api/BodyRecords")
    fun read(@QueryMap request: Map<String, String>): Call<DataSourceResponse<BodyRecord>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("api/BodyRecords")
    suspend fun create(@FieldMap bodyRecord: Map<String, String>): DataSourceResponse<BodyRecord>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @PUT("api/BodyRecords")
    suspend fun update(
        @Query("dateMillis") dateMillis: Long,
        @Query("bodyId") bodyId: Int,
        @FieldMap bodyRecord: Map<String, String>
    ): DataSourceResponse<BodyRecord>

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @DELETE("api/BodyRecords")
    suspend fun destroy(
        @Query("dateMillis") dateMillis: Long,
        @Query("bodyId") bodyId: Int
    ): DataSourceResponse<BodyRecord>
}