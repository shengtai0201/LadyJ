package com.driveinto.ladyj.skin

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.driveinto.ladyj.body.Body
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Skin(
    @PrimaryKey var customerId: Int,
    var conditionDry: Boolean,
    var conditionOily: Boolean,
    var conditionSensitivity: Boolean,
    var conditionMixed: Boolean,
    var improveAcne: Boolean,
    var improveSensitivity: Boolean,
    var improveWrinkles: Boolean,
    var improvePores: Boolean,
    var improveSpots: Boolean,
    var improveDull: Boolean,
    var improvePock: Boolean,
    var improveOther: String?,
    var dirty: Boolean?
) : Parcelable {
    fun getMap(): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["customerId"] = customerId.toString()
        map["conditionDry"] = conditionDry.toString()
        map["conditionOily"] = conditionOily.toString()
        map["conditionSensitivity"] = conditionSensitivity.toString()
        map["conditionMixed"] = conditionMixed.toString()
        map["improveAcne"] = improveAcne.toString()
        map["improveSensitivity"] = improveSensitivity.toString()
        map["improveWrinkles"] = improveWrinkles.toString()
        map["improvePores"] = improvePores.toString()
        map["improveSpots"] = improveSpots.toString()
        map["improveDull"] = improveDull.toString()
        map["improvePock"] = improvePock.toString()
        if (improveOther != null) {
            map["improveOther"] = improveOther.toString()
        }

        return map
    }

    companion object {
        fun emptyInstance(customerId: Int) = Skin(
            customerId,
            conditionDry = false,
            conditionOily = false,
            conditionSensitivity = false,
            conditionMixed = false,
            improveAcne = false,
            improveSensitivity = false,
            improveWrinkles = false,
            improvePores = false,
            improveSpots = false,
            improveDull = false,
            improvePock = false,
            improveOther = null,
            dirty = null
        )
    }
}