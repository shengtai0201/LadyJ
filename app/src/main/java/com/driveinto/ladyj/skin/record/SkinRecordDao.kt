package com.driveinto.ladyj.skin.record

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SkinRecordDao {
    @Query("SELECT * FROM SkinRecord WHERE skinId = :skinId ORDER BY dateMillis")
    fun queryAsync(skinId: Int): LiveData<List<SkinRecord>>

    @Query("SELECT * FROM SkinRecord WHERE skinId = :skinId ORDER BY dateMillis")
    fun query(skinId: Int): List<SkinRecord>

    @Update
    suspend fun updateAsync(skinRecord: SkinRecord)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(skinRecord: SkinRecord)

    @Delete
    suspend fun deleteAsync(skinRecord: SkinRecord)
}