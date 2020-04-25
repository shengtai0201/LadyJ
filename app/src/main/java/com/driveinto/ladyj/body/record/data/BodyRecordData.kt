package com.driveinto.ladyj.body.record.data

import android.os.Parcelable
import androidx.room.Entity
import com.driveinto.ladyj.room.Converters
import kotlinx.android.parcel.Parcelize

// 組合
@Parcelize
@Entity(primaryKeys = ["dateMillis", "bodyId"])
data class BodyRecordData(
    val dateMillis: Long,
    var bodyId: Int,
    var design: String?,
    var buy: String?,
    var dirty: Boolean?
) : Parcelable {
    fun keys(): String = "[${this.dateMillis}][${this.bodyId}]"

    fun getMap(): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["dateMillis"] = dateMillis.toString()
        map["bodyId"] = bodyId.toString()
        if (design != null) {
            map["design"] = design!!
        }
        if (buy != null) {
            map["buy"] = buy!!
        }

        return map
    }

    companion object {
        fun emptyInstance(bodyId: Int) = BodyRecordData(Converters.toUTCMillis(), bodyId, null, null, null)

        const val key = "BODY_RECORD_DATA"
    }
}