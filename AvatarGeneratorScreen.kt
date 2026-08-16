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
fun AvatarGeneratorScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var photoUrl by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("AI Human Avatar") } // AI Human Avatar, Cartoon Avatar, Anime Avatar, Business Avatar
    var lockConsistentFace by remember { mutableStateOf(true) }
    var customPrompt by remember { mutableStateOf("Professional lighting, futuristic background, studio quality") }
    var selectedQuality by remember { mutableStateOf("HD 1080p") }

    val avatarStyles = listOf(
        "AI Human Avatar",
        "Cartoon Avatar",
        "Anime Avatar",
        "Business Avatar"
    )

    val samplePhotos = listOf(
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400" to "Portrait Female",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400" to "Portrait Male",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400" to "Executive",
        "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400" to "Casual Model"
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
            .testTag("avatar_generator_screen")
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
                Text("AI AVATAR GENERATOR", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                Text("Human, Cartoon, Anime & Business Avatars with Face Lock", fontSize = 12.sp, color = AccentPink)
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
                Text("1. CREATE AVATAR FROM PHOTO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it },
                    label = { Text("Photo URL or Image Link", color = Color.Gray, fontSize = 12.sp) },
                    placeholder = { Text("Paste image URL (JPG / PNG)...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Face, contentDescription = null, tint = AccentCyan) },
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
                Text("Or choose a sample face portrait:", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    samplePhotos.forEach { (url, label) ->
                        val isSelected = photoUrl == url
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSelected) AccentPink else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                .clickable { photoUrl = url }
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

        // Avatar Style Selection
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. SELECT AVATAR STYLE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    avatarStyles.chunked(2).forEach { rowStyles ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowStyles.forEach { style ->
                                val isSel = selectedStyle == style
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentPink else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedStyle = style }
                                        .padding(vertical = 12.dp, horizontal = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = style,
                                        color = if (isSel) Color.White else Color.LightGray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Face Consistency Lock & Custom Prompt
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text("3. CONSISTENT CHARACTER FACE LOCK", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Text("Preserves facial features across all avatar renders", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = lockConsistentFace,
                        onCheckedChange = { lockConsistentFace = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AccentGold,
                            checkedTrackColor = PrimaryPurple
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = customPrompt,
                    onValueChange = { customPrompt = it },
                    label = { Text("Custom Details / Background / Outfit", color = Color.Gray, fontSize = 12.sp) },
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

        // HD Export Quality
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
                    Text("4. HD EXPORT QUALITY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                    Text("Est. Cost: $cost Credits", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AccentGold)
                }
                Spacer(modifier = Modifier.height(10.dp))

                val qualityOptions = listOf(
                    "Standard" to "+0",
                    "HD 1080p" to "+1 Credit",
                    "2K Ultra" to "+2 Credits",
                    "4K Cinema" to "+4 Credits"
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    qualityOptions.forEach { (qual, extraCost) ->
                        val isSel = selectedQuality == qual
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                .clickable { selectedQuality = qual }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(qual, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(extraCost, fontSize = 9.sp, color = if (isSel) AccentCyan else Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Avatar Button
        Button(
            onClick = {
                viewModel.generateAvatar(
                    photoUrl = photoUrl.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400" },
                    style = selectedStyle,
                    lockConsistentFace = lockConsistentFace,
                    customPrompt = customPrompt,
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
                        Brush.horizontalGradient(listOf(PrimaryPurple, AccentPink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Face, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE $selectedStyle ($selectedQuality)" else "GENERATE $selectedStyle ($cost CREDITS)",
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
                            Text("AI AVATAR SYNTHESIZED SUCCESSFULLY!", fontWeight = FontWeight.Bold, color = AccentPink, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { /* Export HD Avatar */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Export HD Avatar Image", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
