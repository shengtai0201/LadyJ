package com.driveinto.ladyj.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class LoginInfo(override val loginProvider: String, override val providerKey: String) : ILoginInfo