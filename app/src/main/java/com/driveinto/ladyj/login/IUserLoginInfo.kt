package com.driveinto.ladyj.login

interface IUserLoginInfo : ILoginInfo {
    val userName: String?
    val phoneNumber: String?

    override val map: HashMap<String, String>
        get() {
            val map = HashMap<String, String>(super.map)

            map["userName"] = userName ?: ""
            map["phoneNumber"] = phoneNumber ?: ""

            return map
        }

    val loginInfo: LoginInfo
        get() = LoginInfo(super.map["loginProvider"]!!, super.map["providerKey"]!!)
}