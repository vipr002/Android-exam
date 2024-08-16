package com.example.androideksamen.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShoppingCart")
data class CartItem(
    @PrimaryKey
    val productId: Int,
    var quantity: Int
)