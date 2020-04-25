package com.driveinto.ladyj.body.data

import android.util.Log
import com.driveinto.ladyj.DataSourceResponse
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.Repository
import com.driveinto.ladyj.body.Body
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class BodyDataRepository(private val api: BodyDataApi, private val dao: BodyDataDao) : Repository() {

    suspend fun request(body: Body): DataSourceResult<List<BodyData>> {
        lastRequestedPage = 1
        requestMore(body)

        // Get data from the local cache
        val data = dao.queryAsync(body.customerId)

        return DataSourceResult(data, message)
    }

    // 首次開啟，會執行兩次，第二次若網路資料為空，本地查詢依然正常，是因為伺服器資料庫於第二頁確實沒資料
    suspend fun requestMore(body: Body) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        val request = getRequestMap(body.requestMap)
        api.read(request).enqueue(object : Callback<DataSourceResponse<BodyData>> {
            override fun onFailure(call: Call<DataSourceResponse<BodyData>>, t: Throwable) {
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<DataSourceResponse<BodyData>>,
                response: Response<DataSourceResponse<BodyData>>
            ) {
                if (response.isSuccessful) {
                    val servers = (response.body()?.dataCollection ?: emptyList()).map { it.keys() to it }.toMap()

                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute {
                        val clients = dao.query(body.customerId).map { it.keys() to it }.toMap()

                        // disk io
                        GlobalScope.launch(Dispatchers.IO) {
                            servers.forEach {
                                if (clients.containsKey(it.key)) {
                                    // 遠端存在，本地存在
                                    val client = clients[it.key]
                                    if (client != null && client.dirty != false) {
                                        client.dirty = false
                                        dao.updateAsync(client)
                                    }
                                } else {
                                    // 遠端存在，本地不存在
                                    Log.e(LOG_TAG, "遠端存在，本地不存在：${it.value.keys()}")
                                }
                            }

                            loop@ for (client in clients) {
                                if (servers.containsKey(client.key)) {
                                    // 本地存在，遠端存在，略過
                                    continue@loop
                                } else {
                                    // 本地存在，遠端不存在，但有可能因換頁查詢而資料不齊全
                                    Log.w(LOG_TAG, "本地存在，遠端不存在，但有可能因換頁查詢而資料不齊全：${client.key}")
                                }
                            }

                            lastRequestedPage++
                            isRequestInProgress = false
                        }
                    }
                } else {
                    onError(response.errorBody()?.string() ?: "unknown error")
                }
            }
        })
    }

    suspend fun insert(bodyData: BodyData) {
        val executor = Executors.newSingleThreadExecutor()

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                dao.insertAsync(bodyData)
            }
        }
        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                api.create(bodyData.getMap())
            }
        }

        executor.shutdown()
    }

    suspend fun update(bodyData: BodyData) {
        val executor = Executors.newSingleThreadExecutor()

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                dao.updateAsync(bodyData)
            }
        }
        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                api.update(bodyData.dateMillis, bodyData.bodyId, bodyData.getMap())
            }
        }

        executor.shutdown()
    }

    suspend fun delete(bodyData: BodyData) {
        val executor = Executors.newSingleThreadExecutor()

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteAsync(bodyData)
            }
        }
        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                api.destroy(bodyData.dateMillis, bodyData.bodyId)
            }
        }

        executor.shutdown()
    }
}