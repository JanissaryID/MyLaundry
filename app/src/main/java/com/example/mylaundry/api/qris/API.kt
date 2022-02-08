package com.example.mylaundry.api.qris

import com.example.mylaundry.api.qris.generate.GetResponseAPI
import com.example.mylaundry.api.qris.generate.ResponseAPI
import com.example.mylaundry.api.payment.GetResponsePaymentAPI
import com.example.mylaundry.api.qris.payment.ResponsePaymentAPI
import retrofit2.Call
import retrofit2.http.*

interface API {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/qris/generate/")
    fun Qris(@Header("Authorization") authHeader: String, @Body qrData: ResponseAPI) : Call<GetResponseAPI>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/qris/check-status/")
    fun CheckPayment(@Header("Authorization") authHeader: String, @Body statusData: ResponsePaymentAPI) : Call<GetResponsePaymentAPI>

}