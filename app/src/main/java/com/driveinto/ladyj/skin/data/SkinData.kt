package com.driveinto.ladyj.skin.data

import android.os.Parcelable
import androidx.room.Entity
import com.driveinto.ladyj.room.Converters
import kotlinx.android.parcel.Parcelize

// 建議
@Parcelize
@Entity(primaryKeys = ["dateMillis", "skinId"])
data class SkinData(
    val dateMillis: Long,
    var skinId: Int,
    var advice: String?,
    var details: String?,
    var dirty: Boolean?
) : Parcelable {
    fun keys(): String = "[${this.dateMillis}][${this.skinId}]"

    fun getMap(): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["dateMillis"] = dateMillis.toString()
        map["skinId"] = skinId.toString()
        if(advice != null){
            map["advice"] = advice!!
        }
        if(details != null){
            map["details"] = details!!
        }

        return map
    }

    companion object {
        fun emptyInstance(skinId: Int) = SkinData(Converters.toUTCMillis(), skinId, null, null, null)

        const val key = "SKIN_DATA"
    }
}