package com.example.mylaundry.room.dryermachine

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Dryer::class], version = 1)
abstract class DryerDatabaseGet: RoomDatabase() {
    abstract fun dataDao(): DryerDao
}