package com.project.frontendpos.data.remote

import com.project.frontendpos.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthService by lazy { retrofit.create(AuthService::class.java) }
    val productApi: ProductService by lazy { retrofit.create(ProductService::class.java) }
}