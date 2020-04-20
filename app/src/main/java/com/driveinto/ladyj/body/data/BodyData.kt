package com.driveinto.ladyj.body.data

import android.os.Parcelable
import androidx.room.Entity
import com.driveinto.ladyj.customer.Customer
import com.driveinto.ladyj.room.Converters
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(primaryKeys = ["dateMillis", "bodyId"])
data class BodyData(
    val dateMillis: Long,
    var bodyId: Int,
    var remark: String?,
    var diagnosis: String?,
    var dirty: Boolean?
) : Parcelable {
    fun keys(): String = "[${this.dateMillis}][${this.bodyId}]"

    fun getMap(): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["dateMillis"] = dateMillis.toString()
        map["bodyId"] = bodyId.toString()
        if (remark != null) {
            map["remark"] = remark!!
        }
        if (diagnosis != null) {
            map["diagnosis"] = diagnosis!!
        }

        return map
    }

    companion object {
        fun emptyInstance(bodyId: Int) = BodyData(Converters.toUTCMillis(), bodyId, null, null, null)

        const val key = "BODY_DATA"
    }
}