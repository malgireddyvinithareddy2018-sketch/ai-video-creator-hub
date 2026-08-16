package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.models.AiCharacter
import com.example.data.models.ContentIdea
import com.example.data.models.GenerationItem
import com.example.data.models.ProductAd

@Database(entities = [GenerationItem::class, ContentIdea::class, AiCharacter::class, ProductAd::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun generationDao(): GenerationDao
    abstract fun aiCharacterDao(): AiCharacterDao
    abstract fun productAdDao(): ProductAdDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_video_creator_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
