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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AiCharacter
import com.example.ui.components.CharacterSelectorCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CharacterMasterScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var activeTab by remember { mutableStateOf(0) } // 0: Master Character Creator, 1: Expressions, 2: Poses & Actions, 3: Outfits, 4: 360 Sheet Views, 5: Character Library, 6: Consistent Video Generator

    // Create Master Character Form State
    var charName by remember { mutableStateOf("") }
    var charType by remember { mutableStateOf("Human Realism") } // Human Realism, 3D Pixar Style, Anime / Manga, Cyberpunk Hero
    var gender by remember { mutableStateOf("Male") }
    var hairStyle by remember { mutableStateOf("Short Wavy Dark Hair") }
    var defaultOutfit by remember { mutableStateOf("Casual Denim Jacket & White Tee") }
    var facialFeatures by remember { mutableStateOf("Sharp Jawline, Brown Eyes, Clean Shaved") }
    var customDescription by remember { mutableStateOf("") }
    var isConsistentLock by remember { mutableStateOf(true) }

    // Video Prompt State
    var videoActionPrompt by remember { mutableStateOf("") }
    var selectedGeneratorDestination by remember { mutableStateOf("Prompt to Video") }

    val activeCharacter by viewModel.activeCharacter.collectAsState()
    val savedCharacters by viewModel.savedCharacters.collectAsState()
    val state by viewModel.generationState.collectAsState()
    val user by viewModel.user.collectAsState()

    val tabTitles = listOf(
        "1. Create Master",
        "2. Expressions",
        "3. Poses",
        "4. Outfits",
        "5. 360° Views",
        "6. Library (${savedCharacters.size})",
        "7. Video Generator"
    )

    val expressionsList = listOf(
        "Happy & Smiling" to "Warm welcoming smile with bright eyes and natural lighting",
        "Angry & Intense" to "Clenched jaw, sharp focused gaze, dramatic high-contrast lighting",
        "Surprised & Shocked" to "Wide open eyes, expressive parted lips, dynamic posture",
        "Sad & Somber" to "Subtle downturned eyes, moody soft cinematic shadow",
        "Serious & Authoritative" to "Strong confident look, calm intense eyes, studio portrait",
        "Joyful Laughing" to "Open energetic candid laugh, head slightly tilted back",
        "Winking & Playful" to "Playful single eye wink with a charming smirk",
        "Thoughtful & Pensive" to "Gazing sideways with deep contemplation, rim lighting"
    )

    val posesList = listOf(
        "Standing Hero Stance" to "Full-body confident standing pose, chest out, hands on waist",
        "Sitting at Desk / Studio" to "Relaxed sitting pose at a modern desk with microphone/laptop",
        "Walking Down Street" to "Mid-stride cinematic walking shot down urban city street",
        "Running / Sprinting" to "High-energy athletic sprint pose with motion blur background",
        "Action Combat Stance" to "Dynamic fighting stance with clenched fists and ready posture",
        "Dancing / Stage" to "Rhythmic dance pose on illuminated stage with fluid motion",
        "Keynote Speaking" to "Delivering speech with expressive open hand gestures",
        "Fashion Runway Walk" to "Elegant high-fashion model walk on brightly lit catwalk"
    )

    val outfitsList = listOf(
        "Casual Streetwear" to "Oversized hoodie, ripped jeans, white sneakers & gold chain",
        "Formal Business Suit" to "Tailored navy blue suit, crisp white shirt & silk tie",
        "Royal Traditional Attire" to "Rich embroidered traditional sherwani/saree with gold jewelry",
        "Cyberpunk Tactical Armor" to "Futuristic matte black tactical body suit with glowing neon blue seams",
        "Superhero Costume" to "Sleek metallic armor suit with chest emblem & flowing cape",
        "Sporty Athletic Apparel" to "Performance gym tank top, athletic shorts & running shoes"
    )

    val angleViews = listOf(
        "Front View" to "Direct front face & symmetrical body stance",
        "Left Side View" to "90° Left profile face & body silhouette",
        "Right Side View" to "90° Right profile face & body silhouette",
        "Back View" to "Rear full body & hairstyle detail view",
        "45° Cinematic Angle" to "Three-quarter dynamic portrait lighting"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("character_master_screen")
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
                    Text("CHARACTER CONSISTENCY PRO", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentPink)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("PRO SUITE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
                Text("Master Characters, Face Lock ID, Expressions, Poses, Outfits & Universal Reuse", fontSize = 11.sp, color = AccentPink)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Character Consistency Lock Banner Card
        CharacterSelectorCard(viewModel = viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tabTitles.size) { idx ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentPink else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        .clickable { activeTab = idx }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tabTitles[idx],
                        color = if (isSel) Color.White else Color.LightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            // TAB 0: CREATE MASTER CHARACTER
            0 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("1. MASTER CHARACTER NAME & ID SYSTEM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = charName,
                            onValueChange = { charName = it },
                            placeholder = { Text("e.g. Inspector Vijay, Cyber Alex, Maya The Warrior...", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("2. CHARACTER ART STYLE & RENDER ENGINE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Human Realism", "3D Pixar Style", "Anime / Manga", "Cyberpunk Hero").forEach { type ->
                                val isSel = charType == type
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { charType = type }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(type, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("3. GENDER & BODY MODEL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Male", "Female", "Non-Binary").forEach { g ->
                                val isSel = gender == g
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentPink else Color.Transparent, RoundedCornerShape(10.dp))
                                        .clickable { gender = g }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(g, color = if (isSel) Color.White else Color.LightGray, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("4. HAIRSTYLE & FACIAL STRUCTURE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = hairStyle,
                            onValueChange = { hairStyle = it },
                            placeholder = { Text("e.g. Short wavy dark hair with subtle fade...", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("5. DEFAULT SIGNATURE OUTFIT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = defaultOutfit,
                            onValueChange = { defaultOutfit = it },
                            placeholder = { Text("e.g. Casual denim jacket & white tee, brown boots...", color = Color.Gray) },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("6. EXTRA FACIAL DETAILS & ACCESSORIES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = customDescription,
                            onValueChange = { customDescription = it },
                            placeholder = { Text("e.g. High cheekbones, athletic build, subtle scar on left eyebrow, aviator sunglasses...", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentPink,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Auto Save & Lock Master Character ID", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Assigns unique ID (CHAR_PRO_XXXX) for identical face in all generators", fontSize = 10.sp, color = Color.Gray)
                            }
                            Switch(
                                checked = isConsistentLock,
                                onCheckedChange = { isConsistentLock = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = AccentPink, checkedTrackColor = PrimaryPurple)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val name = charName.ifBlank { "Master Character" }
                        val fullPrompt = "Master Character '$name' ($charType, $gender): Hair: $hairStyle | Outfit: $defaultOutfit | Features: $facialFeatures | Extra: $customDescription"
                        viewModel.generateCharacter(
                            charName = name,
                            characterType = charType,
                            gender = gender,
                            style = charType,
                            isConsistent = isConsistentLock,
                            description = fullPrompt
                        )
                        Toast.makeText(context, "Master Character '$name' created & saved to library! 🎭", Toast.LENGTH_SHORT).show()
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
                                text = "GENERATE MASTER CHARACTER & ID (1 CREDIT)",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // TAB 1: CHARACTER EXPRESSIONS PACK
            1 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("FACIAL EXPRESSIONS PACK (SAME FACE ID)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentCyan)
                        if (activeCharacter != null) {
                            Text("Locked: ${activeCharacter?.id}", fontSize = 10.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text("Generates new image/video with exact facial structure under different emotional states:", fontSize = 11.sp, color = Color.Gray)

                    expressionsList.forEach { (expTitle, desc) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AccentPink.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.SentimentSatisfied, contentDescription = null, tint = AccentPink)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(expTitle, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Text(desc, color = Color.Gray, fontSize = 10.sp)
                                }
                                Button(
                                    onClick = {
                                        val targetChar = activeCharacter?.name ?: charName.ifBlank { "Master Character" }
                                        val targetId = activeCharacter?.id ?: "CHAR_ACTIVE"
                                        val prompt = "Close-up portrait of [$targetId] $targetChar with $expTitle expression: $desc"
                                        viewModel.generateTextToVideo(prompt = prompt, durationSeconds = 10, aspectRatio = "16:9", language = "English", isHd = true)
                                        Toast.makeText(context, "Rendering $expTitle expression for $targetChar... 🎭", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Render Face", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // TAB 2: POSES & ACTIONS
            2 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("POSES & ACTION SCENES (SAME FACE ID)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentPink)
                        if (activeCharacter != null) {
                            Text("Locked: ${activeCharacter?.id}", fontSize = 10.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text("Generates full-body dynamic action poses while keeping face and body build identical:", fontSize = 11.sp, color = Color.Gray)

                    posesList.forEach { (poseTitle, desc) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AccentGold.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = AccentGold)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(poseTitle, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Text(desc, color = Color.Gray, fontSize = 10.sp)
                                }
                                Button(
                                    onClick = {
                                        val targetChar = activeCharacter?.name ?: charName.ifBlank { "Master Character" }
                                        val targetId = activeCharacter?.id ?: "CHAR_ACTIVE"
                                        val prompt = "Cinematic video scene of [$targetId] $targetChar in $poseTitle pose: $desc"
                                        viewModel.generateTextToVideo(prompt = prompt, durationSeconds = 10, aspectRatio = "16:9", language = "English", isHd = true)
                                        Toast.makeText(context, "Rendering $poseTitle pose video... 🎬", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Create Pose", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // TAB 3: OUTFITS PACK
            3 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("CHARACTER OUTFIT VARIATIONS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentGold)
                        if (activeCharacter != null) {
                            Text("Locked Face: ${activeCharacter?.id}", fontSize = 10.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text("Change clothing and accessories while preserving the exact same face ID:", fontSize = 11.sp, color = Color.Gray)

                    outfitsList.forEach { (outfitName, outfitDesc) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AccentCyan.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Checkroom, contentDescription = null, tint = AccentCyan)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(outfitName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Text(outfitDesc, color = Color.Gray, fontSize = 10.sp)
                                }
                                Button(
                                    onClick = {
                                        val targetChar = activeCharacter?.name ?: charName.ifBlank { "Master Character" }
                                        val targetId = activeCharacter?.id ?: "CHAR_ACTIVE"
                                        val prompt = "Full body portrait of [$targetId] $targetChar wearing $outfitName: $outfitDesc"
                                        viewModel.generateTextToVideo(prompt = prompt, durationSeconds = 10, aspectRatio = "16:9", language = "English", isHd = true)
                                        Toast.makeText(context, "Rendering $outfitName variation... 👔", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Apply Outfit", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // TAB 4: 360 DEGREE VIEWS SHEET
            4 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("360° CHARACTER ROTATION SHEET VIEWS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentGold)
                    Text("Generates complete model turnaround reference sheet views:", fontSize = 11.sp, color = Color.Gray)

                    angleViews.forEach { (title, desc) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(PrimaryPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Camera, contentDescription = null, tint = Color.White)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Text(desc, color = Color.Gray, fontSize = 10.sp)
                                }
                                Button(
                                    onClick = {
                                        val targetChar = activeCharacter?.name ?: charName.ifBlank { "Master Character" }
                                        val targetId = activeCharacter?.id ?: "CHAR_ACTIVE"
                                        val prompt = "Turnaround reference sheet view of [$targetId] $targetChar ($title): $desc"
                                        viewModel.generateTextToVideo(prompt = prompt, durationSeconds = 10, aspectRatio = "16:9", language = "English", isHd = true)
                                        Toast.makeText(context, "Rendering $title turnaround view...", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Render Angle", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // TAB 5: SAVED CHARACTER LIBRARY & REUSE
            5 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SAVED MASTER CHARACTER LIBRARY", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AccentGold)
                        Text("${savedCharacters.size} Master Characters", fontSize = 11.sp, color = Color.Gray)
                    }

                    if (savedCharacters.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Face, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("No Saved Master Characters Yet", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Text("Create your first character in '1. Create Master' tab to get a unique Character ID.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        savedCharacters.forEach { charItem ->
                            val isLocked = activeCharacter?.id == charItem.id
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = if (isLocked) PrimaryPurple.copy(alpha = 0.3f) else DarkSurface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, if (isLocked) AccentGold else Color(0xFF2E2954), RoundedCornerShape(14.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Face,
                                                contentDescription = null,
                                                tint = if (isLocked) AccentGold else Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(charItem.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (isLocked) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(AccentGold)
                                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                                ) {
                                                    Text("ACTIVE FACE LOCK", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                            }

                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(charItem.id))
                                                    Toast.makeText(context, "Copied Character ID '${charItem.id}' 📋", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy ID", tint = AccentCyan, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("🆔 Character ID: ${charItem.id} • ${charItem.gender} • ${charItem.style}", fontSize = 11.sp, color = AccentCyan)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(charItem.seedPrompt, fontSize = 10.sp, color = Color.LightGray, maxLines = 2)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Action Buttons for Library Item
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Button(
                                            onClick = {
                                                if (isLocked) {
                                                    viewModel.setActiveCharacter(null)
                                                    Toast.makeText(context, "Face lock cleared.", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    viewModel.setActiveCharacter(charItem)
                                                    Toast.makeText(context, "Character '${charItem.name}' locked for all generators! 🔒", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = if (isLocked) Color.Red else PrimaryPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(if (isLocked) "Unlock Face" else "Lock Face ID", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.setActiveCharacter(charItem)
                                                Toast.makeText(context, "Character '${charItem.name}' applied to Text-to-Image, Videos & Stories! 🚀", Toast.LENGTH_SHORT).show()
                                                activeTab = 6 // Switch to Video Generator
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2954)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Reuse in Generators", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                viewModel.deleteCharacter(charItem)
                                                Toast.makeText(context, "Character '${charItem.name}' deleted.", Toast.LENGTH_SHORT).show()
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

            // TAB 6: CONSISTENT FACE VIDEO GENERATOR
            6 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = AccentGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CONSISTENT FACE VIDEO & IMAGE GENERATOR", fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (activeCharacter == null) {
                            Text(
                                text = "⚠️ No Master Character Currently Locked! Select a character from Library or create one in 'Create Master'.",
                                color = AccentPink,
                                fontSize = 11.sp
                            )
                        } else {
                            Text(
                                text = "🔒 Currently locked to: ${activeCharacter?.name} (Character ID: ${activeCharacter?.id})",
                                color = AccentCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Select Destination Generator:", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Prompt to Video", "Text to Image", "Story Generator").forEach { dest ->
                                val isSel = selectedGeneratorDestination == dest
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                        .clickable { selectedGeneratorDestination = dest }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(dest, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = videoActionPrompt,
                            onValueChange = { videoActionPrompt = it },
                            label = { Text("Describe video scene or image action", color = Color.Gray, fontSize = 12.sp) },
                            placeholder = { Text("e.g. Walking into a futuristic cyberpunk night club, looking back over shoulder with intense expression...", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentGold,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val charIdPrefix = if (activeCharacter != null) "[CHARACTER_ID:${activeCharacter?.id}] ${activeCharacter?.seedPrompt} - " else ""
                                val fullPrompt = "$charIdPrefix$videoActionPrompt"
                                viewModel.generateTextToVideo(prompt = fullPrompt, durationSeconds = 10, aspectRatio = "16:9", language = "English", isHd = true)
                                Toast.makeText(context, "Generating with Character ID lock! 🚀", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("GENERATE WITH CONSISTENT CHARACTER FACE", fontWeight = FontWeight.Black, fontSize = 11.sp, color = Color.White)
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
                            Text("CHARACTER MASTER RESULT READY", fontWeight = FontWeight.Bold, color = AccentPink, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(s.item.resultText, color = Color.LightGray, fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
            else -> {}
        }
    }
}
