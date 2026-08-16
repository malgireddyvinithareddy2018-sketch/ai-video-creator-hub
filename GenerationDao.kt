package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.ContentIdea
import com.example.data.models.GenerationItem
import com.example.data.models.GenerationType
import kotlinx.coroutines.flow.Flow

@Dao
interface GenerationDao {
    @Query("SELECT * FROM generations ORDER BY timestamp DESC")
    fun getAllGenerations(): Flow<List<GenerationItem>>

    @Query("SELECT * FROM generations WHERE type = :type ORDER BY timestamp DESC")
    fun getGenerationsByType(type: GenerationType): Flow<List<GenerationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneration(item: GenerationItem): Long

    @Query("DELETE FROM generations WHERE id = :id")
    suspend fun deleteGeneration(id: Long)

    @Query("DELETE FROM generations")
    suspend fun clearHistory()

    // Content Calendar
    @Query("SELECT * FROM content_calendar ORDER BY timestamp DESC")
    fun getAllContentIdeas(): Flow<List<ContentIdea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentIdea(idea: ContentIdea): Long

    @Query("UPDATE content_calendar SET isCompleted = :completed WHERE id = :id")
    suspend fun updateIdeaCompletion(id: Long, completed: Boolean)

    @Query("DELETE FROM content_calendar WHERE id = :id")
    suspend fun deleteContentIdea(id: Long)
}
