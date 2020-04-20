package com.driveinto.ladyj.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResult(override val id: String, val role: Int) : Parcelable, ILoginResult {
    companion object {
        // 與 navigation 之參數名稱相同
        const val nameKey = "loginResult"
    }
}