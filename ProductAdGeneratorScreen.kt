package com.example.ui.screens.generators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ProductAd
import com.example.data.models.ProductAdTemplatePreset
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

val productAdTemplatesList = listOf(
    ProductAdTemplatePreset(
        templateId = "beauty",
        categoryName = "Beauty Products",
        exampleTitle = "Glow Vitamin C Serum",
        exampleUrl = "https://amazon.com/dp/B08GLOWSERUM",
        exampleFeatures = "Pure Vitamin C, Hyaluronic Acid, Organic Rosehip Oil",
        exampleBenefits = "Brightens skin in 7 days, reduces dark spots, 24h hydration",
        examplePrice = "₹799 ($14.99)",
        exampleOffer = "Flat 40% Off + Free Shipping",
        defaultHook = "✨ Stop hiding your skin! This 7-day serum gives instant natural glass skin glow...",
        iconName = "Beauty"
    ),
    ProductAdTemplatePreset(
        templateId = "fashion",
        categoryName = "Fashion Products",
        exampleTitle = "Oversized Streetwear Hoodie",
        exampleUrl = "https://shopify.store/products/urban-hoodie",
        exampleFeatures = "100% Heavyweight Cotton 400 GSM, Puff Print, Fleece Lined",
        exampleBenefits = "Ultra warm, aesthetic fit, premium drop shoulder silhouette",
        examplePrice = "₹1,499 ($24.99)",
        exampleOffer = "Buy 1 Get 1 Free Today Only",
        defaultHook = "🔥 The street style hoodie everyone on Instagram is obsessing over right now...",
        iconName = "Fashion"
    ),
    ProductAdTemplatePreset(
        templateId = "electronics",
        categoryName = "Electronics",
        exampleTitle = "Wireless ANC Earbuds Pro",
        exampleUrl = "https://flipkart.com/item/anc-earbuds-pro",
        exampleFeatures = "45dB Active Noise Cancellation, 50H Playtime, Bluetooth 5.4",
        exampleBenefits = "Zero background noise in gym or flights, punchy bass, crystal clear mic",
        examplePrice = "₹2,499 ($32.99)",
        exampleOffer = "30% Off Code: PROAUDIO",
        defaultHook = "🎧 Silence the entire world with 1 tap! Why pay $200 when these exist?",
        iconName = "Tech"
    ),
    ProductAdTemplatePreset(
        templateId = "home",
        categoryName = "Home & Kitchen",
        exampleTitle = "Smart Digital Air Fryer 5.5L",
        exampleUrl = "https://meesho.com/products/air-fryer-55l",
        exampleFeatures = "Rapid 360° Air Circulation, 12 One-Touch Presets, Non-Stick Basket",
        exampleBenefits = "90% less oil, crispy guilt-free fries & chicken in 12 minutes",
        examplePrice = "₹4,299 ($54.99)",
        exampleOffer = "Extra ₹500 OFF + Free Recipe eBook",
        defaultHook = "🍟 Eat crispy fried food EVERY DAY with zero guilt! Here is the kitchen secret...",
        iconName = "Home"
    ),
    ProductAdTemplatePreset(
        templateId = "kids",
        categoryName = "Kids Products",
        exampleTitle = "Interactive Kids Writing Tablet",
        exampleUrl = "https://amazon.in/dp/B08KIDSTAB",
        exampleFeatures = "Eye-Protection LCD Screen, One-Click Erase, Durable Drop-Proof",
        exampleBenefits = "Keeps kids away from smartphones for hours, boosts creativity",
        examplePrice = "₹499 ($8.99)",
        exampleOffer = "Pack of 2 @ 20% Discount",
        defaultHook = "👶 How I finally reduced my kids' smartphone screen time to ZERO...",
        iconName = "Kids"
    ),
    ProductAdTemplatePreset(
        templateId = "digital",
        categoryName = "Digital Products",
        exampleTitle = "AI Prompt Engineering Vault",
        exampleUrl = "https://gumroad.com/l/aipromptsvault",
        exampleFeatures = "1,000+ Verified Master Prompts, Lifetime Updates, Notion Hub",
        exampleBenefits = "Automates copy, design, code, and marketing in 10 seconds",
        examplePrice = "₹999 ($19.00)",
        exampleOffer = "Instant Lifetime Access",
        defaultHook = "🚀 10x your productivity today! The ultimate AI vault top creators use...",
        iconName = "Digital"
    ),
    ProductAdTemplatePreset(
        templateId = "affiliate",
        categoryName = "Affiliate Products",
        exampleTitle = "Multi-Function Portable Blender",
        exampleUrl = "https://amzn.to/3xyzAffiliate",
        exampleFeatures = "USB-C Rechargeable, 6 Steel Blades, Leak-Proof Travel Lid",
        exampleBenefits = "Fresh smoothies on the go anywhere - office, gym, traveling",
        examplePrice = "₹1,299 ($18.50)",
        exampleOffer = "Flash Sale 50% Off Link Below",
        defaultHook = "🥤 The viral portable blender that makes fresh protein shakes in 30 seconds...",
        iconName = "Affiliate"
    )
)

@Composable
fun ProductAdGeneratorScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.generationState.collectAsState()
    val savedProductAds by viewModel.savedProductAds.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0: Link & Details, 1: Output Formats & AI Settings, 2: Templates & Brand Assets, 3: Ad History

    // Product Input State
    var productUrl by remember { mutableStateOf("https://amazon.com/dp/B08N5WRWNW") }
    var imageUrl by remember { mutableStateOf("https://aivideocreator.hub/input/product_hero.jpg") }
    var productName by remember { mutableStateOf("Smart Wireless ANC Headphones") }
    var category by remember { mutableStateOf("Electronics") }
    var features by remember { mutableStateOf("40dB Active Noise Cancellation, 50h Battery, Fast Charge") }
    var benefits by remember { mutableStateOf("Studio quality sound, zero distraction, comfortable memory foam") }
    var price by remember { mutableStateOf("₹2,999 ($39.99)") }
    var offer by remember { mutableStateOf("Flat 40% OFF + Free Express Shipping") }

    // Generation Config
    var selectedDuration by remember { mutableStateOf(15) } // 10, 15, 30, 60
    var selectedFormat by remember { mutableStateOf("Instagram Reel (9:16)") }
    var selectedLanguage by remember { mutableStateOf("Telugu") } // Telugu, English, Hindi
    var selectedBgMusic by remember { mutableStateOf("Upbeat Commercial") } // Upbeat, Energetic Tech, Luxury Acoustic, Chill Ambient
    var selectedQuality by remember { mutableStateOf("1080p Full HD") } // 480p, 720p HD, 1080p Full HD, 1440p 2K, 2160p 4K

    // Brand Assets
    var brandName by remember { mutableStateOf("TechGear E-Store") }
    var brandLogoUrl by remember { mutableStateOf("https://aivideocreator.hub/brand/logo.png") }
    var brandColorHex by remember { mutableStateOf("#7C3AED") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("product_ad_generator_screen")
    ) {
        // Top Navigation Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1F1A3A))
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("PRODUCT ADVERTISEMENT GENERATOR", fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.White)
                Text("Amazon, Flipkart, Shopify links → High-Converting Video Ads", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("1. Product Input", "2. Ad Config", "3. Templates", "4. Ad History (${savedProductAds.size})").forEachIndexed { idx, label ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                        .clickable { activeTab = idx }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            0 -> { // TAB 1: PRODUCT LINK & DETAILS
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Link, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("1. PRODUCT LINK (AMAZON / FLIPKART / MEESHO / SHOPIFY)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = productUrl,
                            onValueChange = { productUrl = it },
                            placeholder = { Text("https://amazon.in/dp/... or Shopify URL", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B1736),
                                unfocusedContainerColor = Color(0xFF1B1736),
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color(0xFF2E2954),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("2. PRODUCT HERO IMAGE URL / UPLOAD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            placeholder = { Text("https://store.com/hero_product.jpg", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B1736),
                                unfocusedContainerColor = Color(0xFF1B1736),
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color(0xFF2E2954),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("3. PRODUCT DETAILS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Product Name", fontSize = 10.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = productName,
                            onValueChange = { productName = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B1736),
                                unfocusedContainerColor = Color(0xFF1B1736),
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color(0xFF2E2954),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Key Features", fontSize = 10.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = features,
                            onValueChange = { features = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B1736),
                                unfocusedContainerColor = Color(0xFF1B1736),
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color(0xFF2E2954),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Customer Benefits", fontSize = 10.sp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = benefits,
                            onValueChange = { benefits = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B1736),
                                unfocusedContainerColor = Color(0xFF1B1736),
                                focusedBorderColor = PrimaryPurple,
                                unfocusedBorderColor = Color(0xFF2E2954),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Price", fontSize = 10.sp, color = Color.LightGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFF1B1736),
                                        unfocusedContainerColor = Color(0xFF1B1736),
                                        focusedBorderColor = PrimaryPurple,
                                        unfocusedBorderColor = Color(0xFF2E2954),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Offer / Discount", fontSize = 10.sp, color = Color.LightGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = offer,
                                    onValueChange = { offer = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFF1B1736),
                                        unfocusedContainerColor = Color(0xFF1B1736),
                                        focusedBorderColor = PrimaryPurple,
                                        unfocusedBorderColor = Color(0xFF2E2954),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            1 -> { // TAB 2: AD CONFIGURATION & AI FEATURES
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("VIDEO QUALITY SETTINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Text("Higher resolution adds rendering credits", fontSize = 10.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(10.dp))

                        val qualityOptions = listOf(
                            "480p" to "Normal (+0)",
                            "720p HD" to "+1 Credit",
                            "1080p Full HD" to "+2 Credits",
                            "1440p 2K" to "+4 Credits",
                            "2160p 4K" to "+6 Credits"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            qualityOptions.chunked(3).forEach { rowQualities ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowQualities.forEach { (qual, extraCost) ->
                                        val isSel = selectedQuality == qual
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                                .clickable { selectedQuality = qual }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(qual, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                                Text(extraCost, fontSize = 9.sp, color = if (isSel) AccentGold else Color.Gray)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val totalAdCredits = viewModel.calculateVideoCredits(selectedDuration, selectedQuality)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TARGET AD DURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                            Text("Est. Cost: $totalAdCredits Credits", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AccentGold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(10, 15, 30, 60).forEach { dur ->
                                val isSel = selectedDuration == dur
                                val durCost = viewModel.calculateVideoCredits(dur, selectedQuality)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .clickable { selectedDuration = dur }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${dur}s Ad", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        Text("$durCost Crd", color = if (isSel) AccentCyan else Color.Gray, fontSize = 9.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("OUTPUT FORMAT & ASPECT RATIO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        val formatsList = listOf(
                            "Instagram Reel (9:16)",
                            "YouTube Shorts (9:16)",
                            "Facebook Ad (1:1)",
                            "WhatsApp Status (9:16)",
                            "Product Promo Video (16:9)"
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            formatsList.forEach { fmt ->
                                val isSel = selectedFormat == fmt
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                        .clickable { selectedFormat = fmt }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Movie, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(fmt, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                        if (isSel) Icon(Icons.Default.Check, contentDescription = null, tint = AccentGold, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("AI AD COPY & VOICEOVER LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Telugu", "English", "Hindi").forEach { lang ->
                                val isSel = selectedLanguage == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                        .clickable { selectedLanguage = lang }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("$lang Copy", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("BACKGROUND MUSIC GENRE / VIBE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Upbeat Commercial", "Energetic Tech", "Luxury Acoustic", "Chill Ambient").forEach { vibe ->
                                val isSel = selectedBgMusic == vibe
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .clickable { selectedBgMusic = vibe }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(vibe.take(12) + "...", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            2 -> { // TAB 3: PRODUCT TEMPLATES & BRAND ASSETS
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Brand Assets Box
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Store, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SAVED BRAND ASSETS & LOGO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Text("Brand Name", fontSize = 10.sp, color = Color.LightGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = brandName,
                                onValueChange = { brandName = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF1B1736),
                                    unfocusedContainerColor = Color(0xFF1B1736),
                                    focusedBorderColor = PrimaryPurple,
                                    unfocusedBorderColor = Color(0xFF2E2954),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Brand Logo Watermark URL", fontSize = 10.sp, color = Color.LightGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = brandLogoUrl,
                                onValueChange = { brandLogoUrl = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF1B1736),
                                    unfocusedContainerColor = Color(0xFF1B1736),
                                    focusedBorderColor = PrimaryPurple,
                                    unfocusedBorderColor = Color(0xFF2E2954),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                        }
                    }

                    // Product Templates Presets
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("1-CLICK PRODUCT AD PRESET TEMPLATES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                            Text("Select a category to pre-fill high converting ad structures", fontSize = 10.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(12.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                productAdTemplatesList.forEach { tmpl ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF1E1A36))
                                            .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(12.dp))
                                            .clickable {
                                                category = tmpl.categoryName
                                                productName = tmpl.exampleTitle
                                                productUrl = tmpl.exampleUrl
                                                features = tmpl.exampleFeatures
                                                benefits = tmpl.exampleBenefits
                                                price = tmpl.examplePrice
                                                offer = tmpl.exampleOffer
                                                activeTab = 0
                                            }
                                            .padding(12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(PrimaryPurple.copy(alpha = 0.3f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(tmpl.categoryName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                                Text("e.g. ${tmpl.exampleTitle} • ${tmpl.exampleOffer}", fontSize = 10.sp, color = Color.Gray)
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(PrimaryPurple)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text("LOAD", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> { // TAB 4: AD HISTORY
                if (savedProductAds.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No Product Ads Saved Yet", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Generate your first ad to see history here", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        savedProductAds.forEach { ad ->
                            ProductAdHistoryItemCard(ad = ad, onDelete = { viewModel.deleteProductAd(ad.id) })
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // GENERATE BUTTON
        val cost = viewModel.calculateVideoCredits(selectedDuration, selectedQuality)
        Button(
            onClick = {
                viewModel.generateFullProductAd(
                    productName = productName,
                    templateCategory = category,
                    productUrl = productUrl,
                    imageUrl = imageUrl,
                    features = features,
                    benefits = benefits,
                    price = price,
                    offer = offer,
                    durationSeconds = selectedDuration,
                    outputFormat = selectedFormat,
                    adLanguage = selectedLanguage,
                    bgMusicVibe = selectedBgMusic,
                    brandName = brandName,
                    quality = selectedQuality
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_product_ad_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            enabled = productName.isNotBlank()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Generate", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GENERATE PRODUCT AD PACKAGE ($cost CREDITS | $selectedQuality)",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // RESULT / LOADING STATE
        when (val currentState = state) {
            is GenerationState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(color = AccentCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(currentState.progressMessage, color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            is GenerationState.Success -> {
                Column {
                    VideoPlayerCard(item = currentState.item)
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("AUTO-CREATED AD COMPONENTS", color = AccentGold, fontWeight = FontWeight.Black, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(currentState.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { /* Export MP4 Video */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("EXPORT FULL MP4 AD VIDEO", fontWeight = FontWeight.Black, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            is GenerationState.Error -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(currentState.message, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(16.dp))
                }
            }
            else -> {}
        }
    }
}

@Composable
fun ProductAdHistoryItemCard(ad: ProductAd, onDelete: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryPurple.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(ad.productName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        Text("${ad.adLanguage} Ad • ${ad.adDurationSeconds}s • ${ad.outputFormat}", color = AccentGold, fontSize = 10.sp)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF2E2954))
                            .clickable { isExpanded = !isExpanded }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(if (isExpanded) "Hide" else "View Ad", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.2f))
                            .clickable { onDelete() }
                            .padding(6.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(14.dp))
                    }
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1B1736))
                        .padding(12.dp)
                ) {
                    Text("⚡ MARKETING HOOK:", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Text(ad.marketingHook, color = Color.White, fontSize = 11.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🗣️ VOICEOVER SCRIPT (${ad.adLanguage}):", color = AccentCyan, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Text(ad.voiceoverText, color = Color.White, fontSize = 11.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🎯 CALL TO ACTION:", color = AccentPink, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Text(ad.ctaText, color = Color.White, fontSize = 11.sp)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📝 SUBTITLES (SRT):", color = Color.LightGray, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Text(ad.subtitlesSrt, color = Color.Gray, fontSize = 10.sp)
                }
            }
        }
    }
}
