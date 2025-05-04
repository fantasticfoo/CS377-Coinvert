package com.example.coinverttest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApiService {
    @GET("v6/046d8294b08d815b9c23f928/latest/{base}")
    fun getRates(@Path("base") base: String): Call<ExchangeRateResponse>
}


