package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_calendar")
data class ContentIdea(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val platform: String, // YouTube, Instagram, TikTok, LinkedIn
    val category: String, // Tech, Lifestyle, Business, Gaming, Education
    val scheduledDate: String,
    val hook: String,
    val scriptText: String,
    val hashtags: String,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
