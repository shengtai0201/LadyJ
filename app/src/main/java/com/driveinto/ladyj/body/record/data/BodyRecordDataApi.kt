package com.driveinto.ladyj.body.record.data

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface BodyRecordDataApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @GET("api/BodyRecordDatas")
    fun read(@QueryMap request: Map<String, String>): Call<DataSourceResponse<BodyRecordData>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("api/BodyRecordDatas")
    suspend fun create(@FieldMap bodyRecordData: Map<String, String>): DataSourceResponse<BodyRecordData>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @PUT("api/BodyRecordDatas")
    suspend fun update(
        @Query("dateMillis") dateMillis: Long,
        @Query("bodyId") bodyId: Int,
        @FieldMap bodyRecordData: Map<String, String>
    ): DataSourceResponse<BodyRecordData>

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @DELETE("api/BodyRecordDatas")
    suspend fun destroy(
        @Query("dateMillis") dateMillis: Long,
        @Query("bodyId") bodyId: Int
    ): DataSourceResponse<BodyRecordData>
}