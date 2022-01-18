package com.example.mylaundry.api.generate

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseAPI(

	@field:SerializedName("merchantid")
	val merchantid: String? = null,

	@field:SerializedName("nominal")
	val nominal: Int? = null,

	@field:SerializedName("tip")
	val tip: Int? = null,

	@field:SerializedName("ref")
	val ref: String? = null,

	@field:SerializedName("callback")
	val callback: String? = null,

	@field:SerializedName("expire")
	val expire: Int? = null

) : Parcelable
