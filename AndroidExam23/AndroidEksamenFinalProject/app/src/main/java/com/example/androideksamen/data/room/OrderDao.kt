package com.example.androideksamen.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androideksamen.data.Order

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("SELECT * FROM OrderHistory WHERE :orderId = orderId")
    fun getOrderById(orderId: Int): Order?

    @Query("SELECT * FROM OrderHistory")
    suspend fun getOrders(): List<Order>

}
