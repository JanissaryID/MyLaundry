package com.example.mylaundry.room.settings

import androidx.lifecycle.LiveData

class SettingsRepository(private val settingDao: SettingsDao) {

    val readAllData: LiveData<List<Settings>> = settingDao.readAllData()

    suspend fun addSettings(settings : Settings){
        settingDao.addSettings(settings)
    }

//    suspend fun deleteLockerBox(lockerBox: Settings){
//        settingDao.deleteLockerBox(lockerBox)
//    }
//
    fun updatevalueSettings(settings: Settings){
        settingDao.updateValueSettings(settings)
    }
}