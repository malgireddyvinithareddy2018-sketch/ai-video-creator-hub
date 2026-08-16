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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.components.CharacterSelectorCard
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ImageToVideoScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var selectedSampleImage by remember { mutableStateOf("Portrait Avatar") }
    var selectedModel by remember { mutableStateOf("Kling Image Animation") } // Kling Image Animation, Runway Gen-4
    var motionPrompt by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableIntStateOf(10) }
    var selectedAspectRatio by remember { mutableStateOf("9:16") }
    var selectedQuality by remember { mutableStateOf("1080p Full HD") }

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    val cost = viewModel.calculateVideoCredits(selectedDuration, selectedQuality)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("image_to_video_screen")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentPink.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = "Image to Video", tint = AccentPink)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("IMAGE TO VIDEO ANIMATOR", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("Powered by Kling Image Animation & Runway Gen-4", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Character Consistency Lock & Selector
        CharacterSelectorCard(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // AI Model Engine Selector
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ANIMATION MODEL ENGINE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Kling Image Animation", "Runway Gen-4").forEach { model ->
                        val isSel = selectedModel == model
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { selectedModel = model }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(model, fontSize = 12.sp, color = if (isSel) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image Picker Box
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SELECT IMAGE TO ANIMATE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1B1736))
                        .border(1.dp, PrimaryPurple, RoundedCornerShape(12.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Upload", tint = AccentCyan, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Tap to upload photo or choose sample below", color = Color.LightGray, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Or Select Preset Sample:", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Portrait Avatar", "Landscape Peak", "Anime Girl", "Product 3D").forEach { sample ->
                        val isSel = selectedSampleImage == sample
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .clickable { selectedSampleImage = sample }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(sample, fontSize = 11.sp, color = if (isSel) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Motion Prompt
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("MOTION DIRECTION PROMPT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = motionPrompt,
                    onValueChange = { motionPrompt = it },
                    placeholder = { Text("e.g. Camera zooms in slowly, hair blows gently in the wind, neon lights flicker...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    shape = RoundedCornerShape(12.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Video Quality Settings
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
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Duration & Aspect Ratio
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
                    Text("DURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                    Text("Est. Cost: $cost Credits", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AccentGold)
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
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${dur}s", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("$durCost Crd", color = if (isSel) AccentCyan else Color.Gray, fontSize = 9.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("9:16", "16:9", "1:1").forEach { ratio ->
                        val isSel = selectedAspectRatio == ratio
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .clickable { selectedAspectRatio = ratio }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(ratio, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Generate Button
        Button(
            onClick = {
                viewModel.generateImageToVideo(
                    imageLabel = selectedSampleImage,
                    prompt = motionPrompt.ifBlank { "Cinematic realistic motion animation" },
                    durationSeconds = selectedDuration,
                    aspectRatio = selectedAspectRatio,
                    selectedModel = selectedModel,
                    quality = selectedQuality
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_image_video_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            enabled = state !is GenerationState.Loading
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Animate", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (user.isPremium) "ANIMATE VIA $selectedModel" else "ANIMATE ($cost CREDITS)",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (val currentState = state) {
            is GenerationState.Loading -> {
                CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is GenerationState.Success -> {
                VideoPlayerCard(item = currentState.item)
            }
            is GenerationState.Error -> {
                Text(currentState.message, color = Color.Red, fontSize = 12.sp)
            }
            else -> {}
        }
    }
}
