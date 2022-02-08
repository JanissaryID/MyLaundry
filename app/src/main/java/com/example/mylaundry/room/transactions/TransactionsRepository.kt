package com.example.mylaundry.room.transactions

import androidx.lifecycle.LiveData

class TransactionsRepository(private val transactionsDao: TransactionsDao) {

    val readAllData: LiveData<List<Transactions>> = transactionsDao.readAllData()

    suspend fun addTransactions(transactions: Transactions){
        transactionsDao.addtransactions(transactions)
    }

    //    suspend fun deleteLockerBox(lockerBox: Settings){
//        settingDao.deleteLockerBox(lockerBox)
//    }
//
    fun updatevalueTransactions(transactions: Transactions){
        transactionsDao.updateValueTransactions(transactions)
    }
}