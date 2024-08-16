package com.example.androideksamen.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androideksamen.data.Product


@Dao
interface ProductDao {

    @Query("SELECT * FROM Products")
    fun getProducts(): List<Product>

    @Query("SELECT * FROM Products WHERE :productId = id")
    fun getProductById(productId: Int): Product?

    @Query("SELECT * FROM Products WHERE id IN (:productIds)")
    suspend fun getProductsById(productIds: List<Int>): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Query("UPDATE Products SET _rating = :rating WHERE id = :id")
    suspend fun updateProductRating(id: Int, rating: Int)
}