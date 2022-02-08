package com.example.mylaundry.room.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Transactions>>
    private val repository: TransactionsRepository

    init {
        val transactionsDao = TransactionsDatabase.getDatabase(application).transacrionsDao()
        repository = TransactionsRepository(transactionsDao)
        readAllData = repository.readAllData
    }

    fun addTransactions(transactions: Transactions){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTransactions(transactions)
        }
    }

    //    fun deleteLockerBox(lockerbox: Settings){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteLockerBox(lockerbox)
//        }
//    }
//
    fun updateSettings(transactions: Transactions){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatevalueTransactions(transactions)
        }
    }
}