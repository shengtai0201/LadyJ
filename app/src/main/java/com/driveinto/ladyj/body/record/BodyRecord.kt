package com.driveinto.ladyj.body.record

import android.os.Parcelable
import androidx.room.Entity
import com.driveinto.ladyj.room.Converters
import kotlinx.android.parcel.Parcelize

// 追蹤記錄
@Parcelize
@Entity(primaryKeys = ["dateMillis", "bodyId"])
data class BodyRecord(
    val dateMillis: Long,
    var bodyId: Int,
    var upperBust: Int?,
    var underBust: Int?,
    var cupSize: Int?,
    var leftArm: Int?,
    var rightArm: Int?,
    var stomach: Int?,
    var waist: Int?,
    var abdominal: Int?,
    var hip: Int?,
    var leftLeg: Int?,
    var rightLeg: Int?,
    var dirty: Boolean?
) : Parcelable {
    fun keys(): String = "[${this.dateMillis}][${this.bodyId}]"

    fun getMap(): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["dateMillis"] = dateMillis.toString()
        map["bodyId"] = bodyId.toString()
        if (upperBust != null) {
            map["upperBust"] = upperBust.toString()
        }
        if (underBust != null) {
            map["underBust"] = underBust.toString()
        }
        if (cupSize != null) {
            map["cupSize"] = cupSize.toString()
        }
        if (leftArm != null) {
            map["leftArm"] = leftArm.toString()
        }
        if (rightArm != null) {
            map["rightArm"] = rightArm.toString()
        }
        if (stomach != null) {
            map["stomach"] = stomach.toString()
        }
        if (waist != null) {
            map["waist"] = waist.toString()
        }
        if (abdominal != null) {
            map["abdominal"] = abdominal.toString()
        }
        if (hip != null) {
            map["hip"] = hip.toString()
        }
        if (leftLeg != null) {
            map["leftLeg"] = leftLeg.toString()
        }
        if (rightLeg != null) {
            map["rightLeg"] = rightLeg.toString()
        }

        return map
    }

    companion object {
        fun emptyInstance(bodyId: Int) = BodyRecord(
            Converters.toUTCMillis(),
            bodyId,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        const val key = "BODY_RECORD"
    }
}