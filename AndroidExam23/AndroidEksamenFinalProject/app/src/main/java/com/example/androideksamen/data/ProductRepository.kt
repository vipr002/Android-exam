package com.example.androideksamen.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.androideksamen.data.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRepository {

    private val _httpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    private val _retrofit =
        Retrofit.Builder()
            .client(_httpClient)
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val _productService = _retrofit.create(ProductService::class.java)

    private lateinit var _appDatabase: AppDatabase
    private val _productDao by lazy { _appDatabase.productDao() }
    private val _cartDao by lazy { _appDatabase.cartDao() }
    private val _favoriteDao by lazy { _appDatabase.favoriteDao() }
    private val _orderDao by lazy { _appDatabase.orderDao() }

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }


    // ---- Products ----

    suspend fun getProducts(): List<Product>  {
      try {
          val response = _productService.getAllProducts()
        if (response.isSuccessful) {
            val products = response.body() ?: emptyList()

            _productDao.insertProducts(products)

            return _productDao.getProducts()
            }
         else {
            throw Exception("Response was not successful")
        }} catch (e: Exception) {
            Log.e("ProductRepository", "Failed to get list of products", e)
         return _productDao.getProducts()
      }
    }

    suspend fun getProductById(productId: Int): Product? {
        return _productDao.getProductById(productId)
    }

    suspend fun getProductsById(idList: List<Int>): List<Product> {
        return _productDao.getProductsById(idList)
    }

    suspend fun updateProductRating(id: Int, rating: Int){
        _productDao.updateProductRating(id, rating)
    }


    // ---- Shopping cart ----

    suspend fun getCartItems(): List<CartItem>{
        return _cartDao.getCartItems()
    }

    suspend fun addCartItem(productId: Int) {
        val existingItem = _cartDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            existingItem.quantity++
            _cartDao.updateCartItemQuantity(productId, existingItem.quantity)
        } else {
            _cartDao.insertCartItem(CartItem(productId, 1))
        }
    }
    fun getTotalCartSum(): Flow<Double> = flow {
        val cartItems = getCartItems()
        var totalSum = 0.0
        for (cartItem in cartItems) {
            val product = getProductById(cartItem.productId)
            if (product != null) {
                totalSum += product.price * cartItem.quantity
            }
        }
        emit(totalSum)
    }

    suspend fun removeCartItem(cartItem : CartItem){
        _cartDao.removeCartItem(cartItem)
    }
    suspend fun getCartItemByProductId(productId: Int): CartItem? {
        return _cartDao.getCartItemByProductId(productId)
    }


    // ---- Favorites ----

    suspend fun getFavorites(): List<Favorite> {
        return _favoriteDao.getFavorites()
    }

    suspend fun addFavorite(favorite: Favorite) {
        _favoriteDao.insertFavorite(favorite)
    }

    suspend fun removeFavorite(favorite: Favorite){
        _favoriteDao.removeFavorite(favorite)
    }


    // ---- Orders ----

    suspend fun addOrder(order : Order){
        _orderDao.insertOrder(order)
    }

    suspend fun getOrders(): List<Order> {
        return _orderDao.getOrders()
    }

    suspend fun getOrderById(orderId: Int): Order? {
        return _orderDao.getOrderById(orderId)

    }
}