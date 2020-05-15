package com.driveinto.ladyj.body

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.R
import com.driveinto.ladyj.room.LadyJDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BodyViewModel(application: Application) : AndroidViewModel(application) {

    private val bodyRepository: BodyRepository

    init {
        val database = LadyJDatabase.getDatabase(application)
        val baseUrl = application.resources.getString(R.string.base_url)

        bodyRepository = BodyRepository(ApiFactory.create(baseUrl), database.bodyDao())
    }

    fun modify(body: Body) {
        viewModelScope.launch(Dispatchers.IO) {
            bodyRepository.modify(body)
        }
    }
}