package com.driveinto.ladyj.body.record

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.body.Body
import com.driveinto.ladyj.room.LadyJDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BodyRecordViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val bodyRecordRepository: BodyRecordRepository

    init {
        val database = LadyJDatabase.getDatabase(application)

        bodyRecordRepository =
            BodyRecordRepository(ApiFactory.create("http://10.0.2.2:4915/"), database.bodyRecordDao())
    }

    private val body = MutableLiveData<Body>()

    private val dataResult: LiveData<DataSourceResult<List<BodyRecord>>> = body.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(bodyRecordRepository.request(it))
        }
    }

    val data: LiveData<List<BodyRecord>> = Transformations.switchMap(dataResult) { it.data }
    val message: LiveData<String> = Transformations.switchMap(dataResult) { it.message }

    fun list(body: Body) {
        this.body.postValue(body)
    }

    fun listScrolled(body: Body, visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            viewModelScope.launch(Dispatchers.IO) {
                bodyRecordRepository.requestMore(body)
            }
        }
    }

    fun insert(body: Body, bodyRecord: BodyRecord) {
        bodyRecord.bodyId = body.customerId

        viewModelScope.launch(Dispatchers.IO) {
            bodyRecordRepository.insert(bodyRecord)
        }
    }

    fun update(bodyRecord: BodyRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            bodyRecordRepository.update(bodyRecord)
        }
    }

    fun delete(bodyRecord: BodyRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            bodyRecordRepository.delete(bodyRecord)
        }
    }
}