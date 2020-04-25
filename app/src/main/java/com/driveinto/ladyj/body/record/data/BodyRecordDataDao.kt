package com.driveinto.ladyj.body.record.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BodyRecordDataDao {
    @Query("SELECT * FROM BodyRecordData WHERE bodyId = :bodyId ORDER BY dateMillis")
    fun queryAsync(bodyId: Int): LiveData<List<BodyRecordData>>

    @Query("SELECT * FROM BodyRecordData WHERE bodyId = :bodyId ORDER BY dateMillis")
    fun query(bodyId: Int): List<BodyRecordData>

    @Update
    suspend fun updateAsync(bodyRecordData: BodyRecordData)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(bodyRecordData: BodyRecordData)

    @Delete
    suspend fun deleteAsync(bodyRecordData: BodyRecordData)
}