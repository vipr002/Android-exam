package com.example.androideksamen.screens.order_details


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androideksamen.screens.order_details.OrderDetailsViewModel
import com.example.androideksamen.screens.order_history.OrderItem
import com.example.androideksamen.screens.product_details.ProductDetailsViewModel
import com.example.androideksamen.screens.product_overview.ProductItem

@Composable
fun OrderDetailsScreen(
    id: Int,
    onBackButtonClick: () -> Unit = {},
    viewModel: OrderDetailsViewModel = viewModel(),
    navController: NavController,
) {

    val loading = viewModel.loading.collectAsState()
    val order by viewModel.order.collectAsState()

    // ------------------------------

    if(loading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(id) {
        id.let {
            viewModel.loadOrder(it)
        }
    }

    if(order == null) {
        Text(text = "Failed to get order details. Selected order object is NULL!")
        return
    }
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
                text = "Order",
                style = MaterialTheme.typography.titleLarge
            )
        }
        
        Divider()

        for(x in order!!.items){

            ProductItem(product = x.product){ id ->
                navController.navigate("ProductDetailsScreen/$id")
            }
            Text(text = "Quantity: " + x.quantity.toString())
        }
        }
}

