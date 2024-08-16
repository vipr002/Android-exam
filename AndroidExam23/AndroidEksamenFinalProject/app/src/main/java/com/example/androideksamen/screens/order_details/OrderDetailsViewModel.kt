package com.example.androideksamen.screens.order_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androideksamen.data.Order
import com.example.androideksamen.data.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class OrderDetailsViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _order = MutableStateFlow<Order?>(null)
    val order = _order.asStateFlow()

    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder = _selectedOrder.asStateFlow()


    fun loadOrder(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedOrders = ProductRepository.getOrderById(id)
            _order.value = fetchedOrders
        }
    }

    fun setSelectedOrder(orderId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _selectedOrder.value = ProductRepository.getOrderById(orderId)
            _loading.value = false
        }
    }
}
