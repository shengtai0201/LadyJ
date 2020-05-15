package com.driveinto.ladyj.organization

import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface OrganizationApi {
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json")
    @POST("LadyJ/ReadOrganization")
    fun read(@Query("rootUserId") id: String): Call<List<Organization>>
}