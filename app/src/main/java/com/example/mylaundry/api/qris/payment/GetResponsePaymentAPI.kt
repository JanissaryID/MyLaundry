package com.example.mylaundry.api.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetResponsePaymentAPI(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("time")
	val time: Time? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("ref")
	val ref: String? = null,

	@field:SerializedName("nominal")
	val nominal: Int? = null,

	@field:SerializedName("callback_response")
	val callbackResponse: String? = null,

	@field:SerializedName("merchant_trxid")
	val merchantTrxid: String? = null,

	@field:SerializedName("callback")
	val callback: String? = null,

	@field:SerializedName("tip")
	val tip: Int? = null
) : Parcelable

@Parcelize
data class Time(

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("paid")
	val paid: String? = null
) : Parcelable
