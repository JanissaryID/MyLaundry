package com.example.mylaundry.room.washermachine

import androidx.lifecycle.LiveData

class WasherRepository(private val washerDao: WasherDao) {

    val readAllData: LiveData<List<Washer>> = washerDao.readAllData()

    suspend fun addWashers(washer: Washer){
        washerDao.addWasher(washer)
    }

    suspend fun addWashersReset(washer: Washer){
        washerDao.addWasherReset(washer)
    }

//        suspend fun deleteLockerBox(lockerBox: Settings){
//        settingDao.deleteLockerBox(lockerBox)
//    }

    fun updatevalueWasher(washer: Washer){
        washerDao.updateValueWasher(washer)
    }

    fun deleteTable(){
        washerDao.delete()
    }
}