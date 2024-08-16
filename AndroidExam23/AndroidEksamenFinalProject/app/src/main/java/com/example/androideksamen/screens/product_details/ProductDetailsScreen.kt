package com.example.androideksamen.screens.product_details

import android.annotation.SuppressLint
import android.util.Log
import android.widget.RatingBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androideksamen.data.Product

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductDetailsScreen(
    id: String,
    onBackButtonClick: () -> Unit = {},
    viewModel: ProductDetailsViewModel = viewModel(),
) {

    val loading = viewModel.loading.collectAsState()
    val isFavorite = viewModel.isFavorite.collectAsState()
    val product by viewModel.product.collectAsState()
    val rating = viewModel.rating.collectAsState().value

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
            viewModel.loadProduct(it.toInt())
        }
    }

    if(product == null) {
        Text(text = "Failed to get product details. Selected product object is NULL!")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
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
                    contentDescription = "Back arrow"
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Product Details",
                style = MaterialTheme.typography.titleLarge
            )
        }
        product?.let {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row{
                    AsyncImage(
                        modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)
                            .background(color = Color.White)
                            .padding(15.dp),
                        model = product!!.image,
                        alignment = Alignment.Center,
                        contentDescription = "Image of ${it.title}"
                    )
                    IconButton(
                        onClick = { viewModel.updateFavorite(it.id) }
                    ) {

                        Icon(
                            imageVector = if (isFavorite.value) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "Update Favorite",
                            tint = Color.Red
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, text = "${it.title}")

                    Spacer(modifier = Modifier.height(20.dp))

                    Rating(
                        rating = rating,
                        ratingChanged = {
                            viewModel.setRating(id.toInt(), it)
                        }
                    )
                }
                Text(fontSize = 15.sp, text = "${it.description}")
                Spacer(modifier = Modifier.height(20.dp))

                Text("Product Price: ${it.price}")

                IconButton(
                    modifier = Modifier
                        .width(150.dp),
                    onClick = { product?.let { viewModel.addToCart(it.id) } }
                ) {
                    Row (
                        modifier = Modifier
                            .width(150.dp)
                            .background(color = Color.DarkGray),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)){
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Add to Cart",
                            tint = Color.White
                        )
                        Text(text = "Add to Cart", color = Color.White)
                    }
                }
            }
        } ?: Text("Loading product details...")
    }
}

// ---- Rating ----

// https://www.jetpackcompose.app/snippets/RatingBar
// https://www.youtube.com/watch?v=bhlQq5s0WHw&ab_channel=AhmedGuedmioui

@Composable
fun Rating(
    rating: Int,
    ratingChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Rate this product:")
        (1..5).forEach { starNumber ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (starNumber <= rating) Color.Yellow else Color.Gray,
                modifier = Modifier.clickable {
                    ratingChanged(starNumber)
                }
            )
        }
    }
}

