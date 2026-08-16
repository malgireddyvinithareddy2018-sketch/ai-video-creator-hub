package com.example.ui.screens.generators

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.*
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
fun MusicStudioScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val musicTypes = listOf(
        "Background Music 🎵",
        "Cinematic Music 🎬",
        "Motivational Music 🔥",
        "Podcast Music 🎙️",
        "Commercial Ad Music 📢",
        "Lo-Fi & Chill 🎧"
    )

    val genreStyles = listOf(
        "Orchestral & Epic",
        "EDM & Upbeat",
        "Acoustic Guitar",
        "Deep House",
        "Synthwave Cyberpunk",
        "Ambient Relaxing",
        "Hip Hop & Trap"
    )

    val moodOptions = listOf(
        "Epic & Heroic ⚡",
        "Inspiring & Uplifting ✨",
        "Energetic & Hype 🚀",
        "Dark & Suspenseful 🌑",
        "Calm & Peaceful 🌿",
        "Happy & Bright ☀️"
    )

    val durationOptions = listOf(15, 30, 60, 120, 180)
    val tempoOptions = listOf(
        "Slow (70 BPM)",
        "Moderate (100 BPM)",
        "Upbeat (125 BPM)",
        "High Energy (140+ BPM)"
    )

    var selectedMusicType by remember { mutableStateOf("Background Music 🎵") }
    var selectedGenre by remember { mutableStateOf("Orchestral & Epic") }
    var selectedMood by remember { mutableStateOf("Inspiring & Uplifting ✨") }
    var selectedDuration by remember { mutableIntStateOf(30) }
    var selectedTempo by remember { mutableStateOf("Upbeat (125 BPM)") }
    var isRoyaltyFree by remember { mutableStateOf(true) }

    var isPlayingAudio by remember { mutableStateOf(false) }
    var audioProgress by remember { mutableFloatStateOf(0f) }

    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    // Simulating audio playback progress bar
    LaunchedEffect(isPlayingAudio) {
        if (isPlayingAudio) {
            while (isPlayingAudio && audioProgress < 1f) {
                kotlinx.coroutines.delay(200)
                audioProgress = (audioProgress + 0.02f).coerceAtMost(1f)
                if (audioProgress >= 1f) {
                    isPlayingAudio = false
                    audioProgress = 0f
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("music_studio_screen")
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
                    Text("AI MUSIC STUDIO", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("ROYALTY FREE MP3", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Background, Cinematic, Motivational, Podcast & Ad Music", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Music Category
        Text("1. SELECT MUSIC CATEGORY", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentGold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            musicTypes.chunked(2).forEach { rowTypes ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowTypes.forEach { type ->
                        val isSel = selectedMusicType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) PrimaryPurple else DarkSurface)
                                .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                                .clickable { selectedMusicType = type }
                                .padding(vertical = 10.dp, horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                type,
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

        // Input Card Options
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Genre
                Text("2. GENRE & INSTRUMENTAL STYLE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentCyan)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    genreStyles.chunked(2).forEach { rowGenres ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowGenres.forEach { genre ->
                                val isSel = selectedGenre == genre
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedGenre = genre }
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(genre, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mood
                Text("3. MOOD & VIBE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentPink)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    moodOptions.chunked(2).forEach { rowMoods ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowMoods.forEach { mood ->
                                val isSel = selectedMood == mood
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedMood = mood }
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(mood, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Duration Selector
                Text("4. DURATION (SECONDS)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = AccentGold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    durationOptions.forEach { dur ->
                        val isSel = selectedDuration == dur
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { selectedDuration = dur }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${dur}s", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tempo / BPM
                Text("5. TEMPO / SPEED (BPM)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    tempoOptions.chunked(2).forEach { rowTempos ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowTempos.forEach { tempo ->
                                val isSel = selectedTempo == tempo
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedTempo = tempo }
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(tempo, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Royalty Free License Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E1A36))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("100% Royalty Free License", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Monetize on YouTube, Shorts, Reels & Ads", fontSize = 9.sp, color = Color.Gray)
                        }
                    }
                    Switch(
                        checked = isRoyaltyFree,
                        onCheckedChange = { isRoyaltyFree = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = PrimaryPurple)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                viewModel.generateAiMusicStudio(
                    musicType = selectedMusicType.replace(" 🎵", "").replace(" 🎬", "").replace(" 🔥", "").replace(" 🎙️", "").replace(" 📢", "").replace(" 🎧", ""),
                    genreStyle = selectedGenre,
                    mood = selectedMood.replace(" ⚡", "").replace(" ✨", "").replace(" 🚀", "").replace(" 🌑", "").replace(" 🌿", "").replace(" ☀️", ""),
                    durationSeconds = selectedDuration,
                    tempoBpm = selectedTempo,
                    isRoyaltyFree = isRoyaltyFree
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
                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (user.isPremium) "GENERATE MP3 MUSIC TRACK (${selectedDuration}s)" else "GENERATE MP3 MUSIC TRACK (1 CREDIT)",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Output State
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("ROYALTY FREE MP3 GENERATED", fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 12.sp)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PrimaryPurple)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("320kbps MP3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Audio Player Widget
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF1E1A36))
                                .border(1.dp, AccentGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = {
                                            isPlayingAudio = !isPlayingAudio
                                            if (isPlayingAudio) {
                                                Toast.makeText(context, "Playing AI Music Track Preview 🎧", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(AccentGold)
                                    ) {
                                        Icon(
                                            if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = "Play/Pause",
                                            tint = Color.Black,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(s.item.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp, maxLines = 1)
                                        Text("${selectedDuration}s | Stereo 320kbps MP3 Master", fontSize = 10.sp, color = AccentCyan)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Audio Waveform & Slider Progress
                                Slider(
                                    value = audioProgress,
                                    onValueChange = {
                                        audioProgress = it
                                        isPlayingAudio = false
                                    },
                                    colors = SliderDefaults.colors(
                                        thumbColor = AccentGold,
                                        activeTrackColor = AccentGold,
                                        inactiveTrackColor = Color(0xFF2E2954)
                                    ),
                                    modifier = Modifier.fillMaxWidth().height(20.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val currentSecs = (audioProgress * selectedDuration).toInt()
                                    Text(String.format("%02d:%02d", currentSecs / 60, currentSecs % 60), fontSize = 10.sp, color = Color.Gray)
                                    Text(String.format("%02d:%02d", selectedDuration / 60, selectedDuration % 60), fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // MP3 Download & Export Button
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(s.item.resultUrl))
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "🎵 Download AI Royalty Free Music Track (${s.item.title}): ${s.item.resultUrl}\n\nGenerated with AI Video Creator & Music Studio!")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Export MP3 Track"))
                                Toast.makeText(context, "MP3 Download Link copied & export ready! 🚀", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("EXPORT & DOWNLOAD MP3 (320kbps)", fontWeight = FontWeight.Black, fontSize = 11.sp, color = Color.Black)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Full Composition Breakdown Text
                        Text("COMPOSER ARRANGEMENT & LICENSE DETAILS:", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        SelectionContainer {
                            Text(
                                s.item.resultText,
                                color = Color.LightGray,
                                fontSize = 11.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
