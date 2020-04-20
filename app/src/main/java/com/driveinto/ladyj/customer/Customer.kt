package com.driveinto.ladyj.customer

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.driveinto.ladyj.room.Converters
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Customer(
    @PrimaryKey(autoGenerate = true) val rowId: Long,
    var id: Int,
    var memberId: String?,
    var name: String,
    var phone: String?,
    var birthdayMillis: Long,
    var address: String,
    var job: String?,
    var lineId: String?,
    var dateMillis: Long?,
    var height: Int?,
    var weight: Int?,
    var reference: String,
    var adviserId: String,
    var roleId: String,
    var dirty: Boolean?
) : Parcelable {
    fun getRolePosition(): Int {
        val role = CustomerRoles.fromId(roleId)
        return role?.position ?: 0
    }

    fun getMap(rowId: Long? = null): HashMap<String, String> {
        val map = HashMap<String, String>()

        map["rowId"] = (rowId ?: this.rowId).toString()
        map["id"] = id.toString()
        if (memberId != null) {
            map["memberId"] = memberId!!
        }
        map["name"] = name
        if (phone != null) {
            map["phone"] = phone!!
        }
        map["birthdayMillis"] = birthdayMillis.toString()
        map["address"] = address
        if (job != null) {
            map["job"] = job!!
        }
        if (lineId != null) {
            map["lineId"] = lineId!!
        }
        if (dateMillis != null) {
            map["dateMillis"] = dateMillis.toString()
        }
        map["height"] = height.toString()
        map["weight"] = weight.toString()
        map["reference"] = reference
        map["adviserId"] = adviserId
        map["roleId"] = roleId

        return map
    }

    companion object {
        fun emptyInstance() =
            Customer(0, 0, null, "", null, 0, "", null, null, null, null, null, "", "", "", null)

        const val key = "CUSTOMER"
    }
}