package com.driveinto.ladyj.skin.record

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.R
import com.driveinto.ladyj.body.Body
import com.driveinto.ladyj.room.LadyJDatabase
import com.driveinto.ladyj.skin.Skin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SkinRecordViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val skinRecordRepository: SkinRecordRepository

    init {
        val database = LadyJDatabase.getDatabase(application)
        val baseUrl = application.resources.getString(R.string.base_url)

        skinRecordRepository = SkinRecordRepository(ApiFactory.create(baseUrl), database.skinRecordDao())
    }

    private val skin = MutableLiveData<Skin>()

    private val dataResult: LiveData<DataSourceResult<List<SkinRecord>>> = skin.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(skinRecordRepository.request(it))
        }
    }

    val data: LiveData<List<SkinRecord>> = Transformations.switchMap(dataResult) { it.data }
    val message: LiveData<String> = Transformations.switchMap(dataResult) { it.message }

    fun list(skin: Skin) {
        this.skin.postValue(skin)
    }

    fun listScrolled(skin: Skin, visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            viewModelScope.launch(Dispatchers.IO) {
                skinRecordRepository.requestMore(skin)
            }
        }
    }

    fun insert(skin: Skin, skinRecord: SkinRecord) {
        skinRecord.skinId = skin.customerId

        viewModelScope.launch(Dispatchers.IO) {
            skinRecordRepository.insert(skinRecord)
        }
    }

    fun update(skinRecord: SkinRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            skinRecordRepository.update(skinRecord)
        }
    }

    fun delete(skinRecord: SkinRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            skinRecordRepository.delete(skinRecord)
        }
    }
}