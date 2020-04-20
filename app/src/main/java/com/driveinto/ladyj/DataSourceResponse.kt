package com.driveinto.ladyj

data class DataSourceResponse<T>(val dataCollection: List<T>, val errorMessage: String, val totalRowCount: Int)