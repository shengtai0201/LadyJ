package com.driveinto.ladyj.app

import android.app.Dialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.driveinto.ladyj.room.Converters
import java.util.*

class DatePickerFragment(
    private val listener: DatePickerDialog.OnDateSetListener,
    private val utcMillis: Long? = null
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = if (utcMillis != null) {
            val dateTime = Converters.toDateTime(utcMillis)
            dateTime.toCalendar(Locale.getDefault())
        } else {
            // Use the current date as the master date in the picker
            Calendar.getInstance()
        }

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), listener, year, month, day)
    }
}