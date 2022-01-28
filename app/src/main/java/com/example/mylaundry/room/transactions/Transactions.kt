package com.example.mylaundry.room.transactions

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "TransactionLaundry")
data class Transactions(
    @PrimaryKey(autoGenerate = true)

    var idMachine: Int? = null,

    var typeMachine: String? = null,

    var noMachine: Int? = null,

    var date: String? = null,

    var timeMachine: String? = null,

    var priceMachine: Int? = null
) : Parcelable