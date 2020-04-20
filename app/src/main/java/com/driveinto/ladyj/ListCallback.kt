package com.driveinto.ladyj

interface ListCallback<T> {
    fun onItemChanging(entity: T?, operation: DetailOperations)
}