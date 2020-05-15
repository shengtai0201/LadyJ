package com.driveinto.ladyj.login

data class LoginInfo(override val loginProvider: String, override val providerKey: String) : ILoginInfo