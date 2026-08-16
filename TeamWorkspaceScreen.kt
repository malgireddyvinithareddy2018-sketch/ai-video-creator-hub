package com.example.ui.screens.generators

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.models.SharedProject
import com.example.data.models.TeamActivity
import com.example.data.models.TeamChatMessage
import com.example.data.models.TeamMember
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun TeamWorkspaceScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var activeTab by remember { mutableStateOf(0) } // 0: Members & Roles, 1: Shared Projects, 2: Team Chat, 3: Activity History

    // Dialog States
    var showCreateTeamModal by remember { mutableStateOf(false) }
    var newTeamName by remember { mutableStateOf("") }
    var newTeamDesc by remember { mutableStateOf("") }

    var showInviteMemberModal by remember { mutableStateOf(false) }
    var inviteEmail by remember { mutableStateOf("") }
    var inviteRole by remember { mutableStateOf("Editor") } // Admin, Editor, Viewer

    var showAddProjectModal by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var newProjectMediaType by remember { mutableStateOf("AI Video") }

    // Chat Message State
    var chatInputText by remember { mutableStateOf("") }

    val teamWorkspace by viewModel.activeTeamWorkspace.collectAsState()

    val rolesList = listOf("Admin", "Editor", "Viewer")
    val projectTypesList = listOf("AI Video", "Master Character", "Reel / Short", "Product Ad", "AI Voice & Script")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("team_workspace_screen")
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
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("TEAM WORKSPACE", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("PRO TEAM", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Collaborate, Assign Roles & Share Studio Projects", fontSize = 11.sp, color = AccentCyan)
            }

            IconButton(
                onClick = { showCreateTeamModal = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryPurple)
            ) {
                Icon(Icons.Default.GroupAdd, contentDescription = "New Team", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Workspace Card & Invite Code Header
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AccentGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(teamWorkspace.name, fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                        Text(teamWorkspace.description, fontSize = 11.sp, color = Color.LightGray)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E1A36))
                            .border(1.dp, AccentCyan, RoundedCornerShape(8.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Team Code", teamWorkspace.inviteCode)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Team invite code copied! 📋", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(teamWorkspace.inviteCode, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("👥 ${teamWorkspace.members.size} Members • 📁 ${teamWorkspace.sharedProjects.size} Shared Projects", fontSize = 11.sp, color = AccentGold, fontWeight = FontWeight.Bold)

                    Button(
                        onClick = { showInviteMemberModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Invite Member", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs (4 Tabs)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("1. Members", "2. Projects", "3. Chat", "4. Activity").forEachIndexed { idx, title ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(10.dp))
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
            // TAB 0: MEMBERS & ROLE MANAGEMENT
            0 -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TEAM MEMBERS & ROLES", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentGold)
                        Text("${teamWorkspace.members.size} Active Members", fontSize = 11.sp, color = Color.Gray)
                    }

                    teamWorkspace.members.forEach { member ->
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
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when (member.role) {
                                                        "Admin" -> AccentGold
                                                        "Editor" -> PrimaryPurple
                                                        else -> Color(0xFF2E2954)
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(member.avatarInitials, fontWeight = FontWeight.Black, color = Color.White, fontSize = 14.sp)
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(member.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                                if (member.isOnline) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .size(8.dp)
                                                            .clip(CircleShape)
                                                            .background(Color.Green)
                                                    )
                                                }
                                            }
                                            Text(member.email, fontSize = 11.sp, color = AccentCyan)
                                        }
                                    }

                                    // Role Badge
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                when (member.role) {
                                                    "Admin" -> AccentGold
                                                    "Editor" -> AccentCyan
                                                    else -> Color.Gray
                                                }
                                            )
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(member.role.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Role selector & remove actions
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Change Role:", fontSize = 10.sp, color = Color.Gray)

                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        rolesList.forEach { role ->
                                            val isCurrent = member.role == role
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isCurrent) PrimaryPurple else Color(0xFF1E1A36))
                                                    .border(1.dp, if (isCurrent) AccentGold else Color.Transparent, RoundedCornerShape(6.dp))
                                                    .clickable { viewModel.updateMemberRole(member.id, role) }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(role, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isCurrent) Color.White else Color.LightGray)
                                            }
                                        }
                                    }

                                    if (member.role != "Admin") {
                                        IconButton(
                                            onClick = {
                                                viewModel.removeTeamMember(member.id)
                                                Toast.makeText(context, "${member.name} removed from team.", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.PersonRemove, contentDescription = "Remove", tint = Color.Red, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 1: SHARED PROJECTS
            1 -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SHARED TEAM PROJECTS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentGold)
                        Button(
                            onClick = { showAddProjectModal = true },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share Project", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (teamWorkspace.sharedProjects.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.FolderOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No Shared Projects Yet", fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Click 'Share Project' to collaborate with your team!", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        teamWorkspace.sharedProjects.forEach { proj ->
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
                                            Icon(
                                                when (proj.mediaType) {
                                                    "AI Video" -> Icons.Default.Movie
                                                    "Master Character" -> Icons.Default.Face
                                                    "Reel / Short" -> Icons.Default.VideoLibrary
                                                    else -> Icons.Default.Folder
                                                },
                                                contentDescription = null,
                                                tint = AccentCyan,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(proj.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (proj.status.contains("Approved")) AccentCyan else AccentGold)
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(proj.status.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Category: ${proj.mediaType} • Shared by: ${proj.creatorName} • ${proj.lastModified}", fontSize = 11.sp, color = Color.LightGray)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                Toast.makeText(context, "Opening shared project '${proj.name}' in AI Studio...", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Open & Edit", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                Toast.makeText(context, "Export link generated for team review!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(AccentCyan, AccentGold)))
                                        ) {
                                            Text("Review Link", fontSize = 10.sp, color = AccentCyan)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 2: TEAM CHAT & AI CO-PILOT
            2 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Chat, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("TEAM CHAT & AI ASSISTANT", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            }
                            Text("Type @ai for AI co-pilot", fontSize = 10.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Chat Messages List
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            teamWorkspace.chatMessages.forEach { msg ->
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = if (msg.isSelf) Alignment.End else Alignment.Start
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(msg.senderName, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (msg.senderName.contains("AI")) AccentGold else AccentCyan)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("(${msg.senderRole}) • ${msg.timestamp}", fontSize = 9.sp, color = Color.Gray)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                when {
                                                    msg.isSelf -> PrimaryPurple
                                                    msg.senderName.contains("AI") -> Color(0xFF2E2954)
                                                    else -> Color(0xFF1E1A36)
                                                }
                                            )
                                            .border(1.dp, if (msg.senderName.contains("AI")) AccentGold else Color.Transparent, RoundedCornerShape(12.dp))
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(msg.message, fontSize = 12.sp, color = Color.White)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Chat Input Bar
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = chatInputText,
                                onValueChange = { chatInputText = it },
                                placeholder = { Text("Message team or @ai for prompts...", color = Color.Gray, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = AccentCyan,
                                    unfocusedBorderColor = Color(0xFF2E2954)
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    if (chatInputText.isNotBlank()) {
                                        viewModel.sendTeamChatMessage(chatInputText)
                                        chatInputText = ""
                                    }
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PrimaryPurple)
                                    .size(48.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                            }
                        }
                    }
                }
            }

            // TAB 3: ACTIVITY HISTORY
            3 -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TEAM AUDIT & ACTIVITY HISTORY", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentGold)
                        Text("${teamWorkspace.activities.size} Logged Actions", fontSize = 11.sp, color = Color.Gray)
                    }

                    teamWorkspace.activities.forEach { act ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.History, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${act.memberName} ${act.action} '${act.target}'",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(act.timestamp, fontSize = 9.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Create Team Modal
        if (showCreateTeamModal) {
            AlertDialog(
                onDismissRequest = { showCreateTeamModal = false },
                containerColor = DarkSurface,
                title = { Text("Create New Team Workspace", fontWeight = FontWeight.Bold, color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newTeamName,
                            onValueChange = { newTeamName = it },
                            label = { Text("Team Name", color = Color.Gray, fontSize = 11.sp) },
                            placeholder = { Text("e.g. Viral Shorts Agency", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentGold)
                        )

                        OutlinedTextField(
                            value = newTeamDesc,
                            onValueChange = { newTeamDesc = it },
                            label = { Text("Description", color = Color.Gray, fontSize = 11.sp) },
                            placeholder = { Text("e.g. Collaborative space for YouTube & Instagram creators", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentCyan)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.createTeamWorkspace(newTeamName, newTeamDesc) { success, msg ->
                                showCreateTeamModal = false
                                newTeamName = ""
                                newTeamDesc = ""
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Create Workspace", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateTeamModal = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // Invite Member Modal
        if (showInviteMemberModal) {
            AlertDialog(
                onDismissRequest = { showInviteMemberModal = false },
                containerColor = DarkSurface,
                title = { Text("Invite Member to Team", fontWeight = FontWeight.Bold, color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = inviteEmail,
                            onValueChange = { inviteEmail = it },
                            label = { Text("Member Email", color = Color.Gray, fontSize = 11.sp) },
                            placeholder = { Text("creator@aistudio.io", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentCyan)
                        )

                        Text("Assign Role:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            rolesList.forEach { role ->
                                val isSel = inviteRole == role
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                        .border(1.dp, if (isSel) AccentGold else Color.Transparent, RoundedCornerShape(8.dp))
                                        .clickable { inviteRole = role }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(role, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.inviteTeamMember(inviteEmail, inviteRole) { success, msg ->
                                if (success) {
                                    showInviteMemberModal = false
                                    inviteEmail = ""
                                }
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Send Invite", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showInviteMemberModal = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        // Share Project Modal
        if (showAddProjectModal) {
            AlertDialog(
                onDismissRequest = { showAddProjectModal = false },
                containerColor = DarkSurface,
                title = { Text("Share Project with Team", fontWeight = FontWeight.Bold, color = Color.White) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = newProjectName,
                            onValueChange = { newProjectName = it },
                            label = { Text("Project Name", color = Color.Gray, fontSize = 11.sp) },
                            placeholder = { Text("e.g. Neo-Tokyo AI Video Trailer", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = AccentGold)
                        )

                        Text("Media Category:", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            projectTypesList.chunked(2).forEach { row ->
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    row.forEach { type ->
                                        val isSel = newProjectMediaType == type
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                                .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                                .clickable { newProjectMediaType = type }
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(type, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.White else Color.LightGray, maxLines = 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addSharedTeamProject(newProjectName, newProjectMediaType) { success, msg ->
                                if (success) {
                                    showAddProjectModal = false
                                    newProjectName = ""
                                }
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Share with Team", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddProjectModal = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}
