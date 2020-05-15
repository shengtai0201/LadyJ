package com.driveinto.ladyj.organization

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.R
import com.driveinto.ladyj.login.ILoginResult
import kotlinx.coroutines.Dispatchers

class OrganizationViewModel(application: Application) : AndroidViewModel(application) {

    private val organizationRepository: OrganizationRepository

    init {
        val baseUrl = application.resources.getString(R.string.base_url)

        organizationRepository = OrganizationRepository(ApiFactory.create(baseUrl))
    }

    private val loginResult = MutableLiveData<ILoginResult>()

    private val dataResult: LiveData<DataSourceResult<List<Organization>>> = loginResult.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(organizationRepository.request(it))
        }
    }

    val data: LiveData<List<Organization>> = Transformations.switchMap(dataResult) { it.data }
    val message: LiveData<String> = Transformations.switchMap(dataResult) { it.message }

    fun list(result: ILoginResult) {
        loginResult.postValue(result)
    }
}