package com.driveinto.ladyj.login

interface ILoginResult {
    val id: String

    val requestMap: HashMap<String, String>
        get() {
            val map = HashMap<String, String>()
            map["filter[logic]"] = "and"
            map["filter[filters][0][field]"] = "AdviserId"
            map["filter[filters][0][operator]"] = "eq"
            map["filter[filters][0][value]"] = id

            return map
        }
}