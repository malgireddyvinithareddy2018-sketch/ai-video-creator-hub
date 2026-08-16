package com.example.ui.screens.generators

import android.content.Intent
import android.widget.Toast
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

data class PromptItem(
    val id: String,
    val title: String,
    val category: String, // Video Prompts, Image Prompts, Character Prompts, Product Ad Prompts, Story Prompts, Thumbnail Prompts
    val promptText: String,
    val tags: List<String>,
    var isFavorite: Boolean = false
)

@Composable
fun PromptLibraryScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val categories = listOf(
        "All Prompts",
        "Video Prompts",
        "Image Prompts",
        "Character Prompts",
        "Product Ad Prompts",
        "Story Prompts",
        "Thumbnail Prompts",
        "Favorites ❤️"
    )

    var selectedCategory by remember { mutableStateOf("All Prompts") }
    var searchQuery by remember { mutableStateOf("") }

    // Sample High-Quality AI Prompt Library
    val samplePrompts = remember {
        mutableStateListOf(
            PromptItem(
                id = "p1",
                title = "Cinematic Cyberpunk City Rain Video",
                category = "Video Prompts",
                promptText = "Ultra-realistic 8K cinematic video of a futuristic cyberpunk neon-lit city at night during heavy rain, camera slow panning through reflective wet streets, flying futuristic vehicles, moody atmosphere, volumetric fog, photorealistic octane render 60fps.",
                tags = listOf("Cinematic", "Cyberpunk", "8K Video", "Slow Motion")
            ),
            PromptItem(
                id = "p2",
                title = "Hyper-Realistic Photorealistic Portrait",
                category = "Image Prompts",
                promptText = "Studio portrait shot of a young Indian female entrepreneur in modern business attire, natural golden hour lighting, cinematic depth of field, sharp focus on eyes, 85mm lens f/1.4, flawless skin texture, photorealistic 8k.",
                tags = listOf("Portrait", "Studio Light", "85mm", "Realistic")
            ),
            PromptItem(
                id = "p3",
                title = "3D Animated Cartoon Hero Character",
                category = "Character Prompts",
                promptText = "3D Pixar-style animated hero character sheet: front view, side view, and 3/4 view of a young tech wizard boy wearing neon glasses and a futuristic hoodie, expressive eyes, vibrant colors, clean white background, consistent face details.",
                tags = listOf("3D Character", "Pixar Style", "Character Sheet", "Consistent")
            ),
            PromptItem(
                id = "p4",
                title = "Luxury Smartwatch Commercial Ad",
                category = "Product Ad Prompts",
                promptText = "3D motion graphics product ad: sleek matte black luxury smartwatch hovering surrounded by floating holographic water ripples and metallic particles, dramatic studio rim lighting, cinematic slow spin, 4K resolution.",
                tags = listOf("Product Ad", "Luxury", "3D Motion", "Commercial")
            ),
            PromptItem(
                id = "p5",
                title = "Ancient Temple Mystery Sci-Fi Story",
                category = "Story Prompts",
                promptText = "Write a captivating sci-fi mystery story: An archeologist in 2030 uncovers a glowing alien obelisk beneath an ancient Indian temple. Include cinematic scene-by-scene script, visual directions, viral opening hook, and cliffhanger climax.",
                tags = listOf("Sci-Fi", "Mystery", "Script", "Story")
            ),
            PromptItem(
                id = "p6",
                title = "High CTR YouTube Tech Thumbnail",
                category = "Thumbnail Prompts",
                promptText = "Viral YouTube thumbnail image prompt: Shocked young creator face on the left pointing with hand, bold neon yellow typography on right saying 'DON'T BUY THIS!', glowing futuristic smartphone on desk with red lightning effects, high contrast, 16:9 aspect ratio.",
                tags = listOf("YouTube Thumbnail", "High CTR", "Clickbait", "16:9")
            ),
            PromptItem(
                id = "p7",
                title = "Drone Hyperlapse Mountain Sunrise",
                category = "Video Prompts",
                promptText = "FPV drone camera hyperlapse sweeping over majestic snow-capped Himalayan mountains during sunrise, golden sunlight breaking through clouds, crystal clear alpine lake reflections, 4k cinematic footage 60fps.",
                tags = listOf("Drone", "FPV", "Landscape", "4K")
            ),
            PromptItem(
                id = "p8",
                title = "Anime Fantasy Cyber Warrior",
                category = "Character Prompts",
                promptText = "Detailed anime character design: female cyber-samurai with glowing blue katana, wearing sleek carbon fiber armor, dynamic combat stance, dramatic neon background, high resolution anime masterpiece.",
                tags = listOf("Anime", "Cyber Samurai", "Fantasy", "Detailed")
            ),
            PromptItem(
                id = "p9",
                title = "Sneakers Shoe Explosion Ad Prompt",
                category = "Product Ad Prompts",
                promptText = "Exploding product breakdown ad: vibrant athletic sneaker disassembled into flying soles, cushion mesh, and laces suspended in mid-air, dynamic speedlines, high energy e-commerce ad render.",
                tags = listOf("E-Commerce", "Sneakers", "3D Explosion", "High Energy")
            ),
            PromptItem(
                id = "p10",
                title = "Motivational Millionaire Daily Routine",
                category = "Story Prompts",
                promptText = "Generate a high-retention 60-second Reels/Shorts script about 5 daily habits of self-made millionaires. Include 1-second viral hook, fast B-roll visual cues, punchy captions, and strong call to action.",
                tags = listOf("Reels", "Shorts", "Motivation", "Finance")
            ),
            PromptItem(
                id = "p11",
                title = "Glowing Neon AI Brain Concept",
                category = "Image Prompts",
                promptText = "Macro conceptual photography: glowing glass human brain with intricate fiber optic circuits pulsating with glowing cyan and gold light, dark void background, volumetric ray tracing, octane render 8k.",
                tags = listOf("AI Concept", "Neon", "Octane Render", "Macro")
            ),
            PromptItem(
                id = "p12",
                title = "Before vs After Finance Thumbnail",
                category = "Thumbnail Prompts",
                promptText = "Split screen YouTube thumbnail: left side shows sad person holding $0 bank statement in red glow, right side shows happy rich person with green $100,000 stack and upward stock graph, bold text '$0 to $100K'.",
                tags = listOf("Finance", "Before After", "Split Screen", "CTR")
            )
        )
    }

    // Filter Logic
    val filteredPrompts = remember(selectedCategory, searchQuery, samplePrompts.size) {
        samplePrompts.filter { item ->
            val matchesCat = when (selectedCategory) {
                "All Prompts" -> true
                "Favorites ❤️" -> item.isFavorite
                else -> item.category == selectedCategory
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                item.title.contains(searchQuery, ignoreCase = true) ||
                        item.promptText.contains(searchQuery, ignoreCase = true) ||
                        item.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }
            matchesCat && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .testTag("prompt_library_screen")
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
                    Text("AI PROMPT LIBRARY", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentCyan)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("STUDIO HUB", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Search, Copy & Save High-Converting Video & Image Prompts", fontSize = 11.sp, color = AccentGold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search prompts (e.g. Cyberpunk, 3D, Product Ad, Thumbnail...)", color = Color.Gray, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = AccentCyan) },
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
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = Color(0xFF2E2954)
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Horizontal Row
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
                        .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        cat,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Results Count Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredPrompts.size} Prompts Available",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AccentCyan
            )

            if (selectedCategory == "Favorites ❤️") {
                Text("Saved Favorites", fontSize = 11.sp, color = AccentPink)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Prompts List
        if (filteredPrompts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FolderOpen, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No prompts found matching your criteria", color = Color.Gray, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPrompts, key = { it.id }) { prompt ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFF2E2954), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Title & Category & Favorite Toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(prompt.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(PrimaryPurple.copy(alpha = 0.3f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(prompt.category, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        val idx = samplePrompts.indexOfFirst { it.id == prompt.id }
                                        if (idx != -1) {
                                            val updated = samplePrompts[idx].copy(isFavorite = !samplePrompts[idx].isFavorite)
                                            samplePrompts[idx] = updated
                                            Toast.makeText(
                                                context,
                                                if (updated.isFavorite) "Added to Favorites ❤️" else "Removed from Favorites",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                ) {
                                    Icon(
                                        if (prompt.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (prompt.isFavorite) AccentPink else Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Prompt Body Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1E1A36))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    prompt.promptText,
                                    fontSize = 11.sp,
                                    color = Color.LightGray,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Tags & Action Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    items(prompt.tags) { tag ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFF2A254B))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("#$tag", fontSize = 9.sp, color = Color.Gray)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    IconButton(
                                        onClick = {
                                            val shareIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, "✨ AI Prompt (${prompt.title}):\n\n${prompt.promptText}")
                                                type = "text/plain"
                                            }
                                            context.startActivity(Intent.createChooser(shareIntent, "Share Prompt"))
                                        },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF2A254B))
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }

                                    Button(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(prompt.promptText))
                                            Toast.makeText(context, "Prompt Copied! 📋", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Copy", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
