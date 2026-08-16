package com.example.ui.screens.generators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

data class VideoTemplateItem(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val duration: String,
    val recommendedRatio: String,
    val badge: String = "POPULAR"
)

@Composable
fun VideoTemplatesScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val categories = listOf(
        "Product Ads",
        "Motivation",
        "Podcast",
        "Story",
        "Luxury",
        "Travel",
        "Kids",
        "Education",
        "News"
    )

    var selectedCategory by remember { mutableStateOf("Product Ads") }

    val templatesList = remember {
        listOf(
            // Product Ads
            VideoTemplateItem("p1", "E-Commerce Flash Sale Reel", "Product Ads", "High energy product showcase with 3D text overlays & discount hooks", "15s", "9:16", "HOT"),
            VideoTemplateItem("p2", "Minimalist Tech Unboxing", "Product Ads", "Sleek dark studio lighting for gadgets and premium items", "30s", "16:9", "TRENDING"),
            VideoTemplateItem("p3", "Amazon Product UGC Review", "Product Ads", "Authentic customer testimonial style with split screen rating", "15s", "9:16"),

            // Motivation
            VideoTemplateItem("m1", "Cinematic Gym Motivation", "Motivation", "Dramatic dark aesthetic, heavy bass music & inspiring voiceover", "15s", "9:16", "POPULAR"),
            VideoTemplateItem("m2", "Morning Mindset & Gratitude", "Motivation", "Golden hour sunrise visuals with peaceful piano soundtrack", "30s", "9:16"),
            VideoTemplateItem("m3", "CEO Success Mindset", "Motivation", "High-contrast urban imagery with bold typography quotes", "15s", "9:16"),

            // Podcast
            VideoTemplateItem("c1", "Viral Podcast Audiogram", "Podcast", "Animated wave visualizer with real-time subtitles & highlight clip", "30s", "9:16", "VIRAL"),
            VideoTemplateItem("c2", "Studio Conversation Highlight", "Podcast", "Dual camera layout with speaker identification cards", "30s", "16:9"),
            VideoTemplateItem("c3", "Solo Host Hot Take", "Podcast", "Centered mic setup with dynamic caption popups", "15s", "9:16"),

            // Story
            VideoTemplateItem("s1", "Cinematic Mystery Short", "Story", "Atmospheric fog, tension build-up & narrative voiceover", "30s", "16:9", "POPULAR"),
            VideoTemplateItem("s2", "Mythological Folk Legend", "Story", "Vibrant CGI art style with traditional background score", "60s", "9:16", "FEATURED"),
            VideoTemplateItem("s3", "Sci-Fi Cyberpunk Adventure", "Story", "Neon cityscapes and futuristic character dialogue", "30s", "9:16"),

            // Luxury
            VideoTemplateItem("l1", "Supercar & Yacht Lifestyle", "Luxury", "Gold tint color grade, sleek transitions & expensive beats", "15s", "9:16", "LUXURY"),
            VideoTemplateItem("l2", "High-End Real Estate Tour", "Luxury", "Smooth camera walkthroughs of modern mansions & penthouses", "30s", "16:9"),
            VideoTemplateItem("l3", "Luxury Watch & Jewelry", "Luxury", "Extreme macro closeups with elegant sparkle effects", "15s", "1:1"),

            // Travel
            VideoTemplateItem("t1", "Tropical Beach Paradise", "Travel", "Drone beach sweeps, turquoise ocean water & tropical house music", "15s", "9:16", "TRENDING"),
            VideoTemplateItem("t2", "Mountain Hiking Vlog", "Travel", "Cinematic nature landscapes with fast jump cuts", "30s", "9:16"),
            VideoTemplateItem("t3", "European City Walking Tour", "Travel", "Cobblestone streets, local cuisine & cozy acoustic tunes", "15s", "9:16"),

            // Kids
            VideoTemplateItem("k1", "Colorful 3D Cartoon Song", "Kids", "Playful animated characters, bright rainbow palette & nursery rhyme", "30s", "16:9", "KIDS"),
            VideoTemplateItem("k2", "Fun Animal Learning Quiz", "Kids", "Cute talking animals asking interactive trivia questions", "30s", "9:16"),
            VideoTemplateItem("k3", "Bedtime Magical Tale", "Kids", "Soft watercolor art with gentle bedtime storytelling", "60s", "16:9"),

            // Education
            VideoTemplateItem("e1", "Did You Know? Quick Facts", "Education", "Fast-paced infographic animations with pop sound effects", "15s", "9:16", "VIRAL"),
            VideoTemplateItem("e2", "Science Experiment Explained", "Education", "Step-by-step 3D diagram breakdown with clear voiceover", "30s", "16:9"),
            VideoTemplateItem("e3", "Historical Event Timeline", "Education", "Vintage map zooms and archival photo motion", "30s", "9:16"),

            // News
            VideoTemplateItem("n1", "Breaking News Ticker Reel", "News", "Red alert banner, live newsroom background & headline ticker", "15s", "9:16", "NEWS"),
            VideoTemplateItem("n2", "Tech Industry Daily Brief", "News", "Modern digital studio set with chart overlays", "30s", "16:9"),
            VideoTemplateItem("n3", "Market & Crypto Update", "News", "Real-time stock ticker styling with energetic news intro", "15s", "9:16")
        )
    }

    val filteredTemplates = templatesList.filter { it.category == selectedCategory }
    var selectedTemplate by remember(selectedCategory) { mutableStateOf(filteredTemplates.firstOrNull()) }
    var userCustomText by remember { mutableStateOf("") }
    var selectedRatio by remember { mutableStateOf("9:16") }
    var selectedQuality by remember { mutableStateOf("1080p Full HD") }

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()
    val cost = viewModel.calculateVideoCredits(10, selectedQuality)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("video_templates_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DarkSurface)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("AI VIDEO TEMPLATES", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                Text("Product Ads, Motivation, Podcast, Story, Luxury & More", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Scroll Row
        Text("CATEGORY SELECTOR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSel = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(20.dp))
                        .clickable {
                            selectedCategory = cat
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cat,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Templates Cards Carousel
        Text("SELECT A TEMPLATE ($selectedCategory)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            filteredTemplates.forEach { tmpl ->
                val isChosen = selectedTemplate?.id == tmpl.id
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isChosen) Color(0xFF2E1A52) else DarkSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, if (isChosen) AccentCyan else Color.Transparent, RoundedCornerShape(14.dp))
                        .clickable {
                            selectedTemplate = tmpl
                            selectedRatio = tmpl.recommendedRatio
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isChosen) AccentCyan else PrimaryPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(tmpl.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(AccentGold)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(tmpl.badge, fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(tmpl.description, fontSize = 11.sp, color = Color.Gray, maxLines = 2)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(horizontalAlignment = Alignment.End) {
                            Text(tmpl.duration, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                            Text(tmpl.recommendedRatio, fontSize = 10.sp, color = Color.LightGray)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Input for Chosen Template
        selectedTemplate?.let { tmpl ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CUSTOMIZE '${tmpl.title.uppercase()}'", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = userCustomText,
                        onValueChange = { userCustomText = it },
                        label = { Text("Your Brand Name, Product Link or Headline Text", color = Color.Gray, fontSize = 12.sp) },
                        placeholder = { Text("e.g. Acme Tech Unboxing, 50% Off Code: SAVE50", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = AccentGold) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = AccentGold,
                            unfocusedBorderColor = Color(0xFF2E2954)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Aspect Ratio:", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("9:16 (Reels/Shorts)", "16:9 (YouTube)", "1:1 (Feed)").forEach { ratio ->
                            val rCode = ratio.split(" ").first()
                            val isSel = selectedRatio == rCode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                    .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                    .clickable { selectedRatio = rCode }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(ratio, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Button
        Button(
            onClick = {
                selectedTemplate?.let { tmpl ->
                    viewModel.generateFromVideoTemplate(
                        categoryName = selectedCategory,
                        templateTitle = tmpl.title,
                        userCustomText = userCustomText.ifBlank { "Featured ${selectedCategory} Showcase" },
                        aspectRatio = selectedRatio,
                        quality = selectedQuality
                    )
                }
            },
            enabled = selectedTemplate != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(listOf(PrimaryPurple, AccentCyan))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "RENDER TEMPLATE VIDEO" else "RENDER TEMPLATE ($cost CREDITS)",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // State Output
        when (val s = state) {
            is GenerationState.Loading -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = AccentCyan)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(s.progressMessage, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { s.progressPercent },
                            modifier = Modifier.fillMaxWidth(),
                            color = AccentCyan,
                            trackColor = Color(0xFF1E1A36)
                        )
                    }
                }
            }
            is GenerationState.Error -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3B1020)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Color.Red)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(s.message, color = Color.White, fontSize = 12.sp)
                    }
                }
            }
            is GenerationState.Success -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TEMPLATE VIDEO RENDERED SUCCESSFULLY!", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Download Template Video */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Export MP4 Template Video", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
