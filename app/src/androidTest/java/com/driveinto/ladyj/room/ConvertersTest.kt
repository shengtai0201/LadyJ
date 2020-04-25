package com.driveinto.ladyj.room

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test
import java.util.*

internal class ConvertersTest {

    @Test
    fun test1() {
        val date = DateTime(1970, 1, 1, 0, 0, 0)
        val local = DateTimeZone.forTimeZone(TimeZone.getDefault())
        val millis = date.millis
        val result = local.convertLocalToUTC(millis, false)

        println("開始")
        println(local.id)
        println(millis)
        println(result)
        println("結束")

        val test = Converters.toUTCMillis(1970, 1, 1)
        println(test)
    }

    @Test
    fun test2() {
        val local = DateTimeZone.forTimeZone(TimeZone.getDefault())
        val l = local.convertUTCToLocal(-57600000L)
        val date = DateTime(l)

        println("開始")
        println(date.toString("yyyy/MM/dd"))
        println("結束")

        val test = Converters.toDateString(-57600000L)
        println(test)
    }

    @Test
    fun test3() {
        val millis = Converters.toUTCMillis(2020, 4, 23)

        println("開始")
        println(millis)
        println("結束")
    }
}