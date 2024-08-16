package com.example.androideksamen.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Products")
data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val image: String?,
    val category: String,
    val description: String,
    private var _rating: Int = 0
){
    var rating
        get() = _rating
        set(value) {
            rating = value
        }


    fun doesMatchQuery(query : String): Boolean {
        val matchingCombinations = listOf(
            "$title"
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

