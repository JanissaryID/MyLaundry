package com.example.mylaundry.room.settings

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSettings(settings : Settings)

    @Query("SELECT * FROM SettingsLaundry ORDER BY idSettings ASC")
    fun readAllData(): LiveData<List<Settings>>

    @Delete
    suspend fun deleteSettings(settings: Settings)

    @Update
    fun updateValueSettings(settings: Settings)

    @Query("SELECT * FROM SettingsLaundry")
    fun getAllData():List<Settings>

}