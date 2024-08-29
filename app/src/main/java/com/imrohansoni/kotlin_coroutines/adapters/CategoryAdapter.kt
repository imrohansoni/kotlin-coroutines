package com.imrohansoni.kotlin_coroutines.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imrohansoni.kotlin_coroutines.Categories
import com.imrohansoni.kotlin_coroutines.databinding.CategoryBinding
import com.imrohansoni.kotlin_coroutines.models.Category

class CategoryAdapter(
    private val categories: Categories,
    private val callback: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private lateinit var binding: CategoryBinding

    inner class CategoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(category: Category) {
            binding.name.text = category.name
            Glide.with(view.context).load(category.imageUrl).into(binding.image)
            binding.container.setOnClickListener {
                callback.invoke(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        binding = CategoryBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(binding.root)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }
}