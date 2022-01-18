package com.example.mylaundry.room.dryermachine

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "DryerMachine")
data class Dryer(
    @PrimaryKey(autoGenerate = true)

    var idDryer: Int? = null,

    var numberMachine: Int? = null,

    var isActive: Boolean? = null,
) : Parcelable