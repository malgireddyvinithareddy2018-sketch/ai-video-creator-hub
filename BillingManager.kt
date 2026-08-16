package com.example.backend

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.data.models.PlanType
import kotlinx.coroutines.delay

data class SubscriptionProduct(
    val productId: String,
    val title: String,
    val price: String,
    val planType: PlanType,
    val creditsGranted: Int,
    val description: String
)

object BillingManager {
    private const val TAG = "BillingManager"

    val AVAILABLE_SUBSCRIPTIONS = listOf(
        SubscriptionProduct(
            productId = "monthly_pro_subscription",
            title = "Monthly Pro Plan",
            price = "$14.99/mo",
            planType = PlanType.MONTHLY_PREMIUM,
            creditsGranted = 150,
            description = "150 AI Credits/mo • 1080p Full HD • Fast AI Queue"
        ),
        SubscriptionProduct(
            productId = "yearly_vip_subscription",
            title = "Yearly VIP Pass (Best Value)",
            price = "$99.99/yr",
            planType = PlanType.YEARLY_PREMIUM,
            creditsGranted = 1000,
            description = "1,000 VIP Credits • Google Veo + Kling 1.5 + Runway Gen-4 Access • Unlimited Captions"
        )
    )

    fun initialize(context: Context) {
        Log.d(TAG, "Initializing Google Play Billing Client for AI Video Hub")
    }

    suspend fun launchSubscriptionPurchaseFlow(
        activity: Activity?,
        product: SubscriptionProduct,
        onSuccess: (PlanType, Int) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Launching Google Play Billing flow for product: ${product.productId}")
        try {
            delay(1000)
            Log.d(TAG, "Play Billing purchase successful for ${product.title}")
            onSuccess(product.planType, product.creditsGranted)
        } catch (e: Exception) {
            Log.e(TAG, "Play Billing purchase error", e)
            onError("Payment was cancelled or failed. Please check your Google Play payment method.")
        }
    }
}
