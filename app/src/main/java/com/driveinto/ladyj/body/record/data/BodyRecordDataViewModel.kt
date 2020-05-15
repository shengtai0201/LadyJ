package com.driveinto.ladyj.body.record.data

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.R
import com.driveinto.ladyj.body.Body
import com.driveinto.ladyj.room.LadyJDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BodyRecordDataViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val bodyRecordDataRepository: BodyRecordDataRepository

    init {
        val database = LadyJDatabase.getDatabase(application)
        val baseUrl = application.resources.getString(R.string.base_url)

        bodyRecordDataRepository = BodyRecordDataRepository(ApiFactory.create(baseUrl), database.bodyRecordDataDao())
    }

    private val body = MutableLiveData<Body>()

    private val dataResult: LiveData<DataSourceResult<List<BodyRecordData>>> = body.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(bodyRecordDataRepository.request(it))
        }
    }

    val data: LiveData<List<BodyRecordData>> = Transformations.switchMap(dataResult) { it.data }
    val message: LiveData<String> = Transformations.switchMap(dataResult) { it.message }

    fun list(body: Body) {
        this.body.postValue(body)
    }

    fun listScrolled(body: Body, visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            viewModelScope.launch(Dispatchers.IO) {
                bodyRecordDataRepository.requestMore(body)
            }
        }
    }

    fun insert(body: Body, bodyRecordData: BodyRecordData) {
        bodyRecordData.bodyId = body.customerId

        viewModelScope.launch(Dispatchers.IO) {
            bodyRecordDataRepository.insert(bodyRecordData)
        }
    }

    fun update(bodyRecordData: BodyRecordData) {
        viewModelScope.launch(Dispatchers.IO) {
            bodyRecordDataRepository.update(bodyRecordData)
        }
    }

    fun delete(bodyRecordData: BodyRecordData) {
        viewModelScope.launch(Dispatchers.IO) {
            bodyRecordDataRepository.delete(bodyRecordData)
        }
    }
}