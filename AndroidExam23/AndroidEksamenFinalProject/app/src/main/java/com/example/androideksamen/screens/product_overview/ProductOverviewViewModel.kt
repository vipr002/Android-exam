package com.example.androideksamen.screens.product_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androideksamen.data.Product
import com.example.androideksamen.data.ProductRepository
import com.example.androideksamen.data.ProductRepository.getProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductOverviewViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    val products = _products.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory


    // ---- Load Products ----

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            setCategory(null)
            _products.value = ProductRepository.getProducts()
            _loading.value = false
        }
    }

    // ---- Searchfield ----
   // https://www.youtube.com/watch?v=CfL6Dl2_dAE
    
    val produkts = combine(searchText, _products, selectedCategory)
        { text, produkts, category ->
            if(text.isBlank() && category.isNullOrBlank()){
                produkts
            }   else {
                produkts.filter{
                    it.doesMatchQuery(text) && (category.isNullOrBlank() || it.category == category)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _products.value
        )
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun setIsSearching(searching: Boolean){
        _isSearching.value = searching
    }
    fun toggleSearch(){
        setIsSearching(!_isSearching.value)
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }

}