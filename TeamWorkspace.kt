package com.example.data.models

data class TeamMember(
    val id: String,
    val name: String,
    val email: String,
    val role: String, // "Admin", "Editor", "Viewer"
    val avatarInitials: String,
    val joinedAt: String,
    val isOnline: Boolean = true
)

data class SharedProject(
    val id: String,
    val name: String,
    val mediaType: String, // "AI Video", "Master Character", "Reel / Short", "Product Ad"
    val creatorName: String,
    val lastModified: String,
    val status: String // "In Progress", "Ready for Review", "Approved / Exported"
)

data class TeamChatMessage(
    val id: String,
    val senderName: String,
    val senderRole: String,
    val message: String,
    val timestamp: String,
    val isSelf: Boolean = false
)

data class TeamActivity(
    val id: String,
    val memberName: String,
    val action: String,
    val target: String,
    val timestamp: String
)

data class TeamWorkspace(
    val id: String,
    val name: String,
    val description: String,
    val inviteCode: String,
    val ownerEmail: String,
    val members: List<TeamMember>,
    val sharedProjects: List<SharedProject>,
    val chatMessages: List<TeamChatMessage>,
    val activities: List<TeamActivity>
)
