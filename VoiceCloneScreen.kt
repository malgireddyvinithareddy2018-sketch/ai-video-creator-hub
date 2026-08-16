package com.example.ui.screens.generators

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ClonedVoice
import com.example.ui.theme.*
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun VoiceCloneScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf(0) } // 0: Clone & Train Voice, 1: Text-to-Speech (TTS), 2: Voice Library Management

    // Sample Upload State
    var sampleDurationSeconds by remember { mutableStateOf(30) } // 10s to 60s
    var isAudioSampleUploaded by remember { mutableStateOf(true) }
    var sampleFileName by remember { mutableStateOf("my_voice_sample_30s.mp3") }
    var isPlayingSamplePreview by remember { mutableStateOf(false) }
    var enableNoiseSuppression by remember { mutableStateOf(true) }

    // Voice Creation Form
    var customVoiceName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var selectedLanguage by remember { mutableStateOf("Telugu") }
    var selectedEmotion by remember { mutableStateOf("Energetic & Professional") }
    var voiceDescription by remember { mutableStateOf("") }

    // TTS Form
    var scriptText by remember { mutableStateOf("") }
    var selectedClonedVoiceId by remember { mutableStateOf<String?>(null) }
    var selectedTtsEmotion by remember { mutableStateOf("Energetic") }
    var selectedTtsLanguage by remember { mutableStateOf("Telugu") }

    // Library Filter
    var libraryFilterGender by remember { mutableStateOf("All") }

    val clonedVoices by viewModel.clonedVoices.collectAsState()
    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    val genderList = listOf("Male", "Female", "Non-Binary")

    val sampleDurations = listOf(10, 15, 30, 45, 60)

    val languagesList = listOf(
        "Telugu", "English", "Hindi", "Tamil",
        "Kannada", "Malayalam", "Spanish", "French",
        "German", "Japanese", "Korean", "Arabic"
    )

    val emotionsList = listOf(
        "Energetic & Professional",
        "Storytelling & Calm",
        "Excited & Happy",
        "Emotional & Dramatic",
        "News & Authoritative",
        "Whispering & Intimate"
    )

    val ttsEmotions = listOf(
        "Energetic", "Calm", "Excited", "Angry", "Storytelling", "Whispering"
    )

    // Default select first cloned voice if available
    LaunchedEffect(clonedVoices) {
        if (selectedClonedVoiceId == null && clonedVoices.isNotEmpty()) {
            selectedClonedVoiceId = clonedVoices.first().id
        }
    }

    val activeSelectedVoice = clonedVoices.find { it.id == selectedClonedVoiceId } ?: clonedVoices.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("voice_clone_screen")
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
                    Text("AI VOICE CLONE STUDIO", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PrimaryPurple)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("INSTANT CLONE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
                Text("Upload 10s-60s Audio, Train Personal Male/Female AI Voice & Multilingual TTS", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1. Clone Voice Sample", "2. Speech Generator", "3. Voice Library (${clonedVoices.size})").forEachIndexed { idx, title ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        .clickable { activeTab = idx }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else Color.LightGray,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            // TAB 0: UPLOAD 10s-60s VOICE SAMPLE & CREATE AI VOICE
            0 -> {
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
                            Text("1. UPLOAD AUDIO SAMPLE (10s - 60s)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF2E2954))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("${sampleDurationSeconds}s Length Selected", fontSize = 9.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Duration Selector (10s to 60s)
                        Text("Select Voice Sample Duration:", fontSize = 10.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            sampleDurations.forEach { dur ->
                                val isSelected = sampleDurationSeconds == dur
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSelected) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                        .clickable { sampleDurationSeconds = dur }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${dur}s", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.LightGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Upload Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E1A36))
                                .border(1.dp, if (isAudioSampleUploaded) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                                .clickable {
                                    isAudioSampleUploaded = true
                                    sampleFileName = "my_voice_sample_${sampleDurationSeconds}s.mp3"
                                    Toast.makeText(context, "${sampleDurationSeconds}s audio sample attached! 🎙️", Toast.LENGTH_SHORT).show()
                                }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (isAudioSampleUploaded) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    tint = if (isAudioSampleUploaded) AccentCyan else AccentGold,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isAudioSampleUploaded) "Sample Attached: $sampleFileName (${sampleDurationSeconds}s)" else "Tap to Select / Record Voice Sample (${sampleDurationSeconds}s MP3, WAV, M4A)",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                                Text("Clear background noise & speak naturally for best AI voice clone quality.", fontSize = 10.sp, color = Color.Gray)

                                if (isAudioSampleUploaded) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Button(
                                            onClick = {
                                                isPlayingSamplePreview = !isPlayingSamplePreview
                                                Toast.makeText(
                                                    context,
                                                    if (isPlayingSamplePreview) "Playing sample preview... 🔊" else "Paused preview",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                if (isPlayingSamplePreview) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (isPlayingSamplePreview) "Pause Preview" else "Listen Sample", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // AI Noise Suppression Option
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("AI Studio Noise Suppression & Clarity", fontSize = 11.sp, color = Color.White)
                            }
                            Switch(
                                checked = enableNoiseSuppression,
                                onCheckedChange = { enableNoiseSuppression = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = AccentCyan, checkedTrackColor = PrimaryPurple)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("2. CUSTOM VOICE PROFILE NAME", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customVoiceName,
                            onValueChange = { customVoiceName = it },
                            placeholder = { Text("e.g. My Personal Voice, CEO Studio Voice, RJ Vikram...", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentCyan,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("3. VOICE MODEL GENDER & PRIMARY LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            genderList.forEach { g ->
                                val isSel = selectedGender == g
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentPink else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedGender = g }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(g, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

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

                        Text("4. DEFAULT EMOTION & ACOUSTIC DESCRIPTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            emotionsList.chunked(2).forEach { rowEmotions ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowEmotions.forEach { emo ->
                                        val isSel = selectedEmotion == emo
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                .border(1.dp, if (isSel) AccentPink else Color.Transparent, RoundedCornerShape(10.dp))
                                                .clickable { selectedEmotion = emo }
                                                .padding(vertical = 8.dp, horizontal = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(emo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = voiceDescription,
                            onValueChange = { voiceDescription = it },
                            placeholder = { Text("e.g. Deep warm radio host tone with clean crystal clarity...", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
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

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val name = customVoiceName.ifBlank { "My Personal AI Voice ($selectedGender)" }
                        val newVoice = ClonedVoice(
                            id = "VOICE_${System.currentTimeMillis().toString().takeLast(6)}",
                            name = name,
                            gender = selectedGender,
                            sampleFileName = sampleFileName,
                            primaryLanguage = selectedLanguage,
                            emotionTone = selectedEmotion,
                            description = voiceDescription.ifBlank { "Cloned $selectedGender AI Voice Model trained on $sampleFileName (${sampleDurationSeconds}s audio sample)" }
                        )
                        viewModel.addClonedVoice(newVoice)
                        selectedClonedVoiceId = newVoice.id
                        Toast.makeText(context, "Personal AI Voice Model '$name' created & saved! 🎉", Toast.LENGTH_SHORT).show()
                        activeTab = 1 // Switch to Speech Generator
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
                            Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TRAIN & SAVE AI VOICE CLONE MODEL",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // TAB 1: SYNTHESIZE MULTI-LANGUAGE SPEECH WITH CLONED VOICE
            1 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("1. SELECT CLONED VOICE MODEL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(10.dp))

                        if (clonedVoices.isEmpty()) {
                            Text("No cloned voices found. Please train a voice first in 'Clone Voice Sample'.", color = AccentPink, fontSize = 11.sp)
                        } else {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(clonedVoices.size) { idx ->
                                    val v = clonedVoices[idx]
                                    val isSel = selectedClonedVoiceId == v.id
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                            .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                                            .clickable { selectedClonedVoiceId = v.id }
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Mic, contentDescription = null, tint = if (isSel) AccentGold else Color.Gray, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(v.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                                            }
                                            Text("${v.gender} • ${v.primaryLanguage}", fontSize = 10.sp, color = AccentCyan)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("2. EMOTION TONE & TARGET LANGUAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ttsEmotions.chunked(3).forEach { rowEmos ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowEmos.forEach { emo ->
                                        val isSel = selectedTtsEmotion == emo
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                                .clickable { selectedTtsEmotion = emo }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(emo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            languagesList.chunked(4).forEach { rowLangs ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowLangs.forEach { lang ->
                                        val isSel = selectedTtsLanguage == lang
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                                .clickable { selectedTtsLanguage = lang }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(lang, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("3. ENTER SCRIPT / TEXT TO SYNTHESIZE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = scriptText,
                            onValueChange = { scriptText = it },
                            placeholder = { Text("Enter script or speech in Telugu, English, Hindi etc...", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentPink,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val voice = activeSelectedVoice ?: return@Button
                        viewModel.generateClonedVoiceSpeech(
                            voiceId = voice.id,
                            voiceName = voice.name,
                            gender = voice.gender,
                            language = selectedTtsLanguage,
                            emotion = selectedTtsEmotion,
                            scriptText = scriptText.ifBlank { "Hello! Welcome to AI Studio Voice Cloning. This is your custom generated AI speech." }
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
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (user.isPremium) "GENERATE CLONED SPEECH ($selectedTtsLanguage)" else "GENERATE CLONED SPEECH (2 CREDITS)",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // TAB 2: VOICE LIBRARY MANAGEMENT
            2 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SAVED CUSTOM VOICE LIBRARY", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AccentGold)
                        Text("${clonedVoices.size} Voices", fontSize = 11.sp, color = Color.Gray)
                    }

                    // Male / Female Filter Bar
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("All", "Male", "Female").forEach { gFilter ->
                            val isSel = libraryFilterGender == gFilter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) PrimaryPurple else DarkSurface)
                                    .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(8.dp))
                                    .clickable { libraryFilterGender = gFilter }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(gFilter, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                            }
                        }
                    }

                    val displayedVoices = remember(clonedVoices, libraryFilterGender) {
                        if (libraryFilterGender == "All") clonedVoices
                        else clonedVoices.filter { it.gender.equals(libraryFilterGender, ignoreCase = true) }
                    }

                    if (displayedVoices.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("No Saved Cloned Voices Matching Criteria", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Text("Upload a voice sample in 'Clone Voice Sample' tab to train custom male/female voices.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        displayedVoices.forEach { voice ->
                            val isSelected = selectedClonedVoiceId == voice.id
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = if (isSelected) PrimaryPurple.copy(alpha = 0.3f) else DarkSurface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (isSelected) AccentGold else Color(0xFF2E2954), RoundedCornerShape(14.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Mic, contentDescription = null, tint = if (isSelected) AccentGold else Color.White)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(voice.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                        }

                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(AccentGold)
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text("SELECTED", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("🆔 ${voice.id} • ${voice.gender} • ${voice.primaryLanguage} • ${voice.emotionTone}", fontSize = 11.sp, color = AccentCyan)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(voice.description, fontSize = 10.sp, color = Color.LightGray)

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                selectedClonedVoiceId = voice.id
                                                activeTab = 1 // Switch to Speech Generator
                                                Toast.makeText(context, "Voice '${voice.name}' selected for Speech Generator! 🎙️", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Use in TTS", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                selectedClonedVoiceId = voice.id
                                                Toast.makeText(context, "Voice '${voice.name}' activated for Video Dubbing! 🎬", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2954)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Use in Video Dubbing", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                viewModel.deleteClonedVoice(voice.id)
                                                Toast.makeText(context, "Voice '${voice.name}' deleted.", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
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
                            Text("CLONED SPEECH READY", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { Toast.makeText(context, "Audio file downloaded successfully! 🎵", Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download Studio MP3 Audio", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
