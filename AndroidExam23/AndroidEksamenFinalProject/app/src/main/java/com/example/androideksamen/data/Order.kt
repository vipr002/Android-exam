package com.example.androideksamen.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androideksamen.data.room.ProductListConverter

@Entity(tableName = "OrderHistory")
@TypeConverters(ProductListConverter::class)

    data class Order(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val orderDate: String,
    @TypeConverters(ProductListConverter::class)
    val items: List<OrderItem>
)

data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val orderItemId: Long = 0,
    val product: Product,
    val quantity: Int,
)


