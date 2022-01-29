package com.example.mylaundry.api.machine

import retrofit2.Call
import retrofit2.http.*

interface API {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/machine")
    fun getMachine(): Call<List<ResponseMachine>>


}