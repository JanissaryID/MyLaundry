package com.example.mylaundry.room.washermachine

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mylaundry.room.settings.Settings

@Dao
interface WasherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWasherReset(washer: Washer)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWasher(washer: Washer)

    @Query("SELECT * FROM WasherMachine ORDER BY idWasher ASC")
    fun readAllData(): LiveData<List<Washer>>

    @Delete
    suspend fun deleteWasher(washer: Washer)

    @Update
    fun updateValueWasher(washer: Washer)

    @Query("DELETE FROM WasherMachine")
    fun delete()

    @Query("SELECT * FROM WasherMachine")
    fun getAllData():List<Washer>

}