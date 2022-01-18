package com.example.mylaundry.room.washermachine

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Washer::class], version = 1, exportSchema = false)
abstract class WasherDatabase : RoomDatabase() {

    abstract fun washerDao(): WasherDao

    companion object{
        @Volatile
        private var INSTANCE: WasherDatabase? = null

        fun getDatabase(context: Context) : WasherDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WasherDatabase::class.java,
                    "WasherDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}