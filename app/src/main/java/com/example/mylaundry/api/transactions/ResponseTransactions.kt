package com.example.mylaundry.api.transactions

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseTransactions(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable
