package com.driveinto.ladyj.body

import androidx.lifecycle.LiveData
import androidx.room.*
import com.driveinto.ladyj.customer.Customer

@Dao
interface BodyDao {
    @Query("SELECT * FROM Body WHERE customerId = :customerId LIMIT 1")
    fun queryAsync(customerId: Int): LiveData<Body?>

    @Query("SELECT * FROM Body WHERE customerId = :customerId LIMIT 1")
    fun query(customerId: Int): Body?

    @Update
    suspend fun updateAsync(body: Body)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(body: Body)
}