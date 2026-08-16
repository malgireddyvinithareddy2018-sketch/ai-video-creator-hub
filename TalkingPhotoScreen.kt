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
fun TalkingPhotoScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var photoUrl by remember { mutableStateOf("") }
    var scriptText by remember { mutableStateOf("నమస్కారం! AI Talking Photo ద్వారా మీ ఫోటోలతో మాట్లాడే వీడియోలను సులభంగా తయారు చేయండి.") }
    var selectedLanguage by remember { mutableStateOf("Telugu") } // Telugu, English
    var selectedVoiceGender by remember { mutableStateOf("Female") } // Male, Female, Child, Narrator
    var selectedQuality by remember { mutableStateOf("1080p Full HD") }

    val samplePhotos = listOf(
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400" to "Portrait Woman",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400" to "Portrait Man",
        "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400" to "Studio Model",
        "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400" to "Professional"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()
    val cost = viewModel.calculateVideoCredits(15, selectedQuality)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("talking_photo_screen")
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
                Text("AI TALKING PHOTO", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                Text("Turn any photo into lip-synced talking video", fontSize = 12.sp, color = AccentPink)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Photo Upload / Select Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. UPLOAD OR SELECT PHOTO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it },
                    label = { Text("Photo Image URL or Upload Link", color = Color.Gray, fontSize = 12.sp) },
                    placeholder = { Text("Paste image URL (JPG / PNG)...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, tint = AccentCyan) },
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
                Text("Or pick a sample portrait face:", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    samplePhotos.forEach { (url, name) ->
                        val isSelected = photoUrl == url
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSelected) AccentGold else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                .clickable { photoUrl = url }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(name, fontSize = 10.sp, color = if (isSelected) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Script Section
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
                    Text("2. DIALOGUE / SCRIPT (LIP SYNC)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    TextButton(onClick = {
                        scriptText = if (selectedLanguage == "Telugu") {
                            "ఈ AI టెక్నాలజీతో మీ ప్రొడక్ట్స్, యూట్యూబ్ రీల్స్ మరియు ఇన్స్టాగ్రామ్ వీడియోలు నిమిషాల్లో క్రియేట్ చేయండి!"
                        } else {
                            "Welcome! Transform your static photos into realistic talking avatars with full lip synchronization."
                        }
                    }) {
                        Text("Auto Sample", fontSize = 10.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = scriptText,
                    onValueChange = { scriptText = it },
                    label = { Text("What should the photo speak?", color = Color.Gray, fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
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

        // Language & Voice Gender Selector
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. VOICE LANGUAGE & TYPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Telugu", "English", "Hindi", "Tamil").forEach { lang ->
                        val isSel = selectedLanguage == lang
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { selectedLanguage = lang }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(lang, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Female", "Male", "Child", "Emotional").forEach { g ->
                        val isSel = selectedVoiceGender == g
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { selectedVoiceGender = g }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(g, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
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
                    Text("4. VIDEO QUALITY SETTINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
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

        // Generate Button
        Button(
            onClick = {
                if (scriptText.isNotBlank()) {
                    viewModel.generateTalkingPhoto(
                        photoUrl = photoUrl.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400" },
                        scriptText = scriptText,
                        language = selectedLanguage,
                        voiceGender = selectedVoiceGender,
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
                        Brush.horizontalGradient(listOf(PrimaryPurple, AccentPink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE TALKING VIDEO ($selectedQuality)" else "GENERATE TALKING VIDEO ($cost CREDITS)",
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
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TALKING PHOTO GENERATED & SAVED!", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Saved to Firebase Storage automatically */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save / Download Talking Video", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
