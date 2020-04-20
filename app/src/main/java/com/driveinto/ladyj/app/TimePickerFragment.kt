package com.driveinto.ladyj.app

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import com.driveinto.ladyj.room.Converters
import org.joda.time.DateTime
import java.util.*

class TimePickerFragment(
    private val listener: TimePickerDialog.OnTimeSetListener,
    private val utcMillis: Long? = null
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = if (utcMillis != null) {
            val dateTime = Converters.toDateTime(utcMillis)
            dateTime.toCalendar(Locale.getDefault())
        } else {
            // Use the current time as the master values for the picker
            Calendar.getInstance()
        }

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, listener, hour, minute, DateFormat.is24HourFormat(activity))
    }
}