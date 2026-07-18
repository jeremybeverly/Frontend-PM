package com.project.frontendpos.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.project.frontendpos.BuildConfig

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}