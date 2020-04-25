package com.driveinto.ladyj.customer

import com.driveinto.ladyj.DataSourceResponse
import retrofit2.Call
import retrofit2.http.*

interface CustomerApi {
//    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
//    @GET("api/Customers")
//    suspend fun read(@QueryMap request: Map<String, String>): DataSourceResponse<Customer>

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @GET("api/Customers")
    fun read(@QueryMap request: Map<String, String>): Call<DataSourceResponse<Customer>>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @PUT("api/Customers")
    suspend fun update(@Query("key") id: Int, @FieldMap customer: Map<String, String>): DataSourceResponse<Customer>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("api/Customers")
    suspend fun create(@FieldMap customer: Map<String, String>): DataSourceResponse<Customer>

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @DELETE("api/Customers")
    suspend fun destroy(@Query("key") id: Int): DataSourceResponse<Customer>

//    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
//    @GET("api/Customers")
//    suspend fun read(@Query("key") id: Int): DataSourceResponse<Customer>
}