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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
fun TextToVideoScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var prompt by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("Google Veo") } // Google Veo, Kling AI, Runway API
    var selectedStyle by remember { mutableStateOf("Cinematic") }
    var selectedQuality by remember { mutableStateOf("1080p Full HD") } // 480p, 720p HD, 1080p Full HD, 1440p 2K, 2160p 4K
    var selectedDuration by remember { mutableIntStateOf(10) } // 10, 15, 30, 60
    var selectedAspectRatio by remember { mutableStateOf("16:9") } // 9:16, 16:9, 1:1
    var selectedLanguage by remember { mutableStateOf("English") } // English, Telugu, Hindi, Spanish
    var isHdExport by remember { mutableStateOf(true) }

    val allVideoStyles = listOf(
        "Realistic", "Cinematic", "Pixar", "Disney",
        "Anime", "Cartoon", "3D Animation", "Indian Mythology",
        "Documentary", "Luxury", "Storytelling", "Product Advertisement"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    val creditCost = viewModel.calculateVideoCredits(selectedDuration)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("text_to_video_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Movie, contentDescription = "Text to Video", tint = PrimaryPurple)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("TEXT TO VIDEO GENERATOR", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("Powered by Google Veo, Kling AI & Runway API", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Character Consistency Lock & Selector
        CharacterSelectorCard(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // AI Model Selector
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SELECT AI MODEL ENGINE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Google Veo", "Kling AI", "Runway API").forEach { model ->
                        val isSelected = selectedModel == model
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSelected) AccentCyan else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { selectedModel = model }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(model, fontSize = 12.sp, color = if (isSelected) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 12 VIDEO STYLES PRESETS CARD
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("VIDEO STYLE PRESET (12 STYLES)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    allVideoStyles.chunked(3).forEach { rowStyles ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowStyles.forEach { style ->
                                val isSel = selectedStyle == style
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                        .clickable { selectedStyle = style }
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(style, fontSize = 10.sp, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, maxLines = 1)
                                }
                            }
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
                Text("PROMPT DESCRIPTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    placeholder = { Text("e.g. Cinematic video of a glowing futuristic city in rain, high motion, 8K ultra detail...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("prompt_text_field"),
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

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Language Prompt:", fontSize = 12.sp, color = Color.LightGray)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("English", "Telugu", "Hindi", "Spanish").forEach { lang ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedLanguage == lang) PrimaryPurple else Color(0xFF1E1A36))
                                    .clickable { selectedLanguage = lang }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(lang, fontSize = 11.sp, color = if (selectedLanguage == lang) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Video Quality Settings Card
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

        // Duration Options
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val totalEstCredits = viewModel.calculateVideoCredits(selectedDuration, selectedQuality)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("VIDEO DURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                    Text("Est. Total: $totalEstCredits Credits", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AccentGold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10, 15, 30, 60).forEach { dur ->
                        val isSelected = selectedDuration == dur
                        val durCost = viewModel.calculateVideoCredits(dur, selectedQuality)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSelected) AccentCyan else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { selectedDuration = dur }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${dur}s", fontWeight = FontWeight.Black, fontSize = 15.sp, color = if (isSelected) Color.White else Color.LightGray)
                                Text("$durCost Credits", fontSize = 10.sp, color = if (isSelected) AccentCyan else Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Aspect Ratio Options
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ASPECT RATIO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("9:16" to "Shorts / Reels", "16:9" to "YouTube HD", "1:1" to "Square Feed").forEach { (ratio, label) ->
                        val isSelected = selectedAspectRatio == ratio
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSelected) AccentGold else Color.Transparent, RoundedCornerShape(12.dp))
                                .clickable { selectedAspectRatio = ratio }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(ratio, fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color.White)
                                Text(label, fontSize = 9.sp, color = Color.LightGray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // HD Export Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Hd, contentDescription = "HD", tint = AccentCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("HD 1080p Ultra Render", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Switch(
                        checked = isHdExport,
                        onCheckedChange = { isHdExport = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PrimaryPurple
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        val finalEstCost = viewModel.calculateVideoCredits(selectedDuration, selectedQuality)
        // Generate Button
        Button(
            onClick = {
                if (prompt.isNotBlank()) {
                    viewModel.generateTextToVideo(
                        prompt = prompt,
                        durationSeconds = selectedDuration,
                        aspectRatio = selectedAspectRatio,
                        language = selectedLanguage,
                        isHd = isHdExport,
                        selectedModel = selectedModel,
                        quality = selectedQuality
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("generate_text_video_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            enabled = prompt.isNotBlank() && state !is GenerationState.Loading
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Generate", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (user.isPremium) "GENERATE ($selectedQuality)" else "GENERATE ($finalEstCost CREDITS | $selectedQuality)",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // State Indicator
        when (val currentState = state) {
            is GenerationState.Loading -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = AccentCyan)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(currentState.progressMessage, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Rendering frames on $selectedModel engine...", color = AccentPink, fontSize = 11.sp)
                    }
                }
            }

            is GenerationState.Success -> {
                Text("GENERATED VIDEO RESULT", fontWeight = FontWeight.Black, fontSize = 14.sp, color = AccentCyan)
                Spacer(modifier = Modifier.height(8.dp))
                VideoPlayerCard(item = currentState.item)
            }

            is GenerationState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF3B1414))
                        .border(1.dp, Color.Red, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(currentState.message, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            else -> {}
        }
    }
}
