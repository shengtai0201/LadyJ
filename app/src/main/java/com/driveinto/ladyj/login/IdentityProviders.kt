package com.driveinto.ladyj.login

enum class IdentityProviders(val providerName: String, val providerId: String, val position: Int) {
    Apple("Apple", "apple.com", 0) {
        override fun toString(): String {
            return providerName
        }
    },
    Facebook("Facebook", "", 1){
        override fun toString(): String {
            return providerName
        }
    },
    Google("Google", "", 2){
        override fun toString(): String {
            return providerName
        }
    },
    Microsoft("Microsoft", "microsoft.com", 3) {
        override fun toString(): String {
            return providerName
        }
    },
    Twitter("Twitter", "twitter.com", 4) {
        override fun toString(): String {
            return providerName
        }
    },
    Yahoo("Yahoo", "yahoo.com", 5) {
        override fun toString(): String {
            return providerName
        }
    };

    companion object {
        private val positions = IdentityProviders.values().associateBy(IdentityProviders::position)
        fun fromPosition(position: Int) = positions[position]
    }
}