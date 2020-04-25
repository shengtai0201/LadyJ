package com.driveinto.ladyj.skin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.room.LadyJDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SkinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SkinRepository

    init {
        val database = LadyJDatabase.getDatabase(application)

        repository = SkinRepository(ApiFactory.create("http://10.0.2.2:4915/"), database.skinDao())
    }

    fun modify(skin: Skin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.modify(skin)
        }
    }
}