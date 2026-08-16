package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.AiCharacter
import kotlinx.coroutines.flow.Flow

@Dao
interface AiCharacterDao {
    @Query("SELECT * FROM ai_characters ORDER BY createdAt DESC")
    fun getAllCharacters(): Flow<List<AiCharacter>>

    @Query("SELECT * FROM ai_characters WHERE id = :characterId LIMIT 1")
    suspend fun getCharacterById(characterId: String): AiCharacter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: AiCharacter)

    @Query("DELETE FROM ai_characters WHERE id = :characterId")
    suspend fun deleteCharacter(characterId: String)
}
