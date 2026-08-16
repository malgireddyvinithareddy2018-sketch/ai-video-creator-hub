package com.example.data.models

data class ConnectedAccount(
    val id: String,
    val platform: String, // "YouTube", "Instagram", "Facebook", "Telegram"
    val accountName: String,
    val handle: String,
    val isConnected: Boolean,
    val followers: String
)

data class SocialPost(
    val id: String,
    val title: String,
    val description: String,
    val hashtags: List<String>,
    val platforms: List<String>,
    val mediaType: String, // "Video", "Shorts/Reel", "Image", "Post"
    val mediaUrl: String? = null,
    val scheduledTime: String? = null,
    val status: String, // "Published", "Scheduled", "Failed"
    val publishedAt: String,
    val views: Int = 0,
    val likes: Int = 0
)
