package com.example.mylaundry.api.qris.payment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponsePaymentAPI(

	@field:SerializedName("refid")
	val refid: Long? = null
) : Parcelable
