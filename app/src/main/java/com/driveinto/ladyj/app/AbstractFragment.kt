package com.driveinto.ladyj.app

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.edit
import com.driveinto.ladyj.login.ILoginInfo
import com.driveinto.ladyj.login.LoginResponse
import com.driveinto.ladyj.login.LoginResult
import com.driveinto.ladyj.login.UserLoginInfo
import com.google.android.material.snackbar.Snackbar

abstract class AbstractFragment : Fragment() {

    private var progressBar: ProgressBar? = null

    fun setProgressBar(resId: Int) {
        activity?.run {
            progressBar = this.findViewById(resId)
        }
    }

    fun setProgressBar(progressBar: ProgressBar) {
        this.progressBar = progressBar
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    fun hideKeyboard(view: View) {
        activity?.run {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showToast(message: String) {
        activity?.run {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    protected fun setText(view: EditText, value: Int?) {
        if (value == null) {
            view.text = null
        } else {
            view.setText(value.toString())
        }
    }

    protected fun setText(view: EditText, value: String?) {
        if (value == null) {
            view.text = null
        } else {
            view.setText(value)
        }
    }

    protected fun setText(view: Button, value: Long?, func: (value: Long) -> String) {
        if (value == null) {
            view.text = null
        } else {
            view.text = func(value)
        }
    }

    protected fun setInt(view: EditText, action: (value: Int) -> Unit) {
        val text = view.text.toString()
        if (text.isNotEmpty()) {
            action(text.toInt())
        }
    }

    protected fun setString(view: EditText, action: (value: String) -> Unit) {
        val text = view.text.toString()
        if (text.isNotEmpty()) {
            action(text)
        }
    }

    override fun onStop() {
        super.onStop()
        hideProgressBar()
    }

    private var loginSharedPreferences: SharedPreferences? = null

    protected fun isLoginInfoNullOrEmpty(): Boolean {
        if (loginSharedPreferences == null) {
            loginSharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN_NAME, Context.MODE_PRIVATE)
        }

        val loginProvider = loginSharedPreferences!!.getString(SHARED_PREFERENCES_LOGIN_PROVIDER_NAME, null)
        val providerKey = loginSharedPreferences!!.getString(SHARED_PREFERENCES_PROVIDER_KEY_NAME, null)

        return loginProvider.isNullOrEmpty() || providerKey.isNullOrEmpty()
    }

    protected fun equalsLoginInfo(info: ILoginInfo): Boolean {
        if (loginSharedPreferences == null) {
            loginSharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN_NAME, Context.MODE_PRIVATE)
        }

        val loginProvider = loginSharedPreferences!!.getString(SHARED_PREFERENCES_LOGIN_PROVIDER_NAME, null)
        val providerKey = loginSharedPreferences!!.getString(SHARED_PREFERENCES_PROVIDER_KEY_NAME, null)
        if (loginProvider.isNullOrEmpty() || providerKey.isNullOrEmpty()) {
            return false
        }

        return info.loginProvider == loginProvider && info.providerKey == providerKey
    }

    protected fun setLoginInfo(info: ILoginInfo) {
        if (loginSharedPreferences == null) {
            loginSharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN_NAME, Context.MODE_PRIVATE)
        }

        loginSharedPreferences!!.edit {
            this.putString(SHARED_PREFERENCES_LOGIN_PROVIDER_NAME, info.loginProvider)
            this.putString(SHARED_PREFERENCES_PROVIDER_KEY_NAME, info.providerKey)
            this.apply()
        }
    }

    protected fun isLoginResultNullOrEmpty(): Boolean {
        if (loginSharedPreferences == null) {
            loginSharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN_NAME, Context.MODE_PRIVATE)
        }

        val id = loginSharedPreferences!!.getString(SHARED_PREFERENCES_ID_NAME, null)
        val role = loginSharedPreferences!!.getInt(SHARED_PREFERENCES_ROLE_NAME, 0)

        return id.isNullOrEmpty() || role == 0
    }

    protected fun getLoginResult(): LoginResult? {
        if (loginSharedPreferences == null) {
            loginSharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN_NAME, Context.MODE_PRIVATE)
        }

        val id = loginSharedPreferences!!.getString("LoginResult.id", null)
        val role = loginSharedPreferences!!.getInt("LoginResult.role", 0)
        if (id.isNullOrEmpty() || role == 0) {
            return null
        }

        return LoginResult(id, role)
    }

    protected fun setLoginResult(result: LoginResult) {
        if (loginSharedPreferences == null) {
            loginSharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN_NAME, Context.MODE_PRIVATE)
        }

        val id = loginSharedPreferences!!.getString(SHARED_PREFERENCES_ID_NAME, null)
        val role = loginSharedPreferences!!.getInt(SHARED_PREFERENCES_ROLE_NAME, 0)

        if (result.id != id || result.role != role) {
            loginSharedPreferences!!.edit {
                this.putString(SHARED_PREFERENCES_ID_NAME, result.id)
                this.putInt(SHARED_PREFERENCES_ROLE_NAME, result.role)
                this.apply()
            }
        }
    }

    protected fun signOut() {
        if (loginSharedPreferences == null) {
            loginSharedPreferences =
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN_NAME, Context.MODE_PRIVATE)
        }

        loginSharedPreferences!!.edit {
            this.remove(SHARED_PREFERENCES_LOGIN_PROVIDER_NAME)
            this.remove(SHARED_PREFERENCES_PROVIDER_KEY_NAME)
            this.remove(SHARED_PREFERENCES_ID_NAME)
            this.remove(SHARED_PREFERENCES_ROLE_NAME)
            this.apply()
        }
    }

    companion object {
        private const val SHARED_PREFERENCES_LOGIN_NAME = "Login"
        private const val SHARED_PREFERENCES_LOGIN_PROVIDER_NAME = "LoginInfo.loginProvider"
        private const val SHARED_PREFERENCES_PROVIDER_KEY_NAME = "LoginInfo.providerKey"
        private const val SHARED_PREFERENCES_ID_NAME = "LoginResult.id"
        private const val SHARED_PREFERENCES_ROLE_NAME = "LoginResult.role"
    }
}
