package com.example.mylaundry.room.washermachine

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "WasherMachine")
data class Washer(
    @PrimaryKey(autoGenerate = true)

    var idWasher: Int? = null,

    var numberMachine: Int? = null,

    var isActive: Boolean? = null,
) : Parcelable