package com.example.mylaundry.excel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionMachine (
    var no: Int,
    var type: String,
    var number: Int,
    var date: String,
    var time: String,
    var price: String
) : Parcelable