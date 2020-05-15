package com.driveinto.ladyj.body.data

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.R
import com.driveinto.ladyj.body.Body
import com.driveinto.ladyj.room.LadyJDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BodyDataViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val bodyDataRepository: BodyDataRepository

    init {
        val database = LadyJDatabase.getDatabase(application)
        val baseUrl = application.resources.getString(R.string.base_url)

        bodyDataRepository = BodyDataRepository(ApiFactory.create(baseUrl), database.bodyDataDao())
    }

    private val body = MutableLiveData<Body>()

    //    private val dataResult: LiveData<DataSourceResult<List<BodyData>>> = body.switchMap {
//        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
//            emit(bodyDataRepository.request(it.requestMap))
//        }
//    }
    private val dataResult: LiveData<DataSourceResult<List<BodyData>>> = body.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(bodyDataRepository.request(it))
        }
    }

    val data: LiveData<List<BodyData>> = Transformations.switchMap(dataResult) { it.data }
    val message: LiveData<String> = Transformations.switchMap(dataResult) { it.message }

    fun list(body: Body) {
        this.body.postValue(body)
    }

//    fun listScrolled(body: Body, visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
//        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
//            viewModelScope.launch(Dispatchers.IO) {
//                bodyDataRepository.requestMore(body.requestMap)
//            }
//        }
//    }
    fun listScrolled(body: Body, visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            viewModelScope.launch(Dispatchers.IO) {
                bodyDataRepository.requestMore(body)
            }
        }
    }

    fun insert(body: Body, bodyData: BodyData) {
        bodyData.bodyId = body.customerId

        viewModelScope.launch(Dispatchers.IO) {
            bodyDataRepository.insert(bodyData)
        }
    }

    fun update(bodyData: BodyData) {
        viewModelScope.launch(Dispatchers.IO) {
            bodyDataRepository.update(bodyData)
        }
    }

    fun delete(bodyData: BodyData) {
        viewModelScope.launch(Dispatchers.IO) {
            bodyDataRepository.delete(bodyData)
        }
    }
}