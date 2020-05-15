package com.driveinto.ladyj.organization

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.Repository
import com.driveinto.ladyj.login.ILoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganizationRepository(private val api: OrganizationApi) : Repository() {

    private val data = MutableLiveData<List<Organization>>()

    suspend fun request(result: ILoginResult): DataSourceResult<List<Organization>> {
        api.read(result.id).enqueue(object : Callback<List<Organization>> {
            override fun onFailure(call: Call<List<Organization>>, t: Throwable) {
                message.postValue(t.message ?: "unknown error")
            }

            override fun onResponse(call: Call<List<Organization>>, response: Response<List<Organization>>) {
                if (response.isSuccessful) {
                    val organizations = response.body()
                    if (organizations == null) {
                        Log.i(LOG_TAG, "ReadOrganization 資料為空")
                    } else {
                        data.postValue(organizations)
                    }
                } else {
                    message.postValue(response.errorBody()?.string() ?: "unknown error")
                }
            }
        })

        return DataSourceResult(data, message)
    }

    companion object {
        private const val LOG_TAG = "OrganizationRepository"
    }
}