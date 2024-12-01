package com.ogdenscleaners.ogdenscleanersapp.activities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2"

    val instance: APIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(APIService::class.java)
    }
}

interface APIService {
}
