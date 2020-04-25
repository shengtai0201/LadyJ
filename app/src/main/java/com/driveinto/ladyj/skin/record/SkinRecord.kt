package com.driveinto.ladyj.skin.record

import android.os.Parcelable
import androidx.room.Entity
import com.driveinto.ladyj.room.Converters
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(primaryKeys = ["dateMillis", "skinId"])
data class SkinRecord(
    val dateMillis: Long,
    var skinId: Int,
    var remark: String?,
    var dirty: Boolean?
) : Parcelable {
    fun keys(): String = "[${this.dateMillis}][${this.skinId}]"

    fun getMap(): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["dateMillis"] = dateMillis.toString()
        map["skinId"] = skinId.toString()
        if (remark != null) {
            map["remark"] = remark!!
        }

        return map
    }

    companion object {
        fun emptyInstance(skinId: Int) = SkinRecord(Converters.toUTCMillis(), skinId, null, null)

        const val key = "SKIN_RECORD"
    }
}