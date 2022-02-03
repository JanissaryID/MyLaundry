package com.example.mylaundry.room.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Settings>>
    private val repository: SettingsRepository

    init {
        val settingDao = SettingsDatabase.getDatabase(application).settingsDao()
        repository = SettingsRepository(settingDao)
        readAllData = repository.readAllData
    }

    fun addSettings(settings: Settings){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSettings(settings)
        }
    }

    fun updateSettings(settings: Settings){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatevalueSettings(settings)
        }
    }
}