package com.driveinto.ladyj.app

import android.content.Context
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
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

    override fun onStop() {
        super.onStop()
        hideProgressBar()
    }
}
