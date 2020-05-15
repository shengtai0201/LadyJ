package com.driveinto.ladyj.organization

data class Organization(val destination: User, val source: User) {
    data class User(val id: String, val userName: String)
}