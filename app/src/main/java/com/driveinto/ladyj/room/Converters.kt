package com.driveinto.ladyj.room

import android.text.format.DateFormat
import androidx.room.TypeConverter
import com.driveinto.ladyj.customer.CustomerRoles
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {
//    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

//    @TypeConverter
//    fun fromOffsetDateTime(value: String?): OffsetDateTime? {
////        return value?.let {
////            formatter.parse(value, OffsetDateTime::from)
////        }
//        return toOffsetDateTime(value)
//    }
//
//    @TypeConverter
//    fun offsetDateTimeToString(dateTime: OffsetDateTime?): String? {
////        return dateTime?.format(formatter)
//        return Companion.toString(dateTime)
//    }

//    @TypeConverter
//    fun fromCustomerRoles(value: Int?): CustomerRoles? {
////        return value?.let { enumValueOf<CustomerRoles>(it) }
//        // todo ...
//        return CustomerRoles.None
//    }
//
//    @TypeConverter
//    fun customerRolesToInt(role: CustomerRoles?): Int? {
////        return type?.name
//        // todo: ...
//        return 0
//    }

    companion object {
        fun toUTCMillis(year: Int, month: Int, dayOfMonth: Int): Long {
            val date = DateTime(year, month, dayOfMonth, 0, 0, 0)
            val local = DateTimeZone.forTimeZone(TimeZone.getDefault())

            return local.convertLocalToUTC(date.millis, false)
        }

        fun toUTCMillis(): Long{
            val date = DateTime()
            val local = DateTimeZone.forTimeZone(TimeZone.getDefault())

            return local.convertLocalToUTC(date.millis, false)
        }

        fun toDateTime(utcMillis: Long): DateTime {
            val local = DateTimeZone.UTC.convertUTCToLocal(utcMillis)
            return DateTime(local)
        }

        fun toDateString(utcMillis: Long): String {
//            val local = DateTimeZone.UTC.convertUTCToLocal(utcMillis)
//            val dateTime = DateTime(local)
            val dateTime = toDateTime(utcMillis)

            return dateTime.toString("yyyy/MM/dd")
        }
    }
}