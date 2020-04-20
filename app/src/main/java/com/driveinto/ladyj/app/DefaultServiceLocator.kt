package com.driveinto.ladyj.app

import android.app.Application
import com.driveinto.ladyj.ApiFactory
import com.driveinto.ladyj.login.LoginApi
import com.driveinto.ladyj.login.LoginRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DefaultServiceLocator(val application: Application, val inMemoryDatabase: Boolean) : ServiceLocator {
    // thread pool used for network requests
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    override fun getNetworkExecutor(): Executor = NETWORK_IO

    // thread pool used for disk access
    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    override fun getDiskIOExecutor(): Executor = DISK_IO

    private val _loginApi by lazy { ApiFactory.create<LoginApi>("http://10.0.2.2:4915/") }
    override fun getLoginRepository(): LoginRepository = LoginRepository(_loginApi)

//    private val _customerApi by lazy { ApiFactory.create<CustomerApi>("http://10.0.2.2:4915/") }
//    override fun getCustomerRepository(): CustomerRepository = CustomerRepository(_customerApi)
}