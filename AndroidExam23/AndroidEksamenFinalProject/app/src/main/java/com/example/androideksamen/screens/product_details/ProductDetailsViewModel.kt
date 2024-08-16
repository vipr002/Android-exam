package com.example.androideksamen.screens.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androideksamen.data.Product
import com.example.androideksamen.data.ProductRepository
import com.example.androideksamen.data.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ProductDetailsViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    private val _isInCart = MutableStateFlow(false)
    val isInCart = _isInCart.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _rating = MutableStateFlow(0)
    var rating = _rating.asStateFlow()


    // ---- Products ----

    fun loadProduct(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedProducts = ProductRepository.getProductById(id)
            _product.value = fetchedProducts
            _rating.value = _product.value?.rating ?: 0
        }
    }

    fun setSelectedProduct(productId: String) {
        viewModelScope.launch {
            _loading.value = true
            _selectedProduct.value = ProductRepository.getProductById(productId.toInt())
            _isFavorite.value = isCurrentProductAFavorite()
            _loading.value = false
        }
    }

    fun setRating(id: Int, newRating: Int) {
       _rating.value = newRating
        viewModelScope.launch {
            ProductRepository.updateProductRating(id, newRating)
        }
    }

    // ---- Cart ----

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            ProductRepository.addCartItem(productId)
        }
    }

    // ---- Favorites ----

     fun updateFavorite(productId: Int) {
        viewModelScope.launch {
            if (isFavorite.value) {
                ProductRepository.removeFavorite(Favorite(productId))
            } else {
                ProductRepository.addFavorite(Favorite(productId))
            }
            _isFavorite.value = isCurrentProductAFavorite()
        }
    }

    private suspend fun isCurrentProductAFavorite(): Boolean {
        return ProductRepository.getFavorites().any { it.productId == selectedProduct.value?.id }
    }
}
