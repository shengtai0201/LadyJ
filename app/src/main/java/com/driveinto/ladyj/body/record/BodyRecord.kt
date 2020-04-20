package com.driveinto.ladyj.body.record

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.OffsetDateTime

@Parcelize
@Entity(primaryKeys = ["dateMillis", "bodyId"])
data class BodyRecord(
    val dateMillis: Long,
    val bodyId: Int,
    val upperBust: Int?,
    val underBust: Int?,
    val cupSize: Int?,
    val leftArm: Int?,
    val rightArm: Int?,
    val stomach: Int?,
    val waist: Int?,
    val abdominal: Int?,
    val hip: Int?,
    val leftLeg: Int?,
    val rightLeg: Int?,
    var dirty: Boolean?
) : Parcelable {
}