package com.project.frontendpos.data.repository

import com.project.frontendpos.data.model.product.ProductResponse
import com.project.frontendpos.data.remote.api.ProductService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductRepository(
    private val api: ProductService
) {

    suspend fun getProducts(
        token: String,
        category: String? = null,
        search: String? = null
    ): Result<List<ProductResponse>> = runCatching {
        api.getProducts(token, category, search).data
    }

    suspend fun uploadProduct(
        token: String,
        name: RequestBody,
        price: RequestBody,
        category: RequestBody,
        image: MultipartBody.Part?
    ): Result<Unit> =
        runCatching {
            api.uploadProduct(
                token,
                name,
                price,
                category,
                image
            )
        }.map { Unit }

    suspend fun updateProduct(
        token: String,
        id: String,
        name: RequestBody,
        price: RequestBody,
        category: RequestBody,
        image: MultipartBody.Part?
    ): Result<Unit> =
        runCatching {

            api.updateProduct(
                token,
                id,
                name,
                price,
                category,
                image
            )

        }.map { Unit }

    suspend fun deleteProduct(
        token: String,
        id: String
    ): Result<Unit> =
        runCatching {

            api.deleteProduct(token, id)

        }.map { Unit }
}
