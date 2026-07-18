package com.project.frontendpos.data.remote

import com.project.frontendpos.data.model.ProductRequest
import com.project.frontendpos.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
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