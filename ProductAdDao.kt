package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.ProductAd
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductAdDao {
    @Query("SELECT * FROM product_ads ORDER BY createdAt DESC")
    fun getAllProductAds(): Flow<List<ProductAd>>

    @Query("SELECT * FROM product_ads WHERE id = :adId LIMIT 1")
    suspend fun getAdById(adId: String): ProductAd?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAd(ad: ProductAd)

    @Query("DELETE FROM product_ads WHERE id = :adId")
    suspend fun deleteAd(adId: String)
}
