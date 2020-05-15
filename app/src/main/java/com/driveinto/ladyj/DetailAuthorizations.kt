package com.driveinto.ladyj

enum class DetailAuthorizations(val value: Int) {
    None(1),
    ReadOnly(2);

    companion object{
        private val values = values().associateBy(DetailAuthorizations::value)
        fun fromValue(value: Int) = values[value]

        const val key = "DETAIL_AUTHORIZATIONS"
    }
}