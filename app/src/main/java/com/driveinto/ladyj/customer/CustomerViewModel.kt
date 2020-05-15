package com.driveinto.ladyj.customer

import android.app.Application
import androidx.lifecycle.*
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.DataSourceResult
import com.driveinto.ladyj.R
import com.driveinto.ladyj.body.Body
import com.driveinto.ladyj.body.BodyRepository
import com.driveinto.ladyj.room.LadyJDatabase
import com.driveinto.ladyj.login.ILoginResult
import com.driveinto.ladyj.skin.Skin
import com.driveinto.ladyj.skin.SkinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val customerRepository: CustomerRepository
    private val bodyRepository: BodyRepository
    private val skinRepository: SkinRepository

    init {
        val database = LadyJDatabase.getDatabase(application)
        val baseUrl = application.resources.getString(R.string.base_url)

        customerRepository = CustomerRepository(ApiFactory.create(baseUrl), database.customerDao())
        bodyRepository = BodyRepository(ApiFactory.create(baseUrl), database.bodyDao())
        skinRepository = SkinRepository(ApiFactory.create(baseUrl), database.skinDao())
    }

    private val loginResult = MutableLiveData<ILoginResult>()
    private val dataResult: LiveData<DataSourceResult<List<Customer>>> = loginResult.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(customerRepository.request(it))
        }
    }

    val data: LiveData<List<Customer>> = Transformations.switchMap(dataResult) { it.data }
    val message: LiveData<String> = Transformations.switchMap(dataResult) { it.message }

    fun list(result: ILoginResult) {
        loginResult.postValue(result)
    }

    fun listScrolled(result: ILoginResult, visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            viewModelScope.launch(Dispatchers.IO) {
                customerRepository.requestMore(result)
            }
        }
    }

    fun insert(result: ILoginResult, customer: Customer) {
        customer.adviserId = result.id

        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.insert(customer)
        }
    }

    fun update(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.update(customer)
        }
    }

    fun delete(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.delete(customer)
        }
    }

    private val bodyCustomer = MutableLiveData<Customer>()
    fun readBody(customer: Customer) {
        this.bodyCustomer.postValue(customer)
    }

    private val bodyResult: LiveData<DataSourceResult<Body>> = bodyCustomer.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(bodyRepository.read(it.id))
        }
    }
    val body: LiveData<Body> = Transformations.switchMap(bodyResult) { it.data }

    private val skinCustomer = MutableLiveData<Customer>()
    fun readSkin(customer: Customer) {
        this.skinCustomer.postValue(customer)
    }

    private val skinResult: LiveData<DataSourceResult<Skin>> = skinCustomer.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(skinRepository.read(it.id))
        }
    }
    val skin: LiveData<Skin> = Transformations.switchMap(skinResult) { it.data }
}
