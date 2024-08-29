package com.imrohansoni.kotlin_coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imrohansoni.kotlin_coroutines.api.apiService
import com.imrohansoni.kotlin_coroutines.models.Category
import com.imrohansoni.kotlin_coroutines.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias Categories = List<Category>
typealias Products = List<Product>

class MainActivityViewModel : ViewModel() {
    private val categoriesUiState = MutableLiveData<UiState<Categories>>()
    fun getCategoriesUiState(): LiveData<UiState<Categories>> = categoriesUiState

    private val productsUiState = MutableLiveData<UiState<Products>>()
    fun getProductsUiState(): LiveData<UiState<Products>> = productsUiState

    fun getCategories() {
        categoriesUiState.value = UiState.Loading
        apiService().getCategories().enqueue(object : Callback<Categories> {
            override fun onResponse(
                call: Call<Categories>, response: Response<Categories>
            ) {
                if (response.isSuccessful) {
                    categoriesUiState.value = UiState.Success(response.body()!!)
                } else {
                    categoriesUiState.value =
                        UiState.Error(response.errorBody()?.string().toString())
                }
            }

            override fun onFailure(call: Call<Categories>, throwable: Throwable) {
                categoriesUiState.value = UiState.Error("something went wrong")
            }
        })
    }

    fun getProductById(categoryId: Int) {
        productsUiState.value = UiState.Loading

        apiService().getProductsByCategoryId(categoryId).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, res: Response<Products>) {
                if (res.isSuccessful) {
                    productsUiState.value = UiState.Success(res.body()!!)
                } else {
                    productsUiState.value = UiState.Error(res.errorBody()?.string().toString())
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                productsUiState.value = UiState.Error("something went wrong")
            }

        })
    }

}