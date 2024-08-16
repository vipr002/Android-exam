package com.example.androideksamen.screens.favorite_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androideksamen.screens.product_overview.ProductItem


@Composable
fun FavoriteListScreen(
    viewModel: FavoriteListViewModel,
    onBackButtonClick: () -> Unit = {},
    onProductClick: (filmId: Int) -> Unit = {},
) {
    val products = viewModel.favoriteItems.collectAsState()

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
                text = "Favorites",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Divider()

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(products.value) { product ->
                ProductItem(
                        product = product
                ) {
                    onProductClick(product.id)
                }
            }
        }
    }
}