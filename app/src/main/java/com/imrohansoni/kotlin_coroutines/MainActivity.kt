package com.imrohansoni.kotlin_coroutines

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.imrohansoni.kotlin_coroutines.adapters.CategoryAdapter
import com.imrohansoni.kotlin_coroutines.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivityViewModel.getCategoriesUiState().observe(this) {
            when (it) {
                is UiState.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    Toast.makeText(this, "loading...", Toast.LENGTH_SHORT).show()
                }

                is UiState.Success -> {
                    Log.d("MOCK_INTERCEPTOR", it.data.toString())
                    binding.categoryList.layoutManager = LinearLayoutManager(this)
                    binding.categoryList.adapter = CategoryAdapter(it.data) { category ->
                        Toast.makeText(
                            this@MainActivity,
                            "category id ${category.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        mainActivityViewModel.getCategories()
    }
}