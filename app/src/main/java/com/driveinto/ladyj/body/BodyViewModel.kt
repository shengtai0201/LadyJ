package com.driveinto.ladyj.body

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.room.LadyJDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BodyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BodyRepository

    init {
        val database = LadyJDatabase.getDatabase(application)

        repository = BodyRepository(ApiFactory.create("http://10.0.2.2:4915/"), database.bodyDao())
    }

    fun modify(body: Body) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.modify(body)
        }
    }
}