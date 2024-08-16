package com.example.androideksamen.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androideksamen.data.CartItem
import com.example.androideksamen.data.Favorite
import com.example.androideksamen.data.Order
import com.example.androideksamen.data.Product

@Database(
    entities = [Product::class, CartItem::class, Favorite::class, Order::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(ProductListConverter::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun orderDao(): OrderDao
}