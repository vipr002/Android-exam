package com.example.androideksamen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androideksamen.data.ProductRepository
import com.example.androideksamen.screens.favorite_list.FavoriteListScreen
import com.example.androideksamen.screens.favorite_list.FavoriteListViewModel
import com.example.androideksamen.screens.order_details.OrderDetailsViewModel
import com.example.androideksamen.screens.order_history.OrderHistoryScreen
import com.example.androideksamen.screens.order_history.OrderHistoryViewModel
import com.example.androideksamen.screens.product_details.ProductDetailsScreen
import com.example.androideksamen.screens.product_details.ProductDetailsViewModel
import com.example.androideksamen.screens.product_overview.ProductListScreen
import com.example.androideksamen.screens.product_overview.ProductOverviewViewModel
import com.example.androideksamen.screens.shopping_cart.ShoppingCartScreen
import com.example.androideksamen.screens.shopping_cart.ShoppingCartViewModel
import com.example.androideksamen.screens.order_details.OrderDetailsScreen


class MainActivity : ComponentActivity() {
    private val _overviewViewModel: ProductOverviewViewModel by viewModels()
    private val _productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val _favoriteListViewModel: FavoriteListViewModel by viewModels()
    private val _shoppingCartViewModel: ShoppingCartViewModel by viewModels()
    private val _orderHistoryViewModel: OrderHistoryViewModel by viewModels()
    private val _orderDetailsViewModel : OrderDetailsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProductRepository.initializeDatabase(applicationContext)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "ProductListScreen") {

                composable("ProductListScreen") {
                    ProductListScreen(
                        navController,
                        _overviewViewModel,
                            navigateToCartList = {
                                navController.navigate("ShoppingCartScreen")
                            },
                            navigateToFavoriteList = {
                        navController.navigate("favoriteListScreen")
                    },
                        navigateToOrderHistory = {
                            navController.navigate("orderHistoryScreen")
                        }
                    )
                }

                composable("ProductDetailsScreen/{productId}") { backStackEntry ->
                    val productId =
                        backStackEntry.arguments?.getString("productId") ?: return@composable

                    LaunchedEffect(productId) {
                        _productDetailsViewModel.setSelectedProduct(productId)
                    }
                    ProductDetailsScreen(
                        productId,
                        viewModel = _productDetailsViewModel,
                        onBackButtonClick = { navController.popBackStack() }
                        )
                }

                composable("ShoppingCartScreen") {
                    LaunchedEffect(Unit) {
                        _shoppingCartViewModel.loadCartItems()
                    }
                    ShoppingCartScreen(
                        viewModel = _shoppingCartViewModel,
                        onBackButtonClick = { navController.popBackStack() },
                        onProductClick = { productId ->
                            navController.navigate("productDetailsScreen/$productId")
                        },
                        onPurchaseClick = {
                            _shoppingCartViewModel.purchase()
                        }
                        )
                }

                composable(route = "favoriteListScreen") {
                    LaunchedEffect(Unit) {
                        _favoriteListViewModel.loadFavorites()
                    }
                    FavoriteListScreen(
                        viewModel = _favoriteListViewModel,
                        onBackButtonClick = { navController.popBackStack() },
                        onProductClick = { productId ->
                            navController.navigate("productDetailsScreen/$productId")
                        }
                    )
                }
                composable(route = "orderHistoryScreen") {
                    LaunchedEffect(Unit) {
                        _orderHistoryViewModel.loadOrders()
                    }
                    OrderHistoryScreen(
                        viewModel = _orderHistoryViewModel,
                        onBackButtonClick = { navController.popBackStack() },
                        onProductClick = { productId ->
                            navController.navigate("productDetailsScreen/$productId")
                        },
                        navController = navController
                    )
                }
                composable("OrderDetailsScreen/{orderId}") { backStackEntry ->
                    val orderId =
                        backStackEntry.arguments?.getString("orderId") ?: return@composable

                    LaunchedEffect(orderId) {
                        _orderDetailsViewModel.setSelectedOrder(orderId.toInt())
                    }
                    OrderDetailsScreen(
                        id = orderId.toInt(),
                        viewModel = _orderDetailsViewModel,
                        onBackButtonClick = { navController.popBackStack() },
                        navController = navController
                    )
                }
            }
        }
    }
}