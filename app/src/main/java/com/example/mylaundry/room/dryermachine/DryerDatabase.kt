package com.example.mylaundry.room.dryermachine

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Dryer::class], version = 1, exportSchema = false)
abstract class DryerDatabase : RoomDatabase() {

    abstract fun dryerDao(): DryerDao

    companion object{
        @Volatile
        private var INSTANCE: DryerDatabase? = null

        fun getDatabase(context: Context) : DryerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DryerDatabase::class.java,
                    "DryerDatabase"
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