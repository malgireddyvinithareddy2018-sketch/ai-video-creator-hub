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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

// CHARACTER GENERATOR
@Composable
fun CharacterGeneratorScreen(viewModel: MainViewModel, onBackClick: () -> Unit) {
    var charName by remember { mutableStateOf("") }
    var charType by remember { mutableStateOf("Human") } // Human, Cartoon, Anime
    var gender by remember { mutableStateOf("Male") }
    var isConsistent by remember { mutableStateOf(true) }
    var description by remember { mutableStateOf("") }
    var activeRefTab by remember { mutableStateOf(0) } // 0: Angles, 1: Expressions, 2: Actions

    val state by viewModel.generationState.collectAsState()
    val activeCharacter by viewModel.activeCharacter.collectAsState()
    val savedCharacters by viewModel.savedCharacters.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("character_generator_screen")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Face, contentDescription = "AI Character", tint = PrimaryPurple)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("AI CHARACTER GENERATOR & SHEET", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("Auto Reference Sheets (Views, Expressions & Actions) + ID Reuse", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Character Lock & Selector Card
        com.example.ui.components.CharacterSelectorCard(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Character Inputs Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("CHARACTER NAME & IDENTIFIER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = charName,
                    onValueChange = { charName = it },
                    placeholder = { Text("e.g. Alex - Cyber Founder", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B1736),
                        unfocusedContainerColor = Color(0xFF1B1736),
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = Color(0xFF2E2954),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("CHARACTER STYLE MODEL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Human", "Cartoon 3D", "Anime").forEach { type ->
                        val isSel = charType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .clickable { charType = type }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(type, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("GENDER / AVATAR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Male", "Female", "Non-Binary").forEach { g ->
                        val isSel = gender == g
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                .clickable { gender = g }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(g, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("PHYSICAL DETAILS, OUTFIT & COLORS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("e.g. 25 year old founder, sharp jawline, short dark hair, black leather jacket over grey hoodie, neon blue sneakers...", color = Color.Gray, fontSize = 13.sp) },
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

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Save & Auto Generate Reference Sheet", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Creates 13-angle reference sheet + Character ID lock", fontSize = 10.sp, color = Color.Gray)
                    }
                    Switch(checked = isConsistent, onCheckedChange = { isConsistent = it })
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.generateCharacter(charName, charType, gender, charType, isConsistent, description)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("generate_character_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("GENERATE CHARACTER & REFERENCE SHEET (1 CREDIT)", fontWeight = FontWeight.Black, color = Color.White, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (val currentState = state) {
            is GenerationState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(color = AccentCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(currentState.progressMessage, color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            is GenerationState.Success -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("CHARACTER & REFERENCE SHEET CREATED", color = AccentCyan, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(currentState.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
            else -> {}
        }

        // CHARACTER REFERENCE SHEET DISPLAY BOARD
        if (activeCharacter != null) {
            val char = activeCharacter!!
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("AUTOMATED REFERENCE SHEET", fontSize = 12.sp, fontWeight = FontWeight.Black, color = AccentGold)
                            Text("Character ID: ${char.id} (${char.name})", fontSize = 11.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryPurple)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("13 POSES GENERATED", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sheet Tabs
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Angles & Views (5)", "Expressions (4)", "Actions & Poses (4)").forEachIndexed { idx, tabTitle ->
                            val isSel = activeRefTab == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                    .clickable { activeRefTab = idx }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(tabTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    when (activeRefTab) {
                        0 -> { // Angles / Views
                            val views = listOf(
                                "Front View" to char.frontViewUrl,
                                "Left Side View" to char.leftSideViewUrl,
                                "Right Side View" to char.rightSideViewUrl,
                                "Back View" to char.backViewUrl,
                                "45 Degree View" to char.fortyFiveDegreeViewUrl
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                views.forEach { (label, url) ->
                                    ReferencePoseCard(label = label, charId = char.id, detailText = "Maintain exact face structure, hairstyle & outfit color palette")
                                }
                            }
                        }
                        1 -> { // Expressions
                            val expressions = listOf(
                                "Happy Expression" to char.happyExpressionUrl,
                                "Sad Expression" to char.sadExpressionUrl,
                                "Angry Expression" to char.angryExpressionUrl,
                                "Fearful Expression" to char.fearfulExpressionUrl
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                expressions.forEach { (label, url) ->
                                    ReferencePoseCard(label = label, charId = char.id, detailText = "Facial emotion lock with consistent eye & skin tones")
                                }
                            }
                        }
                        2 -> { // Actions
                            val actions = listOf(
                                "Talking Pose" to char.talkingActionUrl,
                                "Walking Motion" to char.walkingActionUrl,
                                "Running Motion" to char.runningActionUrl,
                                "Sitting Position" to char.sittingActionUrl
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                actions.forEach { (label, url) ->
                                    ReferencePoseCard(label = label, charId = char.id, detailText = "Full body posture & motion consistency across video frames")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReferencePoseCard(label: String, charId: String, detailText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1A36))
            .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryPurple.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Face, contentDescription = label, tint = AccentCyan)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                Text(detailText, fontSize = 10.sp, color = Color.Gray)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF2E2954))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(charId, fontSize = 9.sp, color = AccentGold, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// VOICE GENERATOR
@Composable
fun VoiceGeneratorScreen(viewModel: MainViewModel, onBackClick: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var selectedLang by remember { mutableStateOf("Telugu") } 
    var selectedVoiceType by remember { mutableStateOf("Female") }

    val languagesList = listOf(
        "Telugu", "English", "Hindi", "Tamil",
        "Kannada", "Malayalam", "Arabic", "Japanese",
        "Korean", "French", "German", "Spanish"
    )

    val voiceTypesList = listOf(
        "Male", "Female", "Child", "Narrator",
        "News Reader", "Advertisement", "Emotional"
    )

    val state by viewModel.generationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("voice_generator_screen")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentCyan.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.RecordVoiceOver, contentDescription = "AI Voice", tint = AccentCyan)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("MULTI-LANGUAGE AI VOICE GENERATOR", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("12 Languages • 7 Studio Voice Types • Speech Synthesis", fontSize = 12.sp, color = AccentPink)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("SELECT VOICE LANGUAGE (12 LANGUAGES)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    languagesList.chunked(3).forEach { rowLangs ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowLangs.forEach { lang ->
                                val isSel = selectedLang == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedLang = lang }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(lang, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("SELECT VOICE TYPE / EMOTION (7 VOICE TYPES)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    voiceTypesList.chunked(3).forEach { rowVoices ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowVoices.forEach { vt ->
                                val isSel = selectedVoiceType == vt
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { selectedVoiceType = vt }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(vt, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("VOICEOVER SCRIPT TEXT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Paste or type your script here in Telugu or English...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
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

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    viewModel.generateVoice(text, selectedVoiceType, selectedLang, "$selectedLang-$selectedVoiceType")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("generate_voice_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            enabled = text.isNotBlank()
        ) {
            Text("GENERATE STUDIO VOICE (1 CREDIT)", fontWeight = FontWeight.Black, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (val currentState = state) {
            is GenerationState.Loading -> CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
            is GenerationState.Success -> {
                Text("VOICEOVER AUDIO READY 🔊", color = AccentCyan, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(currentState.item.resultText, color = Color.LightGray, fontSize = 12.sp)
            }
            else -> {}
        }
    }
}

// PRODUCT LINK TO VIDEO
@Composable
fun ProductLinkToVideoScreen(viewModel: MainViewModel, onBackClick: () -> Unit) {
    var url by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf(15) }

    val state by viewModel.generationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("product_to_video_screen")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ShoppingBag, contentDescription = "Product Video", tint = AccentGold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("PRODUCT LINK TO VIDEO", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("Auto-generate viral product ads from Amazon/Shopify URLs", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("PASTE PRODUCT URL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = { Text("https://amazon.com/dp/B08N5WRWNW or Shopify product link...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(12.dp))

                Text("KEY HIGHLIGHTS / PROMO OFFER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("e.g. 50% Off sale ending today, Free shipping...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (url.isNotBlank()) {
                    viewModel.generateProductToVideo(url, notes, duration, "9:16")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("generate_product_video_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            enabled = url.isNotBlank()
        ) {
            Text("GENERATE AUTO PRODUCT PROMO VIDEO", fontWeight = FontWeight.Black, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (val currentState = state) {
            is GenerationState.Loading -> CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
            is GenerationState.Success -> VideoPlayerCard(item = currentState.item)
            else -> {}
        }
    }
}
