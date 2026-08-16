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
fun ThumbnailGeneratorScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf("YouTube (16:9)") } // YouTube (16:9), Instagram Square (1:1), Instagram Reel (9:16)
    var selectedStyle by remember { mutableStateOf("Clickbait Bold Text") }
    var selectedQuality by remember { mutableStateOf("Standard") }

    val platformList = listOf(
        "YouTube (16:9)",
        "Instagram Square (1:1)",
        "Instagram Reel (9:16)"
    )

    val stylePresets = listOf(
        "Clickbait Bold Text",
        "High-Contrast Face",
        "3D Avatar / Gaming",
        "Minimalist Tech",
        "Dark Neon Aesthetic",
        "Cinematic Vlog"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()
    val cost = viewModel.calculateImageCredits(selectedQuality)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("thumbnail_generator_screen")
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
                Text("AI THUMBNAIL GENERATOR", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                Text("YouTube & Instagram Thumbnails + AI Title Suggestions", fontSize = 12.sp, color = AccentGold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Platform Selector
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. SELECT PLATFORM & FORMAT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    platformList.forEach { platform ->
                        val isSel = selectedPlatform == platform
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { selectedPlatform = platform }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = platform,
                                color = if (isSel) Color.White else Color.LightGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Topic & Prompt Input
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("2. VIDEO TOPIC / SUBJECT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    TextButton(onClick = {
                        topic = "How I built a $10,000/month AI automation business in 30 days"
                    }) {
                        Text("Sample Topic", fontSize = 10.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("What is your video about?", color = Color.Gray, fontSize = 12.sp) },
                    placeholder = { Text("e.g. Unboxing iPhone 16 Pro Max in Telugu", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, tint = AccentGold) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AccentGold,
                        unfocusedBorderColor = Color(0xFF2E2954)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Style Selector Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. THUMBNAIL DESIGN STYLE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    stylePresets.chunked(2).forEach { rowStyles ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowStyles.forEach { style ->
                                val isSel = selectedStyle == style
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedStyle = style }
                                        .padding(vertical = 10.dp, horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = style,
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
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quality Settings Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("4. THUMBNAIL RESOLUTION QUALITY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    Text("Est. Cost: $cost Credits", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AccentGold)
                }
                Spacer(modifier = Modifier.height(10.dp))

                val qualityOptions = listOf(
                    "Standard" to "Normal (+0)",
                    "HD" to "+1 Credit",
                    "Ultra HD" to "+2 Credits",
                    "2K" to "+4 Credits",
                    "4K" to "+6 Credits"
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
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Button
        Button(
            onClick = {
                if (topic.isNotBlank()) {
                    viewModel.generateThumbnail(
                        topic = topic,
                        platform = selectedPlatform,
                        style = selectedStyle,
                        quality = selectedQuality
                    )
                }
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
                        Brush.horizontalGradient(listOf(PrimaryPurple, AccentGold))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE THUMBNAIL & TITLES ($selectedQuality)" else "GENERATE THUMBNAIL & TITLES ($cost CREDITS)",
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
                            color = AccentGold,
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
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("THUMBNAIL DESIGN & AI TITLES GENERATED!", fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Save to gallery / database */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Thumbnail & Copy AI Titles", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
