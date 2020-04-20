package com.driveinto.ladyj.skin

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Query
import com.driveinto.ladyj.DataSourceResponse
import com.driveinto.ladyj.body.Body
import retrofit2.Call
import retrofit2.http.*

@Dao
interface SkinDao {
    @Query("SELECT * FROM Skin WHERE customerId = :customerId LIMIT 1")
    fun queryAsync(customerId: Int): LiveData<Skin?>

    @Query("SELECT * FROM Skin WHERE customerId = :customerId LIMIT 1")
    fun query(customerId: Int): Skin?

    @Update
    suspend fun updateAsync(skin: Skin)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(skin: Skin)
}