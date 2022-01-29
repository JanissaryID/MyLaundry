package com.example.mylaundry.api.qris.generate

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetResponseAPI(

	@field:SerializedName("refid")
	val refid: Long? = null,

	@field:SerializedName("rawqr")
	val rawqr: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
