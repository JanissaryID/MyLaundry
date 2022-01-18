package com.example.mylaundry.room.settings

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Settings::class], version = 1)
abstract class SettingDatabaseGet:RoomDatabase() {
    abstract fun dataDao(): SettingsDao
}