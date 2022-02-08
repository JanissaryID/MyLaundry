package com.example.mylaundry.api.machine

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseMachine(

	@field:SerializedName("machine_type")
	val machineType: Int? = null,

	@field:SerializedName("machine_status")
	val machineStatus: Boolean? = null,

	@field:SerializedName("machine_number")
	val machineNumber: Int? = null,

	@field:SerializedName("machine_id")
	val machineId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable
