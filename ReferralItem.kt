package com.example.data.models

data class ReferralItem(
    val id: String = "REF_${System.currentTimeMillis()}",
    val friendName: String,
    val friendEmail: String,
    val status: String = "Completed (+5 Credits)",
    val creditsEarned: Int = 5,
    val affiliateCommission: Double = 2.50,
    val timestamp: Long = System.currentTimeMillis()
)

data class ReferralStats(
    val referralCode: String = "AIVIDEO-REF-7892",
    val totalInvites: Int = 12,
    val successfulSignups: Int = 8,
    val totalCreditsEarned: Int = 40,
    val pendingCredits: Int = 10,
    val affiliateEarningsUsd: Double = 35.00,
    val affiliateTier: String = "Gold Ambassador (20% RevShare)"
)
