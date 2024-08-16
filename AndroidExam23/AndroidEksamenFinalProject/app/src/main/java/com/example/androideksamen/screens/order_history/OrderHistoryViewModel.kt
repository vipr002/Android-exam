package com.example.androideksamen.screens.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androideksamen.data.Order
import com.example.androideksamen.data.Product
import com.example.androideksamen.data.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel : ViewModel() {

    private val _orderItems = MutableStateFlow<List<Order>>(emptyList())
    val orderItems = _orderItems.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        loadOrders()
    }
    fun loadOrders(){
        viewModelScope.launch(Dispatchers.IO) {
                _loading.value = true
                _orderItems.value = ProductRepository.getOrders()
                _loading.value = false
            }
        }
}
