package com.driveinto.ladyj.login

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _userLoginInfo = MutableLiveData<UserLoginInfo?>()
    val userLoginInfo: LiveData<UserLoginInfo?> = _userLoginInfo
    fun signedIn(user: FirebaseUser?) {
        val info = UserLoginInfo.newInstance(user)
        _userLoginInfo.postValue(info)
    }

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse
    fun login(info: IUserLoginInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val retrieved = repository.login(info.map)
//            val result = retrieved.result
            _loginResponse.postValue(retrieved)
        }
    }
}