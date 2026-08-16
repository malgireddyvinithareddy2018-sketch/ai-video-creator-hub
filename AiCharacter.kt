package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_characters")
data class AiCharacter(
    @PrimaryKey
    val id: String, // e.g. "CHAR-78A9B"
    val name: String,
    val style: String, // "Human", "3D Cartoon", "Anime"
    val gender: String, // "Male", "Female", "Non-Binary"
    val description: String,
    val seedPrompt: String,
    val primaryImageUrl: String,
    
    // Angle Views
    val frontViewUrl: String = "",
    val leftSideViewUrl: String = "",
    val rightSideViewUrl: String = "",
    val backViewUrl: String = "",
    val fortyFiveDegreeViewUrl: String = "",
    
    // Expressions
    val happyExpressionUrl: String = "",
    val sadExpressionUrl: String = "",
    val angryExpressionUrl: String = "",
    val fearfulExpressionUrl: String = "",
    
    // Actions
    val talkingActionUrl: String = "",
    val walkingActionUrl: String = "",
    val runningActionUrl: String = "",
    val sittingActionUrl: String = "",
    
    val createdAt: Long = System.currentTimeMillis()
)
