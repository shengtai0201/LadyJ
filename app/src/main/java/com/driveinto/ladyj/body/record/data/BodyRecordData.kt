package com.driveinto.ladyj.body.record.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.OffsetDateTime

@Parcelize
@Entity(primaryKeys = ["dateMillis", "bodyId"])
data class BodyRecordData(
    val dateMillis: Long,
    val bodyId: Int,
    val design: String?,
    val buy: String?,
    var dirty: Boolean?
) : Parcelable {
}