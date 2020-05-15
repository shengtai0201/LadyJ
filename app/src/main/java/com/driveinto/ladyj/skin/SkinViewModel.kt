package com.driveinto.ladyj.skin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.R
import com.driveinto.ladyj.room.LadyJDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SkinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SkinRepository

    init {
        val database = LadyJDatabase.getDatabase(application)
        val baseUrl = application.resources.getString(R.string.base_url)

        repository = SkinRepository(ApiFactory.create(baseUrl), database.skinDao())
    }

    fun modify(skin: Skin) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.modify(skin)
        }
    }
}