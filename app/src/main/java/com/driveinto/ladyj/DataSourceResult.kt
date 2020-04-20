package com.driveinto.ladyj

import androidx.lifecycle.LiveData

data class DataSourceResult<T>(val data: LiveData<T>, val message: LiveData<String>)