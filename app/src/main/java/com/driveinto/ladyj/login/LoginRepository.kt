package com.driveinto.ladyj.login

class LoginRepository(private val api: LoginApi) {
    suspend fun login(info: Map<String, String>) = api.login(info)
}