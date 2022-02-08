package com.example.mylaundry.api.transactions

import retrofit2.Call
import retrofit2.http.*

interface API {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/transactions")
    fun getTransactions(): Call<List<ResponseTransactions>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/transaction-filter-date")
    fun getFilterTransactions(@Query(value="date", encoded=true) date: String?): Call<List<ResponseTransactions>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/add-transaction")
    fun insertTransactions(@Body statusData: ResponseTransactions) : Call<ResponseTransactions>
}