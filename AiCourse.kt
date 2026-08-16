package com.example.data.models

data class CourseLesson(
    val id: String,
    val lessonNumber: Int,
    val title: String,
    val durationMinutes: Int = 12,
    val videoScript: String,
    val keyTakeaways: List<String>,
    val quizQuestion: String,
    val quizOptions: List<String>,
    val correctAnswerIndex: Int,
    val workbookExercise: String
)

data class CourseModule(
    val id: String,
    val moduleNumber: Int,
    val title: String,
    val description: String,
    val lessons: List<CourseLesson>
)

data class AiCourse(
    val id: String,
    val topic: String,
    val targetAudience: String,
    val skillLevel: String, // "Beginner", "Intermediate", "Advanced"
    val modules: List<CourseModule>,
    val totalLessonsCount: Int,
    val estimatedHours: Double,
    val createdAt: String,
    val isPdfExported: Boolean = false
)
