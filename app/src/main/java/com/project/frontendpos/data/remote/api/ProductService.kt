package com.project.frontendpos.data.remote.api

import com.project.frontendpos.data.model.product.ProductContainerResponse
import com.project.frontendpos.data.model.product.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
    @GET("api/products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("category") category: String?,
        @Query("search") search: String?
    ): ProductContainerResponse

    @Multipart
    @POST("api/products")
    suspend fun uploadProduct(
        @Header("Authorization") token: String,
        @Part("product_name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part?
    ): ProductResponse

    @Multipart
    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part("product_name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part?
    ): ProductResponse

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}
