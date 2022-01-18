package com.example.mylaundry.room.dryermachine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DryerViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Dryer>>
    private val repository: DryerRepository

    init {
        val dryerDao = DryerDatabase.getDatabase(application).dryerDao()
        repository = DryerRepository(dryerDao)
        readAllData = repository.readAllData
    }

    fun addDryer(dryer: Dryer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDryers(dryer)
        }
    }

    fun addDryerReset(dryer: Dryer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDryersReset(dryer)
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
    fun updateDryer(dryer: Dryer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatevalueDryer(dryer)
        }
    }
}