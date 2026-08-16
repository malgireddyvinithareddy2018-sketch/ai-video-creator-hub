package com.example.backend

import android.app.Activity
import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay

object AdMobManager {
    private const val TAG = "AdMobManager"
    // Standard AdMob Test Rewarded Ad Unit ID
    const val REWARDED_AD_TEST_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    var isAdLoaded: Boolean = true
        private set

    fun initialize(context: Context) {
        Log.d(TAG, "Initializing Google Mobile Ads SDK with test unit ID: $REWARDED_AD_TEST_UNIT_ID")
        isAdLoaded = true
    }

    /**
     * Simulates or executes Google AdMob Rewarded Video playback
     */
    suspend fun showRewardedAd(
        activity: Activity?,
        onRewardEarned: (Int) -> Unit,
        onAdFailed: (String) -> Unit
    ) {
        Log.d(TAG, "Requesting AdMob Rewarded Video display...")
        try {
            // Simulate video ad display duration (1.5 seconds)
            delay(1500)
            val rewardedAmount = 2 // +2 Credits awarded per ad
            Log.d(TAG, "Rewarded video completed! User earned $rewardedAmount credits.")
            onRewardEarned(rewardedAmount)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display AdMob Rewarded Ad", e)
            onAdFailed("Ad playback was interrupted or unavailable. Please try again.")
        }
    }
}
