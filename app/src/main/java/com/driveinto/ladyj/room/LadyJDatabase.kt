package com.driveinto.ladyj.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.driveinto.ladyj.body.*
import com.driveinto.ladyj.body.data.BodyData
import com.driveinto.ladyj.body.data.BodyDataDao
import com.driveinto.ladyj.body.record.BodyRecord
import com.driveinto.ladyj.body.record.data.BodyRecordData
import com.driveinto.ladyj.customer.Customer
import com.driveinto.ladyj.customer.CustomerDao
import com.driveinto.ladyj.skin.Skin
import com.driveinto.ladyj.skin.SkinDao
import com.driveinto.ladyj.skin.data.SkinData
import com.driveinto.ladyj.skin.record.SkinRecord

@Database(
    entities = [Customer::class, Body::class, BodyData::class, BodyRecord::class, BodyRecordData::class, Skin::class, SkinData::class, SkinRecord::class],
    version = 4,
    exportSchema = false
)
//@TypeConverters(Converters::class)
public abstract class LadyJDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun bodyDao(): BodyDao
    abstract fun bodyDataDao(): BodyDataDao
    abstract fun skinDao(): SkinDao

    companion object {
        @Volatile
        private var INSTANCE: LadyJDatabase? = null

        fun getDatabase(context: Context): LadyJDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(context.applicationContext, LadyJDatabase::class.java, "lady_j")
                        .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}