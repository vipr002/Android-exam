package com.example.androideksamen.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.androideksamen.data.CartItem

@Dao
interface CartDao {

    @Query("SELECT * FROM ShoppingCart")
    suspend fun getCartItems(): List<CartItem>

    @Insert
    suspend fun insertCartItem(cartItem: CartItem)

    @Delete
    suspend fun removeCartItem(cartItem: CartItem)

    @Query("UPDATE ShoppingCart SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateCartItemQuantity(productId: Int, quantity: Int)

    @Query("SELECT * FROM ShoppingCart WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: Int): CartItem?

}
