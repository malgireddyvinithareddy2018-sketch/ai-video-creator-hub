package com.example.ui.screens.generators

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ScriptWriterScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val scriptCategories = listOf(
        "YouTube Video",
        "Shorts / Reels",
        "Product Ad",
        "Story & Mystery",
        "Podcast Episode"
    )

    var selectedCategory by remember { mutableStateOf("YouTube Video") }
    var topicPrompt by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("Telugu") }
    var selectedAudience by remember { mutableStateOf("General Creators & Tech Enthusiasts") }
    var selectedTone by remember { mutableStateOf("High-Energy & Viral") }
    var scriptDuration by remember { mutableStateOf("3 to 5 Minutes") }

    val languagesList = listOf(
        "Telugu", "English", "Hindi", "Tamil",
        "Kannada", "Malayalam", "Spanish", "French"
    )

    val tonesList = listOf(
        "High-Energy & Viral",
        "Cinematic & Suspenseful",
        "Professional & Educational",
        "Funny & Humorous",
        "Emotional & Inspirational",
        "Persuasive Sales Hook"
    )

    val durationsList = listOf(
        "30-60 Sec (Shorts)",
        "1 to 3 Minutes",
        "3 to 5 Minutes",
        "5 to 10 Minutes",
        "15+ Minutes (Deep Dive)"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("script_writer_screen")
    ) {
        // Top Bar
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
                    Text("AI SCRIPT WRITER STUDIO", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PrimaryPurple)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("MULTI-LANG", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
                Text("Generate Viral YouTube, Shorts, Product Ad, Story & Podcast Scripts", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Selector
        Text("1. SELECT SCRIPT TYPE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentGold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            scriptCategories.chunked(3).forEach { rowCats ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowCats.forEach { cat ->
                        val isSel = selectedCategory == cat
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) PrimaryPurple else DarkSurface)
                                .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = cat }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                cat,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.White else Color.LightGray,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Topic / Concept Input
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. TOPIC, CONCEPT OR PRODUCT NAME", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentCyan)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = topicPrompt,
                    onValueChange = { topicPrompt = it },
                    placeholder = {
                        Text(
                            when (selectedCategory) {
                                "Shorts / Reels" -> "e.g. 5 Mind-Blowing AI tools in 2026 you didn't know existed..."
                                "Product Ad" -> "e.g. Wireless Noise-Cancelling Earbuds - $49, Deep Bass, 40hr Battery..."
                                "Story & Mystery" -> "e.g. Sci-Fi time traveler stranded in ancient Rome with a smartphone..."
                                "Podcast Episode" -> "e.g. Future of Artificial General Intelligence with Tech Founder guest..."
                                else -> "e.g. How to build a successful YouTube channel starting from 0 subscribers..."
                            },
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = Color(0xFF2E2954)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("3. TARGET LANGUAGE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentPink)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    languagesList.chunked(4).forEach { rowLangs ->
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
                                    Text(lang, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("4. TONE & STYLE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentGold)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    tonesList.chunked(2).forEach { rowTones ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowTones.forEach { tone ->
                                val isSel = selectedTone == tone
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentPink else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedTone = tone }
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(tone, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("5. ESTIMATED DURATION", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    durationsList.chunked(3).forEach { rowDurs ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowDurs.forEach { dur ->
                                val isSel = scriptDuration == dur
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { scriptDuration = dur }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(dur, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                viewModel.generateAiScript(
                    scriptCategory = selectedCategory,
                    topicOrPrompt = topicPrompt.ifBlank { "Top 5 Viral Marketing Hacks for Content Creators" },
                    targetLanguage = selectedLanguage,
                    targetAudience = selectedAudience,
                    tone = selectedTone,
                    lengthMinutes = scriptDuration
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
                    Icon(Icons.Default.EditNote, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE SCRIPT ($selectedLanguage)" else "GENERATE SCRIPT (1 CREDIT)",
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentCyan)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SCRIPT GENERATED", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 13.sp)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(s.item.resultText))
                                        Toast.makeText(context, "Script copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryPurple)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                                }

                                IconButton(
                                    onClick = {
                                        val shareIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, s.item.resultText)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Share AI Script"))
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(AccentCyan)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        SelectionContainer {
                            Text(
                                s.item.resultText,
                                color = Color.LightGray,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(s.item.resultText))
                                    Toast.makeText(context, "Script Copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy Script", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                            }

                            Button(
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, s.item.resultText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Export TXT / Share Script"))
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export TXT / Share", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
