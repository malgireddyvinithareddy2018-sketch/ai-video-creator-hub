package com.example.data.models

enum class PlanType {
    FREE,
    MONTHLY_PREMIUM,
    YEARLY_PREMIUM
}

enum class LoginType {
    EMAIL,
    GOOGLE,
    GUEST
}

data class User(
    val id: String = "user_default_123",
    val email: String = "creator@aivideo.hub",
    val name: String = "AI Video Creator",
    val loginType: LoginType = LoginType.GUEST,
    val credits: Int = 20,
    val planType: PlanType = PlanType.FREE,
    val rewardedAdsToday: Int = 0,
    val lastRewardedAdResetTime: Long = System.currentTimeMillis(),
    val isAdmin: Boolean = true
) {
    val isPremium: Boolean
        get() = planType != PlanType.FREE
}
