package com.example.androideksamen.screens.shopping_cart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androideksamen.data.Product
import com.example.androideksamen.screens.product_overview.ProductItem

@Composable
fun ShoppingCartScreen(
    viewModel: ShoppingCartViewModel = viewModel(),
    onBackButtonClick: () -> Unit = {},
    onProductClick: (productId: Int) -> Unit = {},
    onPurchaseClick: () -> Unit,
) {
    val selectedItems = remember { mutableStateListOf<Product>() }
    val productsState = viewModel.cartItems.collectAsState()
    val currentPrice = viewModel.totalCartSum().collectAsState(0.0)


    // ------------------------------

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackButtonClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Shopping Cart",
                style = MaterialTheme.typography.titleLarge
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(productsState.value) { product ->
                val quantity =
                    viewModel.getQuantityByProductId(product.id).collectAsState(initial = 0)
                ProductItem(
                    product = product
                ) {
                    onProductClick(product.id)
                }
                Row {
                    IconButton(
                        onClick = {
                            product?.let {
                                viewModel.removeFromCart(product.id)
                                selectedItems.remove(product)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove from Cart",
                            tint = Color.Red
                        )
                    }
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Remove from Cart.    Quantity: ${quantity.value}"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            modifier = Modifier.padding(8.dp),
            text = "Total Price: ${currentPrice.value}"
        )
        Button(
            modifier = Modifier
                .padding(8.dp)
                .width(160.dp)
                .height(40.dp),
            onClick = {
                onPurchaseClick()
                selectedItems.clear()
            }
        ) {
            Text(
                text = "Purchase"
            )
        }
    }
}





