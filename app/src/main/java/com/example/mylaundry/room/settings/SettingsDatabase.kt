package com.example.mylaundry.room.settings

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Settings::class], version = 1, exportSchema = false)
abstract class SettingsDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao

    companion object{
        @Volatile
        private var INSTANCE: SettingsDatabase? = null

        fun getDatabase(context: Context) : SettingsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SettingsDatabase::class.java,
                    "LaundryDatabase"
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