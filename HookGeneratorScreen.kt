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
fun HookGeneratorScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val hookCategories = listOf(
        "All Types Suite",
        "Viral Hooks 💥",
        "Curiosity Hooks 🤔",
        "Emotional Hooks ❤️",
        "Sales Hooks 💰",
        "Motivation Hooks 🔥",
        "Story Hooks 📖"
    )

    var selectedCategory by remember { mutableStateOf("All Types Suite") }
    var topicPrompt by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("Telugu") }
    var targetAudience by remember { mutableStateOf("Reels & Shorts Creators") }

    val languagesList = listOf(
        "Telugu", "English", "Hindi", "Tamil",
        "Kannada", "Malayalam", "Spanish", "French"
    )

    val audienceList = listOf(
        "Reels & Shorts Creators",
        "E-Commerce & Product Buyers",
        "Tech Enthusiasts & Gamers",
        "Fitness & Lifestyle Seekers",
        "Entrepreneurs & Freelancers"
    )

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("hook_generator_screen")
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
                    Text("AI HOOK GENERATOR STUDIO", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentPink)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("VIRAL HOOKS", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
                Text("Viral, Curiosity, Emotional, Sales, Motivation & Story Hooks", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hook Type Selection Chips
        Text("1. SELECT HOOK CATEGORY", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentGold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            hookCategories.chunked(2).forEach { rowCats ->
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
                                .padding(vertical = 10.dp, horizontal = 6.dp),
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

        // Input Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. VIDEO TOPIC, PRODUCT OR CONCEPT", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentCyan)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = topicPrompt,
                    onValueChange = { topicPrompt = it },
                    placeholder = {
                        Text(
                            when (selectedCategory) {
                                "Curiosity Hooks 🤔" -> "e.g. The secret website that Google doesn't want you to know about..."
                                "Sales Hooks 💰" -> "e.g. Wireless Noise Cancelling Headphones on 50% discount today..."
                                "Emotional Hooks ❤️" -> "e.g. How I quit my 9-to-5 job after 5 years of struggling..."
                                "Motivation Hooks 🔥" -> "e.g. Stop wasting your morning routine if you want to be rich in 2026..."
                                "Story Hooks 📖" -> "e.g. I accidentally deleted my client's $100,000 database at 2 AM..."
                                else -> "e.g. 3 AI Video generation tricks to get 1 Million views on Instagram Reels..."
                            },
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
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

                Text("4. TARGET AUDIENCE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentGold)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    audienceList.forEach { aud ->
                        val isSel = targetAudience == aud
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple.copy(alpha = 0.3f) else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { targetAudience = aud }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = isSel, onClick = { targetAudience = aud })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(aud, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                viewModel.generateAiHookSuite(
                    hookCategory = selectedCategory,
                    topic = topicPrompt.ifBlank { "Top 5 Viral Secrets to Grow on YouTube Shorts" },
                    targetLanguage = selectedLanguage,
                    targetAudience = targetAudience
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
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE VIRAL HOOKS ($selectedLanguage)" else "GENERATE VIRAL HOOKS (1 CREDIT)",
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentCyan)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("VIRAL HOOKS READY", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 13.sp)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(s.item.resultText))
                                        Toast.makeText(context, "All hooks copied to clipboard! 📋", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copy All", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                IconButton(
                                    onClick = {
                                        val shareIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, s.item.resultText)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Share Viral Hooks"))
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

                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(s.item.resultText))
                                Toast.makeText(context, "1-Click Copy Successful! 🚀", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPink),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("1-CLICK COPY ALL HOOKS", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
