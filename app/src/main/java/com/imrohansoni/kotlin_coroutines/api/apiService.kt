package com.imrohansoni.kotlin_coroutines.api

import com.imrohansoni.kotlin_coroutines.models.Category
import com.imrohansoni.kotlin_coroutines.models.Product

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("categories")
    fun getCategories(): Call<List<Category>>

    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("products/{productId}")
    fun getProductById(@Path("productId") productId: Int): Call<Product>

    @GET("products")
    fun getProductsByCategoryId(@Query("categoryId") categoryId: Int): Call<List<Product>>
}

fun apiService(): ApiService {
    val mockInterceptor = MockInterceptor()
    val okHttpClient = OkHttpClient.Builder().addInterceptor(mockInterceptor).build()

    val retrofit = Retrofit.Builder()
        .client(OkHttpClient())
        .baseUrl("http://mockapi.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}
