package com.driveinto.ladyj

import androidx.lifecycle.MutableLiveData

abstract class Repository {

    // keep the last requested page. When the request is successful, increment the page number.
    protected var lastRequestedPage = 1

    // LiveData of network errors.
    protected val message = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    protected var isRequestInProgress = false

    protected fun onError(error: String) {
        message.postValue(error)
        isRequestInProgress = false
    }

    protected fun getRequestMap(map: Map<String, String>): Map<String, String> {
        val skip = (NETWORK_PAGE_SIZE * (lastRequestedPage - 1)).coerceAtLeast(0)
        val request = HashMap<String, String>(map)
        request["take"] = NETWORK_PAGE_SIZE.toString()
        request["skip"] = skip.toString()
        request["page"] = lastRequestedPage.toString()
        request["pageSize"] = NETWORK_PAGE_SIZE.toString()

        return request
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
        const val LOG_TAG = "Repository"
    }
}