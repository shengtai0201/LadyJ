package com.driveinto.ladyj.skin.data

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.R
import com.driveinto.ladyj.room.LadyJDatabase
import com.driveinto.ladyj.skin.Skin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SkinDataViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val skinDataRepository: SkinDataRepository

    init {
        val database = LadyJDatabase.getDatabase(application)
        val baseUrl = application.resources.getString(R.string.base_url)

        skinDataRepository = SkinDataRepository(ApiFactory.create(baseUrl), database.skinDataDao())
    }

    private val skin = MutableLiveData<Skin>()

    private val dataResult: LiveData<DataSourceResult<List<SkinData>>> = skin.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(skinDataRepository.request(it))
        }
    }

    val data: LiveData<List<SkinData>> = Transformations.switchMap(dataResult) { it.data }
    val message: LiveData<String> = Transformations.switchMap(dataResult) { it.message }

    fun list(skin: Skin) {
        this.skin.postValue(skin)
    }

    fun listScrolled(skin: Skin, visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            viewModelScope.launch(Dispatchers.IO) {
                skinDataRepository.requestMore(skin)
            }
        }
    }

    fun insert(skin: Skin, skinData: SkinData) {
        skinData.skinId = skin.customerId

        viewModelScope.launch(Dispatchers.IO) {
            skinDataRepository.insert(skinData)
        }
    }

    fun update(skinData: SkinData) {
        viewModelScope.launch(Dispatchers.IO) {
            skinDataRepository.update(skinData)
        }
    }

    fun delete(skinData: SkinData) {
        viewModelScope.launch(Dispatchers.IO) {
            skinDataRepository.delete(skinData)
        }
    }
}