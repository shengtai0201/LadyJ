package com.driveinto.ladyj.login

interface ILoginInfo {
    val loginProvider: String
    val providerKey: String

    val map: HashMap<String, String>
        get() {
            val map = HashMap<String, String>()
            map["loginProvider"] = loginProvider
            map["providerKey"] = providerKey

            return map
        }
}