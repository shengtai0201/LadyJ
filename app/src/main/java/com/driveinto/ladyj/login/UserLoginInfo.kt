package com.driveinto.ladyj.login

import com.google.firebase.auth.FirebaseUser

data class UserLoginInfo(
    override val loginProvider: String,
    override val providerKey: String,
    override val userName: String?,
    override val phoneNumber: String?
) : IUserLoginInfo {
    companion object {
        fun newInstance(user: FirebaseUser?): UserLoginInfo? {
            return if (user != null)
                UserLoginInfo(
                    user.providerId,
                    user.uid,
                    user.displayName,
                    user.phoneNumber
                )
            else
                null
        }
    }
}
