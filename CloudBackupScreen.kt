package com.example.ui.screens.generators

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.models.BackupRecord
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CloudBackupScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var activeTab by remember { mutableStateOf(0) } // 0: Backup & Sync, 1: Restore Snapshots, 2: Auto Backup Settings

    var isBackingUpNow by remember { mutableStateOf(false) }
    var showCustomBackupModal by remember { mutableStateOf(false) }
    var customBackupName by remember { mutableStateOf("") }

    val backupSettings by viewModel.backupSettings.collectAsState()
    val backupHistory by viewModel.backupHistory.collectAsState()
    val user by viewModel.user.collectAsState()

    val usedGb = backupSettings.storageUsedMb / 1024.0
    val totalGb = backupSettings.storageTotalMb / 1024.0
    val storagePercent = (backupSettings.storageUsedMb / backupSettings.storageTotalMb).toFloat().coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("cloud_backup_screen")
    ) {
        // Top Header
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
                    Text("CLOUD BACKUP & SYNC", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentCyan)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("FIREBASE STORAGE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Auto Backup, Restore Projects & Cross-Device Sync", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Firebase Storage Status & Meter Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AccentCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AccentCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CloudSync, contentDescription = null, tint = AccentCyan)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Firebase Storage Connected ☁️", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("gs://ai-studio-pro-backup.appspot.com", fontSize = 10.sp, color = AccentCyan)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (backupSettings.autoBackupEnabled) AccentGold else Color.Gray)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (backupSettings.autoBackupEnabled) "AUTO SYNC ON" else "AUTO SYNC PAUSED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Storage usage progress bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Cloud Storage Used:", fontSize = 11.sp, color = Color.Gray)
                    Text("${String.format("%.2f", usedGb)} GB / ${String.format("%.1f", totalGb)} GB Free", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { storagePercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = AccentCyan,
                    trackColor = Color(0xFF1E1A36)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Last Sync: ${backupSettings.lastSyncTime} • All devices updated", fontSize = 10.sp, color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1. Instant Backup", "2. Restore Projects (${backupHistory.size})", "3. Auto Backup Rules").forEachIndexed { idx, title ->
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
            // TAB 0: INSTANT BACKUP & CROSS-DEVICE SYNC
            0 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("1. BACKUP CONTENT INCLUDED IN SYNC", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(10.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Image, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Backup AI Images & Text-to-Image", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                                Checkbox(
                                    checked = backupSettings.backupImages,
                                    onCheckedChange = { viewModel.updateBackupSettings(backupImages = it) },
                                    colors = CheckboxDefaults.colors(checkedColor = AccentCyan)
                                )
                            }

                            HorizontalDivider(color = Color(0xFF2E2954))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Movie, contentDescription = null, tint = AccentPink, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Backup AI Videos & Kling/Runway Generations", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                                Checkbox(
                                    checked = backupSettings.backupVideos,
                                    onCheckedChange = { viewModel.updateBackupSettings(backupVideos = it) },
                                    colors = CheckboxDefaults.colors(checkedColor = AccentPink)
                                )
                            }

                            HorizontalDivider(color = Color(0xFF2E2954))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Backup Saved AI Prompts & Master Characters", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                                Checkbox(
                                    checked = backupSettings.backupPrompts,
                                    onCheckedChange = { viewModel.updateBackupSettings(backupPrompts = it) },
                                    colors = CheckboxDefaults.colors(checkedColor = AccentGold)
                                )
                            }

                            HorizontalDivider(color = Color(0xFF2E2954))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Backup App Settings & Voice Clones", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                }
                                Checkbox(
                                    checked = backupSettings.backupSettings,
                                    onCheckedChange = { viewModel.updateBackupSettings(backupSettingsToggle = it) },
                                    colors = CheckboxDefaults.colors(checkedColor = PrimaryPurple)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Devices Synced Indicator
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1A36)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("CONNECTED DEVICES SYNCED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("📱 Current Android Phone • 📱 Tablet Pro • 💻 Web Studio Dashboard", fontSize = 10.sp, color = Color.LightGray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Instant Backup Action Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            isBackingUpNow = true
                            viewModel.triggerCloudBackup { success, msg ->
                                isBackingUpNow = false
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                activeTab = 1 // Switch to Restore
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        enabled = !isBackingUpNow
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.horizontalGradient(listOf(PrimaryPurple, AccentCyan))),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isBackingUpNow) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("SYNCING TO FIREBASE...", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                } else {
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("INSTANT BACKUP NOW", fontWeight = FontWeight.Black, fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { showCustomBackupModal = true },
                        modifier = Modifier.height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(AccentGold, AccentPink)))
                    ) {
                        Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = AccentGold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Custom Snapshot", fontSize = 10.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // TAB 1: RESTORE PROJECTS & SNAPSHOTS
            1 -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("RESTORE FROM CLOUD SNAPSHOTS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AccentGold)
                        Text("${backupHistory.size} Backups Available", fontSize = 11.sp, color = Color.Gray)
                    }

                    if (backupHistory.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.CloudOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("No Cloud Snapshots Saved", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                Text("Create your first cloud backup in Tab 1!", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        backupHistory.forEach { item ->
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
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CloudDone, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(item.backupName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(AccentGold)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("${item.sizeMb} MB", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text("🕒 ${item.timestamp} • Device: ${item.deviceName}", fontSize = 11.sp, color = AccentCyan)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Contains: 🖼️ ${item.imagesCount} Images • 🎬 ${item.videosCount} Videos • 📝 ${item.promptsCount} Prompts • ⚙️ Settings", fontSize = 10.sp, color = Color.LightGray)

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                viewModel.restoreFromBackup(item) { success, msg ->
                                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.CloudDownload, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Restore to Device", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                viewModel.deleteBackupRecord(item)
                                                Toast.makeText(context, "Backup snapshot deleted.", Toast.LENGTH_SHORT).show()
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

            // TAB 2: AUTO BACKUP & SYNC RULES
            2 -> {
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text("AUTO BACKUP TO FIREBASE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Automatically sync new videos, images & prompts to cloud", fontSize = 10.sp, color = Color.Gray)
                            }
                            Switch(
                                checked = backupSettings.autoBackupEnabled,
                                onCheckedChange = { viewModel.updateBackupSettings(autoBackupEnabled = it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = AccentCyan, checkedTrackColor = PrimaryPurple)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Auto Backup Frequency:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Realtime", "Daily", "Weekly").forEach { freq ->
                                val isSel = backupSettings.backupFrequency == freq
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                        .clickable { viewModel.updateBackupSettings(backupFrequency = freq) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(freq, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Wi-Fi Only Sync", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Save mobile data by syncing only on Wi-Fi connection", fontSize = 10.sp, color = Color.Gray)
                            }
                            Switch(
                                checked = backupSettings.wifiOnly,
                                onCheckedChange = { viewModel.updateBackupSettings(wifiOnly = it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = PrimaryPurple)
                            )
                        }
                    }
                }
            }
        }

        // Custom Snapshot Modal
        if (showCustomBackupModal) {
            AlertDialog(
                onDismissRequest = { showCustomBackupModal = false },
                containerColor = DarkSurface,
                title = { Text("Name Custom Cloud Snapshot", fontWeight = FontWeight.Bold, color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Give a name for this Firebase Storage backup version:", fontSize = 11.sp, color = Color.Gray)
                        OutlinedTextField(
                            value = customBackupName,
                            onValueChange = { customBackupName = it },
                            placeholder = { Text("e.g. Master Character Project V2 Snapshot...", color = Color.Gray, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentGold)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.triggerCloudBackup(customBackupName) { success, msg ->
                                showCustomBackupModal = false
                                customBackupName = ""
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                activeTab = 1
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Create Backup", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCustomBackupModal = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}
