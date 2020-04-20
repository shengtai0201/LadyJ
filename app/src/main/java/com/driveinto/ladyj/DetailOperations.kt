package com.driveinto.ladyj

enum class DetailOperations(val value: Int) {
    None(1),
    Create(2),
    Read(4),
    Update(8),
    Destroy(16);

    companion object{
        private val values = values().associateBy(DetailOperations::value)
        fun fromValue(value: Int) = values[value]

        const val key = "DETAIL_OPERATIONS"
    }
}