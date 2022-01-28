package com.example.mylaundry.room.transactions

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mylaundry.room.settings.Settings

@Dao
interface TransactionsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addtransactions(transactions: Transactions)

    @Query("SELECT * FROM TransactionLaundry ORDER BY idMachine ASC")
    fun readAllData(): LiveData<List<Transactions>>

    @Delete
    suspend fun deleteTransactions(transactions: Transactions)

    @Update
    fun updateValueTransactions(transactions: Transactions)

    @Query("SELECT * FROM TransactionLaundry")
    fun getAllData():List<Transactions>

}