package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_ads")
data class ProductAd(
    @PrimaryKey
    val id: String, // e.g. "AD-9821A"
    val productName: String,
    val templateCategory: String, // "Beauty", "Fashion", "Electronics", "Home & Kitchen", "Kids", "Digital", "Affiliate"
    val productUrl: String,
    val imageUrl: String,
    val features: String,
    val benefits: String,
    val price: String,
    val offer: String,
    val adDurationSeconds: Int, // 10, 15, 30, 60
    val outputFormat: String, // "Instagram Reel (9:16)", "YouTube Shorts (9:16)", "Facebook Ad", "WhatsApp Status", "Product Promo Video (16:9)"
    val adLanguage: String, // "Telugu", "English", "Hindi"
    val bgMusicVibe: String, // "Upbeat Commercial", "Energetic Tech", "Luxury Acoustic", "Chill Ambient"
    
    // Auto-Generated Marketing Components
    val marketingHook: String,
    val productScript: String,
    val voiceoverText: String,
    val subtitlesSrt: String,
    val ctaText: String,
    val videoResultUrl: String,
    
    // Brand Identity
    val brandName: String = "My Brand",
    val brandLogoUrl: String = "",
    val brandColorHex: String = "#7C3AED",
    
    val createdAt: Long = System.currentTimeMillis()
)

data class BrandAsset(
    val brandName: String = "My Studio Brand",
    val logoUrl: String = "https://aivideocreator.hub/brand/logo.png",
    val primaryColorHex: String = "#7C3AED",
    val tagline: String = "Premium E-commerce Products"
)

data class ProductAdTemplatePreset(
    val templateId: String,
    val categoryName: String,
    val exampleTitle: String,
    val exampleUrl: String,
    val exampleFeatures: String,
    val exampleBenefits: String,
    val examplePrice: String,
    val exampleOffer: String,
    val defaultHook: String,
    val iconName: String
)
