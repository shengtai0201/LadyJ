package com.driveinto.ladyj.body.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BodyDataDao {
    @Query("SELECT * FROM BodyData ORDER BY dateMillis")
    fun queryAsync(): LiveData<List<BodyData>>

    @Query("SELECT * FROM BodyData ORDER BY dateMillis")
    fun query(): List<BodyData>

    @Update
    suspend fun updateAsync(bodyData: BodyData)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(bodyData: BodyData)

    @Delete
    suspend fun deleteAsync(bodyData: BodyData)
}