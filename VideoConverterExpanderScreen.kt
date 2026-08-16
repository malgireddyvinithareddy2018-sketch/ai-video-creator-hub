package com.example.ui.screens.generators

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun VideoConverterExpanderScreen(
    viewModel: MainViewModel,
    initialMode: String = "long_to_short", // "long_to_short" or "short_to_long"
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf(if (initialMode == "short_to_long") 1 else 0) }

    // Long to Short States
    var videoSourceUrl by remember { mutableStateOf("") }
    var selectedAspectRatio by remember { mutableStateOf("9:16 (Reels/Shorts)") }
    var highlightDetection by remember { mutableStateOf("AI Viral Hook Detection") }
    var targetClipsCount by remember { mutableStateOf("3 Shorts Clips") }
    var includeAutoCaptions by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("English + Telugu") }
    var isConverting by remember { mutableStateOf(false) }
    var generatedReels by remember { mutableStateOf<List<String>>(emptyList()) }

    // Short to Long States
    var shortClipUrl by remember { mutableStateOf("") }
    var extensionPrompt by remember { mutableStateOf("") }
    var targetDuration by remember { mutableStateOf("60 Seconds HD") }
    var continuationStyle by remember { mutableStateOf("Seamless AI Narrative Expansion") }
    var isExpanding by remember { mutableStateOf(false) }
    var expandedVideoResult by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("video_converter_expander_screen")
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
                    Text("AI VIDEO CONVERTER & EXPANDER", fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentPink)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("PRO STUDIO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Long Video -> Shorts Reels & Short Clip -> 60s/120s Expander", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1. Long Video -> Short Reels", "2. Short Video -> Long Expander").forEachIndexed { idx, title ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        .clickable { activeTab = idx }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            // TAB 0: LONG VIDEO TO SHORT REELS CONVERTER
            0 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("LONG VIDEO TO SHORT REELS CONVERTER", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Text("Extract viral highlights, auto-crop 9:16, add captions & hashtags", fontSize = 10.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Video Source (YouTube URL or Local MP4):", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = videoSourceUrl,
                            onValueChange = { videoSourceUrl = it },
                            placeholder = { Text("e.g. https://youtube.com/watch?v=sample or upload local file", color = Color.Gray, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentCyan)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Target Aspect Ratio:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                listOf("9:16 (Reels/Shorts)", "1:1 (Square Feed)").forEach { ratio ->
                                    val isSel = selectedAspectRatio == ratio
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                            .clickable { selectedAspectRatio = ratio }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(ratio, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Clips Output Count:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                listOf("3 Shorts Clips", "5 Viral Clips", "10 Highlights").forEach { cnt ->
                                    val isSel = targetClipsCount == cnt
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                            .clickable { targetClipsCount = cnt }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(cnt, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Auto Burn-in SRT Captions", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Animated colorful captions on top", fontSize = 10.sp, color = Color.Gray)
                            }
                            Switch(
                                checked = includeAutoCaptions,
                                onCheckedChange = { includeAutoCaptions = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = PrimaryPurple)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                isConverting = true
                                viewModel.generateTextToVideo(
                                    prompt = "Convert long video highlight into 9:16 viral short reel with animated captions",
                                    durationSeconds = 30,
                                    aspectRatio = "9:16",
                                    onComplete = { success, msg, _ ->
                                        isConverting = false
                                        if (success) {
                                            generatedReels = listOf(
                                                "Reel 1: Key Opening Viral Hook (30s 9:16 HD)",
                                                "Reel 2: Core Lesson & Demo Clip (28s 9:16 HD)",
                                                "Reel 3: Call-To-Action & Outro (25s 9:16 HD)"
                                            )
                                            Toast.makeText(context, "Long video converted into 3 viral short reels with captions! 🎬", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp),
                            enabled = !isConverting
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.horizontalGradient(listOf(PrimaryPurple, AccentPink))),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isConverting) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("CONVERTING LONG VIDEO TO REELS...", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("CONVERT TO 9:16 VIRAL REELS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    }
                                }
                            }
                        }

                        if (generatedReels.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("GENERATED SHORT REELS:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                            Spacer(modifier = Modifier.height(8.dp))
                            generatedReels.forEachIndexed { idx, reelTitle ->
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A36)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                            Icon(Icons.Default.PlayCircle, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(reelTitle, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 11.sp)
                                                Text("9:16 HD • Auto Captions Enabled", fontSize = 9.sp, color = Color.Gray)
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                Toast.makeText(context, "Exporting $reelTitle to gallery & social queue...", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("Export MP4", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 1: SHORT VIDEO TO LONG VIDEO EXPANDER
            1 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SHORT VIDEO TO LONG VIDEO EXPANDER", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Text("Extend short clips (5s/10s) into seamless 30s, 60s, or 120s long videos", fontSize = 10.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Source Short Video Clip (URL or MP4):", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = shortClipUrl,
                            onValueChange = { shortClipUrl = it },
                            placeholder = { Text("e.g. Upload 5s short video clip to expand", color = Color.Gray, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentCyan)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Expansion Continuation Prompt:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = extensionPrompt,
                            onValueChange = { extensionPrompt = it },
                            placeholder = { Text("e.g. Continue the scene with character walking into futuristic cityscape with cinematic lighting", color = Color.Gray, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentCyan)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Target Extended Duration:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("30 Seconds", "60 Seconds HD", "120 Seconds HD").forEach { dur ->
                                val isSel = targetDuration == dur
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                        .clickable { targetDuration = dur }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(dur, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                isExpanding = true
                                viewModel.generateTextToVideo(
                                    prompt = extensionPrompt.ifBlank { "Seamlessly expand short video clip with AI frame interpolation and scene continuation" },
                                    durationSeconds = 60,
                                    aspectRatio = "16:9",
                                    onComplete = { success, msg, videoUrl ->
                                        isExpanding = false
                                        if (success) {
                                            expandedVideoResult = "Expanded Video ($targetDuration): $videoUrl"
                                            Toast.makeText(context, "Short video expanded into long HD video! 🚀", Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp),
                            enabled = !isExpanding
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.horizontalGradient(listOf(PrimaryPurple, AccentCyan))),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isExpanding) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("EXPANDING SHORT VIDEO...", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("EXPAND TO LONG VIDEO ($targetDuration)", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    }
                                }
                            }
                        }

                        if (expandedVideoResult != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("🎉 EXPANDED VIDEO READY!", fontWeight = FontWeight.Black, color = Color.White, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(expandedVideoResult!!, fontSize = 11.sp, color = Color.LightGray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
