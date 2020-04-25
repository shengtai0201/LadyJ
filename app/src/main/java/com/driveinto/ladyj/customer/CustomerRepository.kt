package com.driveinto.ladyj.customer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.driveinto.ladyj.DataSourceResponse
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.Repository
import com.driveinto.ladyj.login.ILoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class CustomerRepository(private val api: CustomerApi, private val dao: CustomerDao) : Repository() {

//    suspend fun request(result: Map<String, String>): DataSourceResult<List<Customer>> {
//        lastRequestedPage = 1
//        requestMore(result)
//
//        // Get data from the local cache
//        val data = dao.queryAsync()
//
//        return DataSourceResult(data, message)
//    }
    suspend fun request(result: ILoginResult): DataSourceResult<List<Customer>> {
        lastRequestedPage = 1
        requestMore(result)

        // Get data from the local cache
        val data = dao.queryAsync()

        return DataSourceResult(data, message)
    }

    // 首次開啟，會執行兩次，第二次若網路資料為空，本地查詢依然正常，是因為伺服器資料庫於第二頁確實沒資料
//    suspend fun requestMore(result: Map<String, String>) {
//        if (isRequestInProgress) return
//
//        isRequestInProgress = true
//        val request = getRequestMap(result)
//        api.read(request).enqueue(object : Callback<DataSourceResponse<Customer>> {
//            override fun onFailure(call: Call<DataSourceResponse<Customer>>, t: Throwable) {
//                onError(t.message ?: "unknown error")
//            }
//
//            override fun onResponse(
//                call: Call<DataSourceResponse<Customer>>,
//                response: Response<DataSourceResponse<Customer>>
//            ) {
//                if (response.isSuccessful) {
//                    val serverCustomers = (response.body()?.dataCollection ?: emptyList()).map { it.id to it }.toMap()
//
//                    val executor = Executors.newSingleThreadExecutor()
//                    executor.execute {
//                        val clientCustomers = dao.query().map { it.id to it }.toMap()
//
//                        // disk io
//                        GlobalScope.launch(Dispatchers.IO) {
//                            serverCustomers.forEach {
//                                if (clientCustomers.containsKey(it.key)) {
//                                    // 遠端存在，本地存在
//                                    val clientCustomer = clientCustomers[it.key]
//                                    if (clientCustomer != null && clientCustomer.dirty != false) {
//                                        clientCustomer.dirty = false
//                                        dao.updateAsync(clientCustomer)
//                                    }
//                                } else {
//                                    // 遠端存在，本地不存在
//                                    Log.e(LOG_TAG, "遠端存在，本地不存在：${it.value.id}")
//                                }
//                            }
//
//                            loop@ for (customer in clientCustomers) {
//                                if (serverCustomers.containsKey(customer.key)) {
//                                    // 本地存在，遠端存在，略過
//                                    continue@loop
//                                } else {
//                                    // 本地存在，遠端不存在，但有可能因換頁查詢而資料不齊全
//                                    Log.w(LOG_TAG, "本地存在，遠端不存在，但有可能因換頁查詢而資料不齊全：${customer.key}")
//                                }
//                            }
//
//                            lastRequestedPage++
//                            isRequestInProgress = false
//                        }
//                    }
//                } else {
//                    onError(response.errorBody()?.string() ?: "unknown error")
//                }
//            }
//        })
//    }
    suspend fun requestMore(result: ILoginResult) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        val request = getRequestMap(result.requestMap)
        api.read(request).enqueue(object : Callback<DataSourceResponse<Customer>> {
            override fun onFailure(call: Call<DataSourceResponse<Customer>>, t: Throwable) {
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<DataSourceResponse<Customer>>,
                response: Response<DataSourceResponse<Customer>>
            ) {
                if (response.isSuccessful) {
                    val serverCustomers = (response.body()?.dataCollection ?: emptyList()).map { it.id to it }.toMap()

                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute {
                        val clientCustomers = dao.query().map { it.id to it }.toMap()

                        // disk io
                        GlobalScope.launch(Dispatchers.IO) {
                            serverCustomers.forEach {
                                if (clientCustomers.containsKey(it.key)) {
                                    // 遠端存在，本地存在
                                    val clientCustomer = clientCustomers[it.key]
                                    if (clientCustomer != null && clientCustomer.dirty != false) {
                                        clientCustomer.dirty = false
                                        dao.updateAsync(clientCustomer)
                                    }
                                } else {
                                    // 遠端存在，本地不存在
                                    Log.e(LOG_TAG, "遠端存在，本地不存在：${it.value.id}")
                                }
                            }

                            loop@ for (customer in clientCustomers) {
                                if (serverCustomers.containsKey(customer.key)) {
                                    // 本地存在，遠端存在，略過
                                    continue@loop
                                } else {
                                    // 本地存在，遠端不存在，但有可能因換頁查詢而資料不齊全
                                    Log.w(LOG_TAG, "本地存在，遠端不存在，但有可能因換頁查詢而資料不齊全：${customer.key}")
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

    suspend fun insert(customer: Customer) {
        val executor = Executors.newSingleThreadExecutor()

        val daoFuture = executor.submit(Callable {
            return@Callable GlobalScope.async(Dispatchers.IO) {
                dao.insertAsync(customer)
            }
        })

        val apiFuture = executor.submit(Callable {
            return@Callable GlobalScope.async(Dispatchers.IO) {
                val rowId = daoFuture.get().await()
                val map = customer.getMap(rowId)
                val response = api.create(map)
                response.dataCollection.first()
            }
        })

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                val serverCustomer = apiFuture.get().await()
                dao.updateAsync(serverCustomer)
            }
        }

        executor.shutdown()
    }

    suspend fun update(customer: Customer) {
        val executor = Executors.newSingleThreadExecutor()

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                dao.updateAsync(customer)
            }
        }
        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                api.update(customer.id, customer.getMap())
            }
        }

        executor.shutdown()
    }

    suspend fun delete(customer: Customer) {
        val executor = Executors.newSingleThreadExecutor()

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                // todo: 資料刪除連動
                dao.deleteAsync(customer)
            }
        }
        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                api.destroy(customer.id)
            }
        }

        executor.shutdown()
    }
}