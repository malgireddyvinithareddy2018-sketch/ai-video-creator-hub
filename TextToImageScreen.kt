package com.example.ui.screens.generators

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CharacterSelectorCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun TextToImageScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var prompt by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("Gemini Image Generation") } // Gemini Image Generation, Imagen 3
    var selectedStyle by remember { mutableStateOf("Photorealistic") }
    var selectedAspectRatio by remember { mutableStateOf("1:1") }
    var selectedQuality by remember { mutableStateOf("Standard") } // Standard, HD, Ultra HD, 2K, 4K

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    val stylesList = listOf("Photorealistic", "Anime / Manga", "Cinematic 3D", "Cyberpunk Neon", "Fantasy Painting", "Brutalist Vector")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("text_to_image_screen")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentCyan.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = "Text to Image", tint = AccentCyan)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("TEXT TO IMAGE GENERATOR", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("Powered by Gemini 2.5 Flash Image & Imagen 3", fontSize = 12.sp, color = AccentPink)
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
                Text("IMAGE AI MODEL ENGINE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Gemini Image Generation", "Imagen 3").forEach { model ->
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

        // Prompt Input
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("IMAGE PROMPT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("e.g. Futuristic lion avatar wearing glowing sci-fi armor, studio lighting...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
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

        // Styles Grid
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ART STYLE PRESETS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    stylesList.chunked(2).forEach { rowStyles ->
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
                                        .padding(vertical = 10.dp, horizontal = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(style, fontSize = 11.sp, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aspect Ratio
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ASPECT RATIO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("1:1", "9:16", "16:9", "4:3").forEach { ratio ->
                        val isSel = selectedAspectRatio == ratio
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .clickable { selectedAspectRatio = ratio }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(ratio, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image Quality Settings
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val imageCost = viewModel.calculateImageCredits(selectedQuality)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("IMAGE QUALITY SETTINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    Text("Est. Cost: $imageCost Credits", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AccentGold)
                }
                Text("Higher resolution adds generation credits", fontSize = 10.sp, color = Color.Gray)
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

        val finalImageCost = viewModel.calculateImageCredits(selectedQuality)
        // Generate Button
        Button(
            onClick = {
                if (prompt.isNotBlank()) {
                    viewModel.generateTextToImage(prompt, selectedStyle, selectedAspectRatio, selectedModel, quality = selectedQuality)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_image_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            enabled = prompt.isNotBlank() && state !is GenerationState.Loading
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Create Art", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (user.isPremium) "GENERATE ART ($selectedQuality)" else "GENERATE ART ($finalImageCost CREDITS | $selectedQuality)",
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
                Text("GENERATED AI ARTWORK", fontWeight = FontWeight.Black, fontSize = 14.sp, color = AccentCyan)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(PrimaryPurple, Color(0xFF0F0B1A))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(AccentPink.copy(alpha = 0.8f), Color.Transparent)
                                    ),
                                    radius = size.width * 0.4f,
                                    center = Offset(size.width * 0.5f, size.height * 0.5f)
                                )
                            }
                            Text("✨ AI Image Render\n[$selectedModel - $selectedStyle]", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(currentState.item.resultText, color = Color.LightGray, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = "Download", tint = AccentCyan)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Download", color = AccentCyan, fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = AccentPink)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Share", color = AccentPink, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            is GenerationState.Error -> {
                Text(currentState.message, color = Color.Red, fontSize = 12.sp)
            }

            else -> {}
        }
    }
}
