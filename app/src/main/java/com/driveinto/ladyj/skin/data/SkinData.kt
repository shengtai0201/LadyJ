package com.driveinto.ladyj.skin.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.OffsetDateTime

@Parcelize
@Entity(primaryKeys = ["dateMillis", "skinId"])
data class SkinData(
    val dateMillis: Long,
    val skinId: Int,
    val advice: String?,
    val details: String?,
    var dirty: Boolean?
) : Parcelable {
}