package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GenerationType {
    TEXT_TO_VIDEO,
    IMAGE_TO_VIDEO,
    TEXT_TO_IMAGE,
    PROMPT_TO_VIDEO,
    PROMPT_IMAGE_TO_VIDEO,
    CHARACTER,
    VOICE,
    SUBTITLES,
    MUSIC,
    PROMPT_GEN,
    PRODUCT_TO_VIDEO,
    CONTENT_IDEA,
    VIRAL_HOOK,
    SCRIPT,
    YT_TITLE,
    IG_CAPTION,
    HASHTAGS,
    CONTENT_CALENDAR,
    PRODUCT_SCRIPT,
    TALKING_PHOTO,
    THUMBNAIL,
    VIDEO_DUBBING,
    AVATAR_GENERATOR,
    STORY_GENERATOR,
    VIDEO_TEMPLATE,
    REEL_MAKER,
    PODCAST_GENERATOR,
    CHARACTER_MASTER,
    VOICE_CLONE,
    SCRIPT_WRITER,
    TITLE_HASHTAG
}

@Entity(tableName = "generations")
data class GenerationItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: GenerationType,
    val title: String,
    val prompt: String,
    val resultText: String = "",
    val resultUrl: String = "",
    val thumbnailColor: Long = 0xFF7000FF,
    val durationSeconds: Int = 10,
    val aspectRatio: String = "16:9",
    val styleName: String = "Realistic",
    val creditsSpent: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)
