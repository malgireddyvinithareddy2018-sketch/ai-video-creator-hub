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
fun PodcastGeneratorScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var selectedHostType by remember { mutableStateOf("Co-Hosts (Male & Female)") }
    var selectedLanguage by remember { mutableStateOf("Telugu") }
    var selectedBgm by remember { mutableStateOf("Lo-Fi Chill Ambient") }
    var selectedExportFormat by remember { mutableStateOf("MP4 Audiogram Video") }
    var includeIntroOutro by remember { mutableStateOf(true) }

    val hostOptions = listOf(
        "Co-Hosts (Male & Female)",
        "Solo Male Host",
        "Solo Female Host"
    )

    val supportedLanguages = listOf(
        "Telugu",
        "English",
        "Hindi",
        "Tamil",
        "Kannada",
        "Malayalam"
    )

    val bgmGenres = listOf(
        "Lo-Fi Chill Ambient",
        "Acoustic Guitar",
        "Corporate Tech Beats",
        "Deep Storytelling Synth"
    )

    val exportFormats = listOf(
        "MP4 Audiogram Video",
        "MP3 Studio Audio"
    )

    val samplePodcastTopics = listOf(
        "The Rise of Artificial Intelligence in Telugu Cinema & Creative Arts" to "AI in Cinema",
        "How to Build a High-Income Digital Career in 2026: Roadmap for Beginners" to "Digital Career 2026",
        "Ancient Mysteries of Indian Temples and Hidden Architecture Wonders" to "Temple Mysteries"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()
    val cost = if (selectedExportFormat.contains("MP4")) 3 else 2

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("podcast_generator_screen")
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
                    Text("AI PODCAST GENERATOR", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("STUDIO QUALITY", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Topic to Script, Male & Female AI Voices, Multi-Lang & BGM", fontSize = 11.sp, color = AccentGold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Topic Input & Samples Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. PODCAST TOPIC OR EPISODE THEME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Enter topic, question or interview theme", color = Color.Gray, fontSize = 12.sp) },
                    placeholder = { Text("e.g. Future of EV startups and charging infrastructure in India...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = AccentGold) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AccentGold,
                        unfocusedBorderColor = Color(0xFF2E2954)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text("Popular Episode Ideas:", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    samplePodcastTopics.forEach { (sampleTopicText, tag) ->
                        val isSel = topic == sampleTopicText
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(8.dp))
                                .clickable { topic = sampleTopicText }
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = "🎙️ $tag: \"$sampleTopicText\"",
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

        // Host Voices & Language Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. AI HOST VOICE & LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Select Host Setup:", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    hostOptions.forEach { host ->
                        val isSel = selectedHostType == host
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { selectedHostType = host }
                                .padding(vertical = 10.dp, horizontal = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = if (isSel) AccentCyan else Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(host, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Podcast Language:", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    supportedLanguages.chunked(3).forEach { rowLangs ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowLangs.forEach { lang ->
                                val isSel = selectedLanguage == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
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

        // Background Music & Intro/Outro Options Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. MUSIC & EXPORT FORMAT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Background Music (BGM):", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    bgmGenres.chunked(2).forEach { rowBgms ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowBgms.forEach { bgm ->
                                val isSel = selectedBgm == bgm
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentPink else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedBgm = bgm }
                                        .padding(vertical = 8.dp, horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(bgm, fontSize = 10.sp, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Include Intro Jingle & Outro Sign-off", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Adds custom studio intro music hook & CTA outro", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeIntroOutro,
                        onCheckedChange = { includeIntroOutro = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = PrimaryPurple)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Export Format:", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    exportFormats.forEach { fmt ->
                        val isSel = selectedExportFormat == fmt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { selectedExportFormat = fmt }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(fmt, fontSize = 11.sp, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Podcast Button
        Button(
            onClick = {
                viewModel.generatePodcast(
                    topic = topic.ifBlank { "Future of AI and Technology in Everyday Life" },
                    hostType = selectedHostType,
                    language = selectedLanguage,
                    bgMusicGenre = selectedBgm,
                    includeIntroOutro = includeIntroOutro,
                    exportFormat = selectedExportFormat
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
                        Brush.horizontalGradient(listOf(PrimaryPurple, AccentGold))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE STUDIO PODCAST ($selectedLanguage)" else "GENERATE STUDIO PODCAST ($cost CREDITS)",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
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
                        CircularProgressIndicator(color = AccentGold)
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
                            Text("PODCAST PACKAGE READY ($selectedExportFormat)", fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { /* Export Audio / Video */ },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Download $selectedExportFormat", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
