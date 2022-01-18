package com.example.mylaundry.room.dryermachine

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mylaundry.room.washermachine.Washer

@Dao
interface DryerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDryerReset(dryer: Dryer)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDryer(dryer: Dryer)

    @Query("SELECT * FROM DryerMachine ORDER BY idDryer ASC")
    fun readAllData(): LiveData<List<Dryer>>

    @Delete
    suspend fun deleteDryer(dryer: Dryer)

    @Update
    fun updateValueDryer(dryer: Dryer)

    @Query("DELETE FROM DryerMachine")
    fun delete()

    @Query("SELECT * FROM DryerMachine")
    fun getAllData():List<Dryer>

}