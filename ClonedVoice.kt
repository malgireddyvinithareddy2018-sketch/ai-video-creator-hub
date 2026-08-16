package com.example.data.models

data class ClonedVoice(
    val id: String,
    val name: String,
    val gender: String, // Male, Female, Child, Non-Binary
    val sampleFileName: String,
    val primaryLanguage: String,
    val emotionTone: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
