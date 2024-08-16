package com.example.androideksamen.screens.shopping_cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androideksamen.data.Order
import com.example.androideksamen.data.OrderItem
import com.example.androideksamen.data.Product
import com.example.androideksamen.data.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar


class ShoppingCartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems = _cartItems.asStateFlow()
    // https://www.baeldung.com/kotlin/current-date-time
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val current = formatter.format(time)


    // ---- Cart Items ----

    fun loadCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val listOfCartIds = ProductRepository.getCartItems().map { it.productId }
            _cartItems.value = ProductRepository.getProductsById(listOfCartIds)
        }
    }
    fun removeFromCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItem = ProductRepository.getCartItemByProductId(productId)
            cartItem?.let {
                ProductRepository.removeCartItem(it)
                loadCartItems()
            }
        }
    }

    // ---- Getting quantity && total sum in cart ----

    fun getQuantityByProductId(productId: Int): Flow<Int> = flow {
        val cartItem = ProductRepository.getCartItemByProductId(productId)
        emit(cartItem?.quantity ?: 0)
    }

    fun totalCartSum(): Flow<Double> = cartItems.map { cartProducts ->
        cartProducts.sumByDouble { product ->
            val price = product.price
            val quantity = getQuantityByProductId(product.id).first()
            price * quantity
        }
    }

        // --- Purchasing products ----

        fun purchase() {
            viewModelScope.launch(Dispatchers.Main) {
                val cartItems = _cartItems.value
                val orderItems = ArrayList<OrderItem>()

                for (item in cartItems) {
                    val quantity = getQuantityByProductId(item.id).first()
                    orderItems.add(OrderItem(product = item, quantity = quantity))
                }

                val newOrder = Order(items = orderItems, orderDate = current)

                ProductRepository.addOrder(newOrder)

                _cartItems.value.forEach { removeFromCart(it.id) }
                _cartItems.value = emptyList()
            }
        }
    }


