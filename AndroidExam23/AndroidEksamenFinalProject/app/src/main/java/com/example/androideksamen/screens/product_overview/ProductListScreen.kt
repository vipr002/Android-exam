package com.example.androideksamen.screens.product_overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductOverviewViewModel,
    onProductClick: (productId: Int) -> Unit = {},
    navigateToCartList: () -> Unit = {},
    navigateToFavoriteList: () -> Unit = {},
    navigateToOrderHistory: () -> Unit = {}
) {
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val products by viewModel.produkts.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Products",
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.loadProducts() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Products"
                    )
                }
                IconButton(
                    onClick = { navigateToCartList() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = Color.Black
                    )
                }
                IconButton(
                    onClick = { navigateToFavoriteList() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
                IconButton(
                    onClick = { navigateToOrderHistory() }
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Order History"
                    )
                }
                IconButton(
                    onClick = { viewModel.toggleSearch() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
        }

        Divider()

        Row {
            Text(modifier = Modifier
                .clickable {viewModel.setCategory(null)
                    viewModel.loadProducts() }
                .padding(8.dp),
                text = "All Products", maxLines = 1)

            Text(modifier = Modifier
                .clickable {viewModel.setCategory("women's clothing") }
                .padding(8.dp),
                text = "Women", maxLines = 1)

            Text(modifier = Modifier
                .clickable {viewModel.setCategory("men's clothing") }
                .padding(8.dp),
                text = "Men", maxLines = 1)

            Text(modifier = Modifier
                .clickable {viewModel.setCategory("jewelery") }
                .padding(8.dp),
                text = "Jewelery", maxLines = 1)

            Text(modifier = Modifier
                .clickable {viewModel.setCategory("electronics") }
                .padding(8.dp),
                text = "Electronics", maxLines = 1)
        }

        Divider()
        // https://www.youtube.com/watch?v=3oXBnM6fZj0&ab_channel=Stevdza-San
        if (isSearching){
            TextField(value = searchText,
                onValueChange =
                viewModel::onSearchTextChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search")},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.setIsSearching(false)
                    }
                )
            )
        }

        Divider()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product
                ) { id ->
                    navController.navigate("ProductDetailsScreen/$id")
                }
            }
        }
    }
}
