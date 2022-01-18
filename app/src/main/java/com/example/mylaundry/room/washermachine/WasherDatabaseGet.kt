package com.example.mylaundry.room.washermachine

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Washer::class], version = 1)
abstract class WasherDatabaseGet: RoomDatabase() {
    abstract fun dataDao(): WasherDao
}