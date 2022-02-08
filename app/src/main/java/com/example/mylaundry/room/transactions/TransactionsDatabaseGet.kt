package com.example.mylaundry.room.transactions

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Transactions::class], version = 1)
abstract class TransactionsDatabaseGet: RoomDatabase() {
    abstract fun dataDao(): TransactionsDao
}