package com.driveinto.ladyj.app

import android.content.Context
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
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
}
