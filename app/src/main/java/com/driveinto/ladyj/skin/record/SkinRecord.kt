package com.driveinto.ladyj.skin.record

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.OffsetDateTime

@Parcelize
@Entity(primaryKeys = ["dateMillis", "skinId"])
data class SkinRecord(
    val dateMillis: Long,
    val skinId: Int,
    val remark: String,
    var dirty: Boolean?
) : Parcelable {
}