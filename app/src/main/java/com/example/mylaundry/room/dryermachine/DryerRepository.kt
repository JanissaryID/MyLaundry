package com.example.mylaundry.room.dryermachine

import androidx.lifecycle.LiveData

class DryerRepository(private val dryerDao: DryerDao) {

    val readAllData: LiveData<List<Dryer>> = dryerDao.readAllData()

    suspend fun addDryers(dryer: Dryer){
        dryerDao.addDryer(dryer)
    }

    suspend fun addDryersReset(dryer: Dryer){
        dryerDao.addDryerReset(dryer)
    }

//        suspend fun deleteLockerBox(lockerBox: Settings){
//        settingDao.deleteLockerBox(lockerBox)
//    }

    fun updatevalueDryer(dryer: Dryer){
        dryerDao.updateValueDryer(dryer)
    }

    fun deleteTable(){
        dryerDao.delete()
    }
}