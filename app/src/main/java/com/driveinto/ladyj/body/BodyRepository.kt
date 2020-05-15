package com.driveinto.ladyj.body

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.driveinto.ladyj.DataSourceResponse
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.customer.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class BodyRepository(private val api: BodyApi, private val dao: BodyDao) {

    // LiveData of network errors.
    private val message = MutableLiveData<String>()
    private val data = MutableLiveData<Body>()

    suspend fun read(customerId: Int): DataSourceResult<Body> {
        api.read(customerId).enqueue(object : Callback<DataSourceResponse<Body>> {
            override fun onFailure(call: Call<DataSourceResponse<Body>>, t: Throwable) {
                message.postValue(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<DataSourceResponse<Body>>,
                response: Response<DataSourceResponse<Body>>
            ) {
                if (response.isSuccessful) {
                    val serverBody = response.body()?.dataCollection?.first()

                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute {
                        val clientBody = dao.query(customerId)
                        if (clientBody == null) {
                            data.postValue(Body.emptyInstance(customerId))
                        }

                        // disk io
                        GlobalScope.launch(Dispatchers.IO) {
                            if (serverBody != null) {
                                if (clientBody != null) {
                                    // 遠端存在，本地存在
                                    if (clientBody.dirty != false) {
                                        clientBody.dirty = false
                                        dao.updateAsync(clientBody)
                                    }

                                    data.postValue(clientBody)
                                } else {
                                    // 遠端存在，本地不存在
                                    Log.e(LOG_TAG, "遠端存在，本地不存在：${customerId}")
                                }
                            } else {
                                if (clientBody != null) {
                                    // 遠端不存在，本地存在，有可能新增或修改尚未完成或失敗
                                    Log.w(LOG_TAG, "本地存在，遠端不存在，有可能新增或修改尚未完成或失敗：${customerId}")

                                    data.postValue(clientBody)
                                } else {
                                    // 遠端不存在，本地不存在，有可能尚未寫入本地資料庫
                                    Log.i(LOG_TAG, "本地不存在，遠端不存在，有可能尚未寫入本地資料庫：${customerId}")
                                }
                            }
                        }
                    }
                } else {
                    message.postValue(response.errorBody()?.string() ?: "unknown error")

                    data.postValue(Body.emptyInstance(customerId))
                }
            }
        })

        return DataSourceResult(data, message)
    }

    suspend fun modify(body: Body) {
        val executor = Executors.newSingleThreadExecutor()
        val clientBody = dao.query(body.customerId)

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                if (clientBody == null) {
                    // 新增
                    dao.insertAsync(body)
                } else {
                    // 修改
                    dao.updateAsync(body)
                }
            }
        }
        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                api.modify(body.customerId, body.getMap())
            }
        }

        executor.shutdown()
    }

    companion object {
        private const val LOG_TAG = "BodyRepository"
    }
}