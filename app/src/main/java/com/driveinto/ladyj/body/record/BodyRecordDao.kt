package com.driveinto.ladyj.body.record

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BodyRecordDao {
    @Query("SELECT * FROM BodyRecord WHERE bodyId = :bodyId ORDER BY dateMillis")
    fun queryAsync(bodyId: Int): LiveData<List<BodyRecord>>

    @Query("SELECT * FROM BodyRecord WHERE bodyId = :bodyId ORDER BY dateMillis")
    fun query(bodyId: Int): List<BodyRecord>

    @Update
    suspend fun updateAsync(bodyRecord: BodyRecord)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(bodyRecord: BodyRecord)

    @Delete
    suspend fun deleteAsync(bodyRecord: BodyRecord)
}