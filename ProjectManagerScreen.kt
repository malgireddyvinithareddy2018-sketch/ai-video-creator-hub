package com.example.ui.screens.generators

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.viewmodel.MainViewModel

data class ProjectItem(
    val id: String,
    val name: String,
    val category: String, // e.g. "Video Project", "Reel / Short", "Product Ad", "Thumbnail", "Podcast", "Story Script"
    val isDraft: Boolean, // true = Draft, false = Saved
    val aspectRatio: String = "16:9",
    val prompt: String,
    val resultUrl: String = "",
    val lastModified: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList()
)

@Composable
fun ProjectManagerScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val categories = listOf("All Projects", "Recent", "Saved Projects", "Draft Projects", "Video Projects", "Product Ads", "Thumbnails")
    var selectedCategory by remember { mutableStateOf("All Projects") }
    var searchQuery by remember { mutableStateOf("") }

    // Dialog state for creating/saving a new project
    var showCreateDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var newProjectCategory by remember { mutableStateOf("Video Project") }
    var newProjectPrompt by remember { mutableStateOf("") }
    var newProjectIsDraft by remember { mutableStateOf(false) }

    // Project delete confirmation dialog state
    var projectToDelete by remember { mutableStateOf<ProjectItem?>(null) }

    // Initial sample projects database
    val projectsList = remember {
        mutableStateListOf(
            ProjectItem(
                id = "proj_1",
                name = "Cyberpunk City AI Reel",
                category = "Reel / Short",
                isDraft = false,
                aspectRatio = "9:16",
                prompt = "Futuristic rainy neon cyberpunk street with flying sports car, cinematic lighting 4k",
                resultUrl = "https://aivideocreator.hub/render/cyberpunk_reel_01.mp4",
                lastModified = System.currentTimeMillis() - (1000 * 60 * 30), // 30 mins ago
                tags = listOf("Reel", "Cyberpunk", "Viral")
            ),
            ProjectItem(
                id = "proj_2",
                name = "Luxury Smartwatch Ad Campaign",
                category = "Product Ad",
                isDraft = true, // Draft
                aspectRatio = "1:1",
                prompt = "3D floating matte black smartwatch with liquid water splash ripple effects and metallic gold particles",
                resultUrl = "",
                lastModified = System.currentTimeMillis() - (1000 * 60 * 60 * 2), // 2 hours ago
                tags = listOf("Product", "Ad", "Draft")
            ),
            ProjectItem(
                id = "proj_3",
                name = "Ancient Indian Mystery Sci-Fi Story",
                category = "Video Project",
                isDraft = false,
                aspectRatio = "16:9",
                prompt = "Archeologist finding glowing alien obelisk inside ancient temple, 8K hyper-realistic cinematic shot",
                resultUrl = "https://aivideocreator.hub/render/temple_scifi_03.mp4",
                lastModified = System.currentTimeMillis() - (1000 * 60 * 60 * 24), // 1 day ago
                tags = listOf("Sci-Fi", "Story", "16:9")
            ),
            ProjectItem(
                id = "proj_4",
                name = "YouTube Finance Clickbait Thumbnail",
                category = "Thumbnail",
                isDraft = false,
                aspectRatio = "16:9",
                prompt = "Shocked creator pointing at $100K green stock graph with bold yellow text 'DON'T BUY THIS!'",
                resultUrl = "https://aivideocreator.hub/render/finance_thumbnail.png",
                lastModified = System.currentTimeMillis() - (1000 * 60 * 60 * 48), // 2 days ago
                tags = listOf("Thumbnail", "YouTube", "CTR")
            ),
            ProjectItem(
                id = "proj_5",
                name = "Telugu Tech News AI Podcast Draft",
                category = "Podcast",
                isDraft = true, // Draft
                aspectRatio = "16:9",
                prompt = "Full episode audio script discussing Gemini 2.0 AI model capabilities in Telugu and English blend",
                resultUrl = "",
                lastModified = System.currentTimeMillis() - (1000 * 60 * 60 * 72), // 3 days ago
                tags = listOf("Podcast", "Telugu", "Draft")
            ),
            ProjectItem(
                id = "proj_6",
                name = "Sneaker Brand Product Showcase",
                category = "Product Ad",
                isDraft = false,
                aspectRatio = "9:16",
                prompt = "High energy explode view of running sneaker soles assembling in mid-air with lightning speedlines",
                resultUrl = "https://aivideocreator.hub/render/sneaker_ad.mp4",
                lastModified = System.currentTimeMillis() - (1000 * 60 * 60 * 96), // 4 days ago
                tags = listOf("E-Commerce", "Shorts", "3D")
            )
        )
    }

    // Filtered project list calculation
    val filteredProjects = remember(selectedCategory, searchQuery, projectsList.size) {
        projectsList.filter { project ->
            val matchesCat = when (selectedCategory) {
                "All Projects" -> true
                "Recent" -> true // Handled by sorting recent
                "Saved Projects" -> !project.isDraft
                "Draft Projects" -> project.isDraft
                "Video Projects" -> project.category.contains("Video", ignoreCase = true) || project.category.contains("Reel", ignoreCase = true)
                "Product Ads" -> project.category.contains("Product", ignoreCase = true)
                "Thumbnails" -> project.category.contains("Thumbnail", ignoreCase = true)
                else -> project.category == selectedCategory
            }

            val matchesSearch = if (searchQuery.isBlank()) true else {
                project.name.contains(searchQuery, ignoreCase = true) ||
                        project.prompt.contains(searchQuery, ignoreCase = true) ||
                        project.category.contains(searchQuery, ignoreCase = true) ||
                        project.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }

            matchesCat && matchesSearch
        }.let { list ->
            if (selectedCategory == "Recent") {
                list.sortedByDescending { it.lastModified }
            } else {
                list.sortedByDescending { it.lastModified }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .testTag("project_manager_screen")
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
                    Text("PROJECT MANAGER", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("STUDIO HUB", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Saved Projects, Drafts, Search, Duplicate & Organize", fontSize = 11.sp, color = AccentCyan)
            }

            // New Project Button
            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "New", tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search projects by name, prompt, category or tags...", color = Color.Gray, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = AccentGold) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface,
                focusedBorderColor = AccentGold,
                unfocusedBorderColor = Color(0xFF2E2954)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSel = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (cat == "Draft Projects") {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(AccentPink)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        } else if (cat == "Saved Projects") {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(AccentCyan)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            cat,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) Color.White else Color.LightGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Projects Count Summary Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredProjects.size} Projects (${projectsList.count { !it.isDraft }} Saved | ${projectsList.count { it.isDraft }} Drafts)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGold
            )
            Text("Sort: Recent First", fontSize = 10.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Projects List
        if (filteredProjects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FolderSpecial, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No projects found matching your criteria", color = Color.Gray, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProjects, key = { it.id }) { project ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (project.isDraft) AccentPink.copy(alpha = 0.4f) else Color(0xFF2E2954),
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Header Row: Title & Status Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(project.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (project.isDraft) AccentPink else AccentCyan)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                if (project.isDraft) "DRAFT" else "SAVED",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text("${project.category} • Ratio: ${project.aspectRatio}", fontSize = 10.sp, color = Color.Gray)
                                }

                                // Quick Duplicate & Delete Actions
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    // Duplicate Project
                                    IconButton(
                                        onClick = {
                                            val duplicateItem = project.copy(
                                                id = "proj_${System.currentTimeMillis()}",
                                                name = "${project.name} (Copy)",
                                                lastModified = System.currentTimeMillis()
                                            )
                                            projectsList.add(0, duplicateItem)
                                            Toast.makeText(context, "Project duplicated! 📋", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = AccentGold, modifier = Modifier.size(18.dp))
                                    }

                                    // Delete Project
                                    IconButton(
                                        onClick = { projectToDelete = project },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Prompt Details Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1E1A36))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    project.prompt,
                                    fontSize = 11.sp,
                                    color = Color.LightGray,
                                    lineHeight = 16.sp,
                                    maxLines = 3
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Bottom Row: Status Toggle / Export / Open Action
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Toggle Draft <-> Saved
                                TextButton(
                                    onClick = {
                                        val idx = projectsList.indexOfFirst { it.id == project.id }
                                        if (idx != -1) {
                                            val updated = projectsList[idx].copy(
                                                isDraft = !projectsList[idx].isDraft,
                                                lastModified = System.currentTimeMillis()
                                            )
                                            projectsList[idx] = updated
                                            Toast.makeText(
                                                context,
                                                if (updated.isDraft) "Moved to Drafts 📝" else "Saved Project Published! 💾",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(
                                        if (project.isDraft) Icons.Default.BookmarkBorder else Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = AccentGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        if (project.isDraft) "Save as Final" else "Move to Draft",
                                        fontSize = 10.sp,
                                        color = AccentGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (project.resultUrl.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(project.resultUrl))
                                                val shareIntent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(Intent.EXTRA_TEXT, "🎬 AI Project (${project.name}): ${project.resultUrl}")
                                                    type = "text/plain"
                                                }
                                                context.startActivity(Intent.createChooser(shareIntent, "Share Project Render"))
                                            },
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF2A254B))
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(project.prompt))
                                            Toast.makeText(context, "Project prompt copied to clipboard! 🚀", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Copy Prompt", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for creating a new project or saving a draft
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("CREATE NEW PROJECT", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        label = { Text("Project Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newProjectPrompt,
                        onValueChange = { newProjectPrompt = it },
                        label = { Text("Prompt / Notes / Script") },
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Save as Draft?", fontSize = 12.sp, color = Color.White)
                        Switch(
                            checked = newProjectIsDraft,
                            onCheckedChange = { newProjectIsDraft = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = AccentGold, checkedTrackColor = PrimaryPurple)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newProjectName.isBlank()) {
                            Toast.makeText(context, "Please enter a project name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val newProject = ProjectItem(
                            id = "proj_${System.currentTimeMillis()}",
                            name = newProjectName.trim(),
                            category = newProjectCategory,
                            isDraft = newProjectIsDraft,
                            prompt = newProjectPrompt.ifBlank { "Custom AI project prompt" },
                            lastModified = System.currentTimeMillis(),
                            tags = listOf(if (newProjectIsDraft) "Draft" else "Saved")
                        )
                        projectsList.add(0, newProject)
                        showCreateDialog = false
                        newProjectName = ""
                        newProjectPrompt = ""
                        Toast.makeText(context, "Project Created Successfully! 🎉", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGold)
                ) {
                    Text("SAVE PROJECT", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 11.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("CANCEL", color = Color.Gray, fontSize = 11.sp)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Delete Confirmation Dialog
    projectToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = { Text("DELETE PROJECT?", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 14.sp) },
            text = { Text("Are you sure you want to delete '${item.name}'? This action cannot be undone.", color = Color.LightGray, fontSize = 12.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        projectsList.removeIf { it.id == item.id }
                        projectToDelete = null
                        Toast.makeText(context, "Project deleted 🗑️", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("DELETE", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 11.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) {
                    Text("CANCEL", color = Color.Gray, fontSize = 11.sp)
                }
            },
            containerColor = DarkSurface
        )
    }
}
