package com.example.data.models

data class BackupRecord(
    val id: String,
    val backupName: String,
    val backupType: String, // "Full Auto Backup", "Manual Backup", "Project Backup"
    val timestamp: String,
    val sizeMb: Double,
    val imagesCount: Int,
    val videosCount: Int,
    val promptsCount: Int,
    val settingsCount: Int,
    val storageBucket: String = "gs://ai-studio-pro-backup.appspot.com",
    val deviceName: String = "Android Device (Current)",
    val isAutoSynced: Boolean = true
)

data class BackupSettings(
    val autoBackupEnabled: Boolean = true,
    val backupFrequency: String = "Daily", // "Realtime", "Daily", "Weekly"
    val backupImages: Boolean = true,
    val backupVideos: Boolean = true,
    val backupPrompts: Boolean = true,
    val backupSettings: Boolean = true,
    val wifiOnly: Boolean = true,
    val lastSyncTime: String = "Just now",
    val storageUsedMb: Double = 142.5,
    val storageTotalMb: Double = 5120.0 // 5 GB Free
)
