package com.example.androideksamen.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteItem")
    data class Favorite(
        @PrimaryKey
        val productId: Int
    )

