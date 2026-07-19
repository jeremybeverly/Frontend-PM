package com.project.frontendpos.data.remote

import com.project.frontendpos.BuildConfig
import com.project.frontendpos.data.remote.api.AuthService
import com.project.frontendpos.data.remote.api.ModifierService
import com.project.frontendpos.data.remote.api.ProductService
import com.project.frontendpos.data.remote.api.TransactionService
import com.project.frontendpos.data.remote.api.ShiftService
import com.project.frontendpos.data.remote.api.CashflowService
import com.project.frontendpos.data.remote.api.TransactionHistoryService
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

    fun resolveImageUrl(relativePath: String?): String {
        if (relativePath.isNullOrBlank()) return "https://placehold.co/600x400/png"

        val cleanBase = BASE_URL.trimEnd('/')

        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            // FIX: If the database saved it as localhost, rewrite it to use the friend's IP
            if (relativePath.contains("localhost") || relativePath.contains("127.0.0.1")) {
                val extractedPath = relativePath.replace(Regex("^https?://(localhost|127\\.0\\.0\\.1)(:\\d+)?"), "")
                val cleanPath = if (extractedPath.startsWith("/")) extractedPath else "/$extractedPath"
                return "$cleanBase$cleanPath"
            }
            return relativePath
        }

        val cleanPath = if (relativePath.startsWith("/")) relativePath else "/$relativePath"
        return "$cleanBase$cleanPath"
    }

    val authApi: AuthService by lazy { retrofit.create(AuthService::class.java) }
    val productApi: ProductService by lazy { retrofit.create(ProductService::class.java) }
    val modifierApi: ModifierService by lazy { retrofit.create(ModifierService::class.java) }
    val transactionApi: TransactionService by lazy { retrofit.create(TransactionService::class.java) }
    val shiftApi: ShiftService by lazy { retrofit.create(ShiftService::class.java) }
    val cashflowApi: CashflowService by lazy { retrofit.create(CashflowService::class.java) }
    val historyApi: TransactionHistoryService by lazy { retrofit.create(TransactionHistoryService::class.java) }
}