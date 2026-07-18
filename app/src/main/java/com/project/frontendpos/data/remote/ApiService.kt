package com.project.frontendpos.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

// --- DATA MODELS ---

data class ProductResponse(
    val _id: String,
    val name: String,
    val price: Double,
    val sku: String? = null
)

data class ProductRequest(
    val name: String,
    val price: Double
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserInfo? = null
)

data class UserInfo(
    val id: String,
    val name: String,
    val username: String,
    val role: String
)

// --- RETROFIT INTERFACE ---

interface ApiService {

    // 1. Public Endpoint (No Token Required)
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // 2. Protected Endpoints (Every single one needs the Authorization Header)

    @GET("api/products")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): List<ProductResponse>

    @POST("api/products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body product: ProductRequest
    ): ProductResponse

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}