package com.example.mylaundry.room.settings

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "SettingsLaundry")
data class Settings(
    @PrimaryKey(autoGenerate = true)

    var idSettings: Int? = null,

    var nameSetting: String? = null,

    var valueSetting: String? = null
) : Parcelable