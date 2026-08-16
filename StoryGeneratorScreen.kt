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
fun StoryGeneratorScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var storyPremise by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Mythological / Fantasy") }
    var selectedLanguage by remember { mutableStateOf("Telugu") }
    var numScenes by remember { mutableStateOf(4) }

    var includeVideoPrompts by remember { mutableStateOf(true) }
    var includeVoiceover by remember { mutableStateOf(true) }
    var includeSubtitles by remember { mutableStateOf(true) }

    val genres = listOf(
        "Mythological / Fantasy",
        "Sci-Fi / Cyberpunk",
        "Thriller / Mystery",
        "Emotional Drama",
        "Comedy & Fun",
        "Action Adventure"
    )

    val supportedLanguages = listOf(
        "Telugu",
        "English",
        "Hindi",
        "Tamil",
        "Kannada",
        "Malayalam"
    )

    val samplePremises = listOf(
        "A young archaeologist discovers an ancient Vijayanagara temple holding a time portal." to "Archaeologist Temple",
        "In 2090, a cyber investigator in Hyderabad tracks an AI clone that gained consciousness." to "2090 AI Clone",
        "Two estranged brothers unite to protect their village's ancient sacred forest from corporate destruction." to "Sacred Forest"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()
    val cost = if (includeVideoPrompts || includeVoiceover) 2 else 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("story_generator_screen")
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
                Text("AI STORY GENERATOR", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                Text("Ideas, Scene Script, Characters, Video Prompts & Voiceover", fontSize = 12.sp, color = AccentGold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Premise / Story Idea Input Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. STORY IDEA / PREMISE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = storyPremise,
                    onValueChange = { storyPremise = it },
                    label = { Text("Enter story theme, title or core concept", color = Color.Gray, fontSize = 12.sp) },
                    placeholder = { Text("e.g. A mystery about a detective who can see lost memories in shadows", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AccentGold) },
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
                Text("Or choose a sample story prompt:", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    samplePremises.forEach { (premiseText, tag) ->
                        val isSel = storyPremise == premiseText
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(8.dp))
                                .clickable { storyPremise = premiseText }
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = "✨ $tag: \"$premiseText\"",
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

        // Genre & Language Selector Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. STORY GENRE & SCRIPT LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Select Genre:", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    genres.chunked(2).forEach { rowGenres ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowGenres.forEach { genre ->
                                val isSel = selectedGenre == genre
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedGenre = genre }
                                        .padding(vertical = 10.dp, horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = genre,
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

                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Output Language:", fontSize = 11.sp, color = Color.Gray)
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
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedLanguage = lang }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(lang, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Production Assets Toggles Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. PRODUCTION ASSETS TO GENERATE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Story to Video Prompts", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Generates cinematic prompts for AI Video Generator", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeVideoPrompts,
                        onCheckedChange = { includeVideoPrompts = it },
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
                        Text("Story to Voiceover Script", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Formatted narration script in $selectedLanguage", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeVoiceover,
                        onCheckedChange = { includeVoiceover = it },
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
                        Text("Story to Subtitles (SRT)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Timed captions ready for video editor", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = includeSubtitles,
                        onCheckedChange = { includeSubtitles = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentPink, checkedTrackColor = PrimaryPurple)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Story Button
        Button(
            onClick = {
                viewModel.generateStory(
                    topicOrPremise = storyPremise.ifBlank { "An ancient mystery artifact found in an underground cave" },
                    genre = selectedGenre,
                    targetLanguage = selectedLanguage,
                    includeVideoPrompts = includeVideoPrompts,
                    includeVoiceoverScript = includeVoiceover,
                    includeSubtitlesSRT = includeSubtitles,
                    numScenes = numScenes
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
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE FULL STORY PACKAGE ($selectedLanguage)" else "GENERATE FULL STORY PACKAGE ($cost CREDITS)",
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
                            Text("COMPLETE STORY PACKAGE GENERATED!", fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Save / Copy Full Story Package */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copy Full Script, Characters & Video Prompts", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
