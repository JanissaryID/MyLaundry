package com.example.mylaundry.room.washermachine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WasherViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Washer>>
    private val repository: WasherRepository

    init {
        val washerDao = WasherDatabase.getDatabase(application).washerDao()
        repository = WasherRepository(washerDao)
        readAllData = repository.readAllData
    }

    fun addWasher(washer: Washer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWashers(washer)
        }
    }

    fun addWasherReset(washer: Washer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWashersReset(washer)
        }
    }

    fun delete(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteTable()
        }
    }

    //    fun deleteLockerBox(lockerbox: Settings){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteLockerBox(lockerbox)
//        }
//    }
//
    fun updateWasher(washer: Washer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatevalueWasher(washer)
        }
    }
}