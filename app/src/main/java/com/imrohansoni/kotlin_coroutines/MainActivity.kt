package com.imrohansoni.kotlin_coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.imrohansoni.kotlin_coroutines.api.apiService
import com.imrohansoni.kotlin_coroutines.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService().getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        this@MainActivity,
                        response.body().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        response.errorBody()?.string().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, throwable: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}