package com.driveinto.ladyj.body

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Body(
    @PrimaryKey var customerId: Int,
    var healthSpine: Boolean,
    var healthBackache: Boolean,
    var healthOther: String?,
    var curveBreastEnhancement: Boolean,
    var curveBreastReduction: Boolean,
    var curveBreastCare: Boolean,
    var curveArm: Boolean,
    var curveHip: Boolean,
    var curveStomach: Boolean,
    var curveWaist: Boolean,
    var curveAbdominal: Boolean,
    var curveThigh: Boolean,
    var curveCalf: Boolean,
    var curveFatSoft: Boolean,
    var curveFatHard: Boolean,
    var curveFatCellulite: Boolean,
    var curveFatTangled: Boolean,
    var curveFatOther: String?,
    var dirty: Boolean?
) : Parcelable {
    val requestMap: HashMap<String, String>
        get() {
            val map = HashMap<String, String>()
            map["filter[logic]"] = "and"
            map["filter[filters][0][field]"] = "BodyId"
            map["filter[filters][0][operator]"] = "eq"
            map["filter[filters][0][value]"] = this.customerId.toString()

            return map
        }

    fun getMap(): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["customerId"] = customerId.toString()
        map["healthSpine"] = healthSpine.toString()
        map["healthBackache"] = healthBackache.toString()
        if (healthOther != null) {
            map["healthOther"] = healthOther!!
        }
        map["curveBreastEnhancement"] = curveBreastEnhancement.toString()
        map["curveBreastReduction"] = curveBreastReduction.toString()
        map["curveBreastCare"] = curveBreastCare.toString()
        map["curveArm"] = curveArm.toString()
        map["curveHip"] = curveHip.toString()
        map["curveStomach"] = curveStomach.toString()
        map["curveWaist"] = curveWaist.toString()
        map["curveAbdominal"] = curveAbdominal.toString()
        map["curveThigh"] = curveThigh.toString()
        map["curveCalf"] = curveCalf.toString()
        map["curveFatSoft"] = curveFatSoft.toString()
        map["curveFatHard"] = curveFatHard.toString()
        map["curveFatCellulite"] = curveFatCellulite.toString()
        map["curveFatTangled"] = curveFatTangled.toString()
        if (curveFatOther != null) {
            map["curveFatOther"] = curveFatOther!!
        }

        return map
    }

    companion object {
        fun emptyInstance(customerId: Int) = Body(
            customerId,
            healthSpine = false,
            healthBackache = false,
            healthOther = null,
            curveBreastEnhancement = false,
            curveBreastReduction = false,
            curveBreastCare = false,
            curveArm = false,
            curveHip = false,
            curveStomach = false,
            curveWaist = false,
            curveAbdominal = false,
            curveThigh = false,
            curveCalf = false,
            curveFatSoft = false,
            curveFatHard = false,
            curveFatCellulite = false,
            curveFatTangled = false,
            curveFatOther = null,
            dirty = null
        )

        const val key = "BODY"
    }
}