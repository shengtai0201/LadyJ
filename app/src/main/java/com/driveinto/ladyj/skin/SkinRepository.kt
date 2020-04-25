package com.driveinto.ladyj.skin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.driveinto.ladyj.DataSourceResponse
import com.driveinto.ladyj.DataSourceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class SkinRepository(private val api: SkinApi, private val dao: SkinDao) {
    // LiveData of network errors.
    private val message = MutableLiveData<String>()
    private val data = MutableLiveData<Skin>()

    suspend fun read(customerId: Int): DataSourceResult<Skin> {
        api.read(customerId).enqueue(object : Callback<DataSourceResponse<Skin>> {
            override fun onFailure(call: Call<DataSourceResponse<Skin>>, t: Throwable) {
                message.postValue(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<DataSourceResponse<Skin>>,
                response: Response<DataSourceResponse<Skin>>
            ) {
                if (response.isSuccessful) {
                    val serverSkin = response.body()?.dataCollection?.first()

                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute {
                        val clientSkin = dao.query(customerId)
                        if (clientSkin == null) {
                            data.postValue(Skin.emptyInstance(customerId))
                        }

                        // disk io
                        GlobalScope.launch(Dispatchers.IO) {
                            if (serverSkin != null) {
                                if (clientSkin != null) {
                                    // 遠端存在，本地存在
                                    if (clientSkin.dirty != false) {
                                        clientSkin.dirty = false
                                        dao.updateAsync(clientSkin)
                                    }

                                    data.postValue(clientSkin)
                                } else {
                                    // 遠端存在，本地不存在
                                    Log.e(LOG_TAG, "遠端存在，本地不存在：${customerId}")
                                }
                            } else {
                                if (clientSkin != null) {
                                    // 遠端不存在，本地存在，有可能新增或修改尚未完成或失敗
                                    Log.w(LOG_TAG, "本地存在，遠端不存在，有可能新增或修改尚未完成或失敗：${customerId}")

                                    data.postValue(clientSkin)
                                } else {
                                    // 遠端不存在，本地不存在，有可能尚未寫入本地資料庫
                                    Log.i(LOG_TAG, "本地不存在，遠端不存在，有可能尚未寫入本地資料庫：${customerId}")
                                }
                            }
                        }
                    }
                } else {
                    message.postValue(response.errorBody()?.string() ?: "unknown error")

                    data.postValue(Skin.emptyInstance(customerId))
                }
            }
        })

        return DataSourceResult(data, message)
    }

    suspend fun modify(skin: Skin) {
        val executor = Executors.newSingleThreadExecutor()
        val clientSkin = dao.query(skin.customerId)

        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                if (clientSkin == null) {
                    // 新增
                    dao.insertAsync(skin)
                } else {
                    // 修改
                    dao.updateAsync(skin)
                }
            }
        }
        executor.submit {
            GlobalScope.launch(Dispatchers.IO) {
                api.modify(skin.customerId, skin.getMap())
            }
        }

        executor.shutdown()
    }

    companion object {
        private const val LOG_TAG = "SkinRepository"
    }
}