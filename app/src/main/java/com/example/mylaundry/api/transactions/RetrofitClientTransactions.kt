package com.example.mylaundry.api.transactions

import com.example.mylaundry.fragment.HomeFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientTransactions {

//    private const val BASE_URL = "https://app-laundry-api.herokuapp.com"
    private var BASE_URL = "http://${HomeFragment.ipvar}:${HomeFragment.portvar}"

    val instance : API by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(API::class.java)
    }
}