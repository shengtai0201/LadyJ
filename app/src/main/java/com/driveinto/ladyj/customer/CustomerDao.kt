package com.driveinto.ladyj.customer

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAsync(customer: Customer): Long

    @Query("SELECT * FROM Customer WHERE adviserId = :adviserId ORDER BY id")
    fun queryAsync(adviserId: String): LiveData<List<Customer>>

    @Query("SELECT * FROM Customer WHERE adviserId = :adviserId ORDER BY id")
    fun query(adviserId: String): List<Customer>

//    @Query("SELECT * FROM Customer WHERE rowId = :rowId LIMIT 1")
//    fun queryAsync(rowId: Long): LiveData<Customer?>

//    @Query("SELECT * FROM Customer WHERE rowId = :rowId LIMIT 1")
//    fun query(rowId: Long): Customer?

    @Update
    suspend fun updateAsync(customer: Customer)

    @Delete
    suspend fun deleteAsync(customer: Customer)
}