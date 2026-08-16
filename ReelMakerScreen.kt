package com.example.ui.screens.generators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

@Composable
fun ReelMakerScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf("Instagram Reels") }
    var selectedPacing by remember { mutableStateOf("Fast / Viral Hook") }
    var selectedLanguage by remember { mutableStateOf("Telugu") }

    var includeHooks by remember { mutableStateOf(true) }
    var includeCaptions by remember { mutableStateOf(true) }
    var includeTrendingMusic by remember { mutableStateOf(true) }
    var includeHashtags by remember { mutableStateOf(true) }

    val platforms = listOf(
        "Instagram Reels",
        "YouTube Shorts",
        "TikTok Videos"
    )

    val pacings = listOf(
        "Fast / Viral Hook",
        "Storytelling & Secret",
        "High-Energy Hype",
        "Educational Explainer"
    )

    val languages = listOf(
        "Telugu",
        "English",
        "Hindi",
        "Tamil",
        "Kannada",
        "Malayalam"
    )

    val sampleTopics = listOf(
        "3 Secret AI Tools nobody is telling you about in 2026" to "Secret AI Tools",
        "How to double your productivity in 15 seconds" to "Productivity Hack",
        "Top 5 places to visit in India before you turn 30" to "Travel Top 5"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()
    val cost = viewModel.calculateVideoCredits(15, "1080p Full HD")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("reel_maker_screen")
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("AI REEL MAKER", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentPink)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("9:16 VERTICAL", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
                Text("Instagram Reels, Shorts & TikTok with Auto Hooks & Hashtags", fontSize = 11.sp, color = AccentPink)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Target Platform Selection Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. TARGET PLATFORM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    platforms.forEach { plat ->
                        val isSel = selectedPlatform == plat
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentPink else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                                .clickable { selectedPlatform = plat }
                                .padding(vertical = 12.dp, horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = plat,
                                color = if (isSel) Color.White else Color.LightGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Topic & Quick Prompts Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. VIRAL TOPIC OR CONCEPT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("What is your Reel/Short video about?", color = Color.Gray, fontSize = 12.sp) },
                    placeholder = { Text("e.g. 3 secret AI tools for college students...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Movie, contentDescription = null, tint = AccentPink) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AccentPink,
                        unfocusedBorderColor = Color(0xFF2E2954)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Sample Viral Ideas:", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    sampleTopics.forEach { (sampleTopicText, tag) ->
                        val isSel = topic == sampleTopicText
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentPink else Color(0xFF2E2954), RoundedCornerShape(8.dp))
                                .clickable { topic = sampleTopicText }
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = "🔥 $tag: \"$sampleTopicText\"",
                                fontSize = 10.sp,
                                color = if (isSel) Color.White else Color.LightGray,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pacing & Language Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. PACING & AUDIO LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Video Pacing Style:", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    pacings.chunked(2).forEach { rowPacings ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowPacings.forEach { p ->
                                val isSel = selectedPacing == p
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedPacing = p }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(p, fontSize = 10.sp, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Script & Audio Language:", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    languages.chunked(3).forEach { rowLangs ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowLangs.forEach { lang ->
                                val isSel = selectedLanguage == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedLanguage = lang }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(lang, fontSize = 11.sp, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Viral Boosters Toggles Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4. VIRAL REEL BOOSTERS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto Viral Hooks (First 3 Sec)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Generates 3 scroll-stopping intro lines", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeHooks,
                        onCheckedChange = { includeHooks = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentPink, checkedTrackColor = PrimaryPurple)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto Captions / Subtitles", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Word-by-word $selectedLanguage sync subtitles", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeCaptions,
                        onCheckedChange = { includeCaptions = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentCyan, checkedTrackColor = PrimaryPurple)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Trending Audio & Music Suggestions", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Matches viral sound tracks on $selectedPlatform", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeTrendingMusic,
                        onCheckedChange = { includeTrendingMusic = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = PrimaryPurple)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Auto Viral Hashtags", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Algorithm-optimized high reach tags", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeHashtags,
                        onCheckedChange = { includeHashtags = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentPink, checkedTrackColor = PrimaryPurple)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Reel Button
        Button(
            onClick = {
                viewModel.generateReel(
                    topic = topic.ifBlank { "Top 3 productivity hacks to save 2 hours every day" },
                    platform = selectedPlatform,
                    pacing = selectedPacing,
                    language = selectedLanguage,
                    includeAutoHook = includeHooks,
                    includeCaptions = includeCaptions,
                    includeTrendingMusic = includeTrendingMusic,
                    includeHashtags = includeHashtags
                )
            },
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
                        Brush.horizontalGradient(listOf(PrimaryPurple, AccentPink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "CREATE VIRAL REEL (9:16)" else "CREATE VIRAL REEL ($cost CREDITS)",
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
                        CircularProgressIndicator(color = AccentPink)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(s.progressMessage, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { s.progressPercent },
                            modifier = Modifier.fillMaxWidth(),
                            color = AccentPink,
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
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentPink)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("VIRAL REEL PACKAGE GENERATED (9:16)", fontWeight = FontWeight.Bold, color = AccentPink, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Export Reel Package */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Export 9:16 Video & Copy Hashtags", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
