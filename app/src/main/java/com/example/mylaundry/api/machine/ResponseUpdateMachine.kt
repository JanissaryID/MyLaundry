package com.example.mylaundry.api.machine

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseUpdateMachine(

	@field:SerializedName("machine_status")
	val machineStatus: Boolean? = null
) : Parcelable
