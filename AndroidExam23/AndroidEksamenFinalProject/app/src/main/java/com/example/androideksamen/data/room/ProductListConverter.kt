package com.example.androideksamen.data.room

import androidx.room.TypeConverter
import com.example.androideksamen.data.OrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ProductListConverter {

    @TypeConverter
    fun fromList(products: List<OrderItem>): String {
        return Gson().toJson(products)
    }

    @TypeConverter
    fun toList(jsonString: String): List<OrderItem> {
        val listType = object : TypeToken<List<OrderItem>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}








