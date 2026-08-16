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
fun VideoDubbingScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var videoUrl by remember { mutableStateOf("") }
    var detectedLanguage by remember { mutableStateOf("Auto Detect (English/Hindi)") }
    var selectedTargetLang by remember { mutableStateOf("Telugu") } // Telugu, English, Hindi, Tamil, Kannada, Malayalam
    var selectedVoiceGender by remember { mutableStateOf("Female Voice") } // Female Voice, Male Voice, Natural Studio
    var generateSubtitles by remember { mutableStateOf(true) }
    var selectedQuality by remember { mutableStateOf("1080p Full HD") }

    val supportedLanguages = listOf(
        "Telugu",
        "English",
        "Hindi",
        "Tamil",
        "Kannada",
        "Malayalam"
    )

    val sampleVideos = listOf(
        "https://aivideocreator.hub/samples/tech_review.mp4" to "Tech Review",
        "https://aivideocreator.hub/samples/movie_trailer.mp4" to "Movie Clip",
        "https://aivideocreator.hub/samples/educational_lecture.mp4" to "Lecture Video",
        "https://aivideocreator.hub/samples/vlog_interview.mp4" to "Vlog Reel"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()
    val cost = viewModel.calculateVideoCredits(15, selectedQuality) + 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("video_dubbing_screen")
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
                Text("AI VIDEO DUBBING", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                Text("Auto Language Detect, AI Voice Translation & Lip Sync", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Video Input / Upload Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. UPLOAD OR PASTE VIDEO LINK", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = videoUrl,
                    onValueChange = { videoUrl = it },
                    label = { Text("Video URL or MP4 Link", color = Color.Gray, fontSize = 12.sp) },
                    placeholder = { Text("https://example.com/source_video.mp4", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = AccentCyan) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = Color(0xFF2E2954)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Or try a sample video link:", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sampleVideos.forEach { (url, label) ->
                        val isSelected = videoUrl == url
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSelected) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                .clickable { videoUrl = url }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, fontSize = 10.sp, color = if (isSelected) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Detected Source Language & Target Language Selector
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
                    Text("2. LANGUAGE DETECTION & TARGET DUBBING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    Text("Auto Detect Active", fontSize = 10.sp, color = AccentCyan)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E1A36))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Original Audio Language: $detectedLanguage", fontSize = 11.sp, color = Color.LightGray)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Target Dubbing Language:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    supportedLanguages.chunked(3).forEach { rowLangs ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowLangs.forEach { lang ->
                                val isSel = selectedTargetLang == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedTargetLang = lang }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = lang,
                                        color = if (isSel) Color.White else Color.LightGray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Voice Type & Subtitles Toggle
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. VOICE & SUBTITLE SETTINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Female Voice", "Male Voice", "Natural Studio").forEach { voice ->
                        val isSel = selectedVoiceGender == voice
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { selectedVoiceGender = voice }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(voice, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Auto-Generate Subtitles (SRT)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Adds $selectedTargetLang captions with lip-sync timing", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = generateSubtitles,
                        onCheckedChange = { generateSubtitles = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AccentCyan,
                            checkedTrackColor = PrimaryPurple
                        )
                    )
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
                    Text("4. EXPORT QUALITY SETTINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    Text("Est. Cost: $cost Credits", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AccentGold)
                }
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
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Dubbing Button
        Button(
            onClick = {
                viewModel.generateVideoDubbing(
                    videoUrl = videoUrl.ifBlank { "https://aivideocreator.hub/samples/tech_review.mp4" },
                    sourceLang = detectedLanguage,
                    targetLang = selectedTargetLang,
                    voiceGender = selectedVoiceGender,
                    includeSubtitles = generateSubtitles,
                    quality = selectedQuality
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
                        Brush.horizontalGradient(listOf(PrimaryPurple, AccentCyan))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "DUB VIDEO TO $selectedTargetLang ($selectedQuality)" else "DUB VIDEO TO $selectedTargetLang ($cost CREDITS)",
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
                            Text("DUBBED VIDEO GENERATED WITH LIP-SYNC!", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Export & Save Dubbed Video */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Export / Download Dubbed MP4 Video", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
