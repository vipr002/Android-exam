package com.example.androideksamen.screens.favorite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androideksamen.data.Product
import com.example.androideksamen.data.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteListViewModel : ViewModel() {

    private val _favoriteItems = MutableStateFlow<List<Product>>(emptyList())
    val favoriteItems = _favoriteItems.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
          val listOfFavoriteIds = ProductRepository.getFavorites().map { it.productId }
          _favoriteItems.value = ProductRepository.getProductsById(listOfFavoriteIds)
        }
    }
}