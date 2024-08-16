package com.example.androideksamen.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androideksamen.data.Favorite

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM FavoriteItem")
    suspend fun getFavorites(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun removeFavorite(favorite: Favorite)
}
