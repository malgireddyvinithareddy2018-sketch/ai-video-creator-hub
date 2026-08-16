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
import com.example.data.models.ConnectedAccount
import com.example.data.models.SocialPost
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SocialAutoPostingScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var activeTab by remember { mutableStateOf(0) } // 0: Auto Post & Schedule, 1: Connect Accounts, 2: Publishing History

    // Form State
    var postTopic by remember { mutableStateOf("") }
    var postTitle by remember { mutableStateOf("") }
    var postDescription by remember { mutableStateOf("") }
    var postHashtags by remember { mutableStateOf("#AIVideo #Reels #YouTubeShorts #Trending2026") }
    var selectedMediaType by remember { mutableStateOf("Shorts/Reel") } // Shorts/Reel, Video, Image, Community Post

    // Platforms Selection
    var selectedPlatforms by remember { mutableStateOf(setOf("YouTube", "Instagram", "Facebook", "Telegram")) }

    // Scheduling State
    var isScheduleMode by remember { mutableStateOf(false) }
    var schedulePreset by remember { mutableStateOf("Today at 06:00 PM") } // Preset time slots

    // Connect Account Dialog State
    var showAddAccountDialog by remember { mutableStateOf(false) }
    var newPlatform by remember { mutableStateOf("YouTube") }
    var newAccountName by remember { mutableStateOf("") }
    var newHandle by remember { mutableStateOf("") }

    var isGeneratingMetadata by remember { mutableStateOf(false) }

    val connectedAccounts by viewModel.connectedAccounts.collectAsState()
    val publishingHistory by viewModel.socialPostsHistory.collectAsState()
    val user by viewModel.user.collectAsState()

    val availablePlatforms = listOf("YouTube", "Instagram", "Facebook", "Telegram")
    val mediaTypes = listOf("Shorts/Reel", "Video", "Image", "Post")
    val scheduleTimeSlots = listOf("Today at 06:00 PM", "Tomorrow at 09:00 AM", "Tomorrow at 08:00 PM", "Weekend Special (Sat 10 AM)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("social_auto_posting_screen")
    ) {
        // Top Header Bar
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
                    Text("SOCIAL AUTO POSTING", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentCyan)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("MULTI-PLATFORM", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Instant Publish & Schedule to YouTube, Instagram, Facebook & Telegram", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1. Auto Post Studio", "2. Accounts (${connectedAccounts.count { it.isConnected }}/${connectedAccounts.size})", "3. History (${publishingHistory.size})").forEachIndexed { idx, title ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(12.dp))
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
            // TAB 0: AUTO POST & SCHEDULE STUDIO
            0 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("1. SELECT TARGET PLATFORMS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            availablePlatforms.forEach { platform ->
                                val isConnected = connectedAccounts.any { it.platform == platform && it.isConnected }
                                val isSelected = selectedPlatforms.contains(platform)
                                val badgeColor = when (platform) {
                                    "YouTube" -> Color(0xFFFF0000)
                                    "Instagram" -> AccentPink
                                    "Facebook" -> Color(0xFF1877F2)
                                    else -> AccentCyan // Telegram
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) badgeColor.copy(alpha = 0.25f) else Color(0xFF1E1A36))
                                        .border(
                                            1.dp,
                                            if (isSelected) badgeColor else Color(0xFF2E2954),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            if (!isConnected) {
                                                Toast.makeText(context, "$platform account is disconnected. Connect in Tab 2!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                selectedPlatforms = if (isSelected) {
                                                    selectedPlatforms - platform
                                                } else {
                                                    selectedPlatforms + platform
                                                }
                                            }
                                        }
                                        .padding(vertical = 10.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            platform,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Color.LightGray
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            if (isConnected) "Connected" else "Offline",
                                            fontSize = 9.sp,
                                            color = if (isConnected) AccentCyan else Color.Red
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("2. SELECT CONTENT MEDIA TYPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            mediaTypes.forEach { mType ->
                                val isSel = selectedMediaType == mType
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                        .clickable { selectedMediaType = mType }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(mType, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // AI Auto Metadata Generator Box
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A36)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, AccentGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("AI METADATA GENERATOR", fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            isGeneratingMetadata = true
                                            viewModel.generateAutoSocialMetadata(
                                                topic = postTopic,
                                                targetPlatform = selectedPlatforms.firstOrNull() ?: "YouTube",
                                                onResult = { genTitle, genDesc, genTags ->
                                                    postTitle = genTitle
                                                    postDescription = genDesc
                                                    postHashtags = genTags
                                                    isGeneratingMetadata = false
                                                    Toast.makeText(context, "AI Title, Description & Hashtags generated! ✨", Toast.LENGTH_SHORT).show()
                                                }
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        enabled = !isGeneratingMetadata
                                    ) {
                                        if (isGeneratingMetadata) {
                                            CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
                                        } else {
                                            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Auto Generate All", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = postTopic,
                                    onValueChange = { postTopic = it },
                                    placeholder = { Text("Enter video or post topic (e.g., Cyberpunk AI character breakdown, 5 Passive Income Ideas)...", color = Color.Gray, fontSize = 11.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true,
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

                        // Manual Title, Description, Hashtag Editors
                        Text("3. POST TITLE (AUTO OR CUSTOM)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = postTitle,
                            onValueChange = { postTitle = it },
                            placeholder = { Text("Catchy viral headline with emojis...", color = Color.Gray, fontSize = 12.sp) },
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

                        Text("4. POST DESCRIPTION & CAPTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = postDescription,
                            onValueChange = { postDescription = it },
                            placeholder = { Text("Engaging summary, call-to-action & link details...", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentCyan,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("5. AUTO VIRAL HASHTAGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = postHashtags,
                            onValueChange = { postHashtags = it },
                            placeholder = { Text("#AIVideo #Reels #Shorts #Viral #Tech", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentPink,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Publishing Type: Instant vs Scheduled
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isScheduleMode) "SCHEDULED PUBLISHING MODE 📅" else "INSTANT PUBLISHING MODE ⚡",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isScheduleMode) "Set future queue slot for optimal engagement" else "Publish directly to selected social networks now",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                            Switch(
                                checked = isScheduleMode,
                                onCheckedChange = { isScheduleMode = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = PrimaryPurple)
                            )
                        }

                        if (isScheduleMode) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Select Scheduled Queue Slot:", fontSize = 10.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                scheduleTimeSlots.chunked(2).forEach { rowSlots ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        rowSlots.forEach { slot ->
                                            val isSel = schedulePreset == slot
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                    .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                                    .clickable { schedulePreset = slot }
                                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(slot, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button
                Button(
                    onClick = {
                        val tagsList = postHashtags.split(" ", ",").filter { it.isNotBlank() }
                        viewModel.publishOrScheduleSocialPost(
                            title = postTitle.ifBlank { "Viral AI Video Showcase" },
                            description = postDescription.ifBlank { "Generated using AI Studio Multi-Platform Auto Posting Engine." },
                            hashtags = tagsList,
                            selectedPlatforms = selectedPlatforms.toList(),
                            mediaType = selectedMediaType,
                            isScheduled = isScheduleMode,
                            scheduledDateTime = schedulePreset,
                            onResult = { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                if (success) {
                                    activeTab = 2 // Switch to History
                                }
                            }
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
                                Brush.horizontalGradient(
                                    if (isScheduleMode) listOf(PrimaryPurple, AccentGold) else listOf(PrimaryPurple, AccentCyan)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isScheduleMode) Icons.Default.Schedule else Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isScheduleMode) "SCHEDULE POST TO ${selectedPlatforms.size} PLATFORMS" else "INSTANT PUBLISH TO ${selectedPlatforms.size} PLATFORMS",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // TAB 1: CONNECT & MANAGE ACCOUNTS
            1 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("CONNECTED SOCIAL ACCOUNTS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AccentGold)
                        Button(
                            onClick = { showAddAccountDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Connect Account", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    connectedAccounts.forEach { acc ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (acc.isConnected) AccentCyan else Color(0xFF2E2954),
                                    RoundedCornerShape(14.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when (acc.platform) {
                                                    "YouTube" -> Color(0xFFFF0000)
                                                    "Instagram" -> AccentPink
                                                    "Facebook" -> Color(0xFF1877F2)
                                                    else -> AccentCyan
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(acc.platform.take(1), fontWeight = FontWeight.Black, color = Color.White, fontSize = 16.sp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(acc.accountName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                        Text("${acc.platform} • ${acc.handle}", fontSize = 11.sp, color = AccentCyan)
                                        Text(acc.followers, fontSize = 10.sp, color = Color.Gray)
                                    }
                                }

                                Switch(
                                    checked = acc.isConnected,
                                    onCheckedChange = { viewModel.toggleAccountConnection(acc.id) },
                                    colors = SwitchDefaults.colors(checkedThumbColor = AccentCyan, checkedTrackColor = PrimaryPurple)
                                )
                            }
                        }
                    }
                }
            }

            // TAB 2: PUBLISHING HISTORY
            2 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("PUBLISHING & SCHEDULE HISTORY", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AccentGold)
                        Text("${publishingHistory.size} Items", fontSize = 11.sp, color = Color.Gray)
                    }

                    if (publishingHistory.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("No Social Posts Yet", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Text("Publish or schedule your first video in Tab 1!", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        publishingHistory.forEach { item ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(14.dp))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(item.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (item.status == "Published") AccentCyan else AccentGold)
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(item.status.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(item.description, fontSize = 11.sp, color = Color.LightGray, maxLines = 2)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Platform Badges
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        item.platforms.forEach { p ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFF1E1A36))
                                                    .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(p, fontSize = 9.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        Spacer(modifier = Modifier.weight(1f))

                                        if (item.status == "Published") {
                                            Text("👁️ ${item.views} Views • ❤️ ${item.likes}", fontSize = 10.sp, color = AccentGold)
                                        } else {
                                            Text("📅 ${item.scheduledTime}", fontSize = 10.sp, color = AccentGold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Account Dialog
        if (showAddAccountDialog) {
            AlertDialog(
                onDismissRequest = { showAddAccountDialog = false },
                containerColor = DarkSurface,
                title = { Text("Connect Social Media Account", fontWeight = FontWeight.Bold, color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Select Platform:", fontSize = 11.sp, color = Color.Gray)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            availablePlatforms.forEach { plat ->
                                val isSel = newPlatform == plat
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .clickable { newPlatform = plat }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(plat, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = newAccountName,
                            onValueChange = { newAccountName = it },
                            label = { Text("Account / Channel Name", color = Color.Gray, fontSize = 11.sp) },
                            placeholder = { Text("e.g. My Studio Channel", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentCyan
                            )
                        )

                        OutlinedTextField(
                            value = newHandle,
                            onValueChange = { newHandle = it },
                            label = { Text("Handle / Page / Bot Username", color = Color.Gray, fontSize = 11.sp) },
                            placeholder = { Text("e.g. @MyStudioOfficial", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentCyan
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.connectAccount(newPlatform, newAccountName, newHandle)
                            showAddAccountDialog = false
                            Toast.makeText(context, "$newPlatform account connected! 🔗", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Authorize & Connect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddAccountDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}
