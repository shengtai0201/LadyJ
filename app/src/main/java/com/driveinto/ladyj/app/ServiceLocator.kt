package com.driveinto.ladyj.app

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.driveinto.ladyj.login.LoginRepository
import java.util.concurrent.Executor

interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        application = context.applicationContext as Application,
                        inMemoryDatabase = false
                    )
                }

                return instance!!
            }
        }

        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getNetworkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getLoginRepository(): LoginRepository

//    fun getCustomerRepository(): CustomerRepository
}