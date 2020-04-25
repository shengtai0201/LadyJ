package com.driveinto.ladyj.skin.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SkinDataDao {
    @Query("SELECT * FROM SkinData WHERE skinId = :skinId ORDER BY dateMillis")
    fun queryAsync(skinId: Int): LiveData<List<SkinData>>

    @Query("SELECT * FROM SkinData WHERE skinId = :skinId ORDER BY dateMillis")
    fun query(skinId: Int): List<SkinData>

    @Update
    suspend fun updateAsync(skinData: SkinData)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(skinData: SkinData)

    @Delete
    suspend fun deleteAsync(skinData: SkinData)
}