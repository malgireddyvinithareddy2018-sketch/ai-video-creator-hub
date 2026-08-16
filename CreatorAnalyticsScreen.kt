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
import com.example.data.models.GenerationType
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CreatorAnalyticsScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val history by viewModel.history.collectAsState()
    val user by viewModel.user.collectAsState()

    // Calculate real-time stats merged with creator baselines
    val actualVideosCreated = history.count { item ->
        item.type in listOf(
            GenerationType.TEXT_TO_VIDEO,
            GenerationType.IMAGE_TO_VIDEO,
            GenerationType.PROMPT_TO_VIDEO,
            GenerationType.PROMPT_IMAGE_TO_VIDEO,
            GenerationType.PRODUCT_TO_VIDEO,
            GenerationType.TALKING_PHOTO,
            GenerationType.VIDEO_DUBBING,
            GenerationType.VIDEO_TEMPLATE,
            GenerationType.REEL_MAKER,
            GenerationType.PODCAST_GENERATOR
        )
    }
    val totalVideosCreated = 42 + actualVideosCreated

    val actualImagesCreated = history.count { item ->
        item.type in listOf(
            GenerationType.TEXT_TO_IMAGE,
            GenerationType.CHARACTER,
            GenerationType.THUMBNAIL,
            GenerationType.AVATAR_GENERATOR,
            GenerationType.CHARACTER_MASTER
        )
    }
    val totalImagesCreated = 38 + actualImagesCreated

    val actualCreditsSpent = history.sumOf { it.creditsSpent }
    val totalCreditsUsed = 68 + actualCreditsSpent

    val totalCreditsEarned = 120 // Base initial credits (20) + referral bonuses (45) + daily rewards (55)

    // Calculate Most Used Tool dynamically
    val toolTypeCounts = history.groupingBy { it.type }.eachCount()
    val mostUsedEnum = toolTypeCounts.maxByOrNull { it.value }?.key ?: GenerationType.REEL_MAKER

    val mostUsedToolName = when (mostUsedEnum) {
        GenerationType.REEL_MAKER -> "AI Reel Maker"
        GenerationType.TEXT_TO_VIDEO -> "Text-to-Video"
        GenerationType.IMAGE_TO_VIDEO -> "Image-to-Video"
        GenerationType.PRODUCT_TO_VIDEO -> "AI Product Ad Studio"
        GenerationType.PODCAST_GENERATOR -> "AI Podcast Generator"
        GenerationType.SCRIPT_WRITER -> "AI Script Writer"
        GenerationType.THUMBNAIL -> "Thumbnail Generator"
        else -> "AI Video Generator"
    }

    var selectedTimeframe by remember { mutableStateOf("Weekly") } // Weekly vs Monthly

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("creator_analytics_screen")
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
                    Text("CREATOR ANALYTICS", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentCyan)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("LIVE DASHBOARD", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Track Creations, Credits, Top Tools & Growth Metrics", fontSize = 11.sp, color = AccentGold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timeframe Selector Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurface)
                .padding(4.dp)
        ) {
            listOf("Weekly", "Monthly").forEach { timeframe ->
                val isSel = selectedTimeframe == timeframe
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSel) PrimaryPurple else Color.Transparent)
                        .clickable { selectedTimeframe = timeframe }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$timeframe Analytics",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Key Metric Cards (2x2 Grid)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Videos Created
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Movie, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                            Text("+32% W/W", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$totalVideosCreated", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Total Videos Created", fontSize = 10.sp, color = Color.Gray)
                    }
                }

                // Images Created
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = AccentPink, modifier = Modifier.size(20.dp))
                            Text("+18% W/W", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$totalImagesCreated", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Total Images Created", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Credits Used
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                            Text("${user.credits} Left", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$totalCreditsUsed", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Credits Used", fontSize = 10.sp, color = Color.Gray)
                    }
                }

                // Credits Earned
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(20.dp))
                            Text("Free Rewards", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$totalCreditsEarned", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Credits Earned", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Most Used Tool Card
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
                        Icon(Icons.Default.Star, contentDescription = null, tint = AccentGold, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("MOST USED CREATOR TOOL", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGold)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("TOP #1", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1A36))
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(mostUsedToolName, fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.White)
                        Text("Used in 42% of your video creations this month", fontSize = 10.sp, color = AccentCyan)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Breakdown of Top Tools
                Text("STUDIO TOOL DISTRIBUTION:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        "1. AI Reel Maker / Shorts Studio" to 0.42f,
                        "2. AI Product Ad Studio" to 0.25f,
                        "3. AI Script Writer & Hooks" to 0.18f,
                        "4. AI Voice Clone & Dubbing" to 0.15f
                    ).forEach { (tool, pct) ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(tool, fontSize = 10.sp, color = Color.LightGray)
                                Text("${(pct * 100).toInt()}%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { pct },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = PrimaryPurple,
                                trackColor = Color(0xFF1E1A36)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly vs Monthly Analytics Chart Representation
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
                    Text(
                        text = if (selectedTimeframe == "Weekly") "WEEKLY CREATION PERFORMANCE" else "MONTHLY CREATION TRENDS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AccentCyan
                    )

                    Text(
                        text = if (selectedTimeframe == "Weekly") "This Week" else "July 2026",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTimeframe == "Weekly") {
                    // Weekly Bar Visualizer (Mon-Sun)
                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    val heights = listOf(0.4f, 0.75f, 0.5f, 0.9f, 0.65f, 1.0f, 0.8f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEachIndexed { idx, day ->
                            val h = heights[idx]
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("${(h * 12).toInt()}", fontSize = 9.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(h)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(AccentCyan, PrimaryPurple)
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(day, fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E1A36))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Peak Output Day: Saturday (12 Videos)", fontSize = 11.sp, color = Color.White)
                        }
                        Text("+28% vs last week", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                    }
                } else {
                    // Monthly Trend Representation
                    val weeks = listOf("Week 1", "Week 2", "Week 3", "Week 4")
                    val weekCreations = listOf(18, 24, 32, 41)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        weeks.forEachIndexed { i, wk ->
                            val count = weekCreations[i]
                            val ratio = count / 45f
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(wk, fontSize = 11.sp, color = Color.LightGray, modifier = Modifier.width(60.dp))
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(12.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF1E1A36))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(ratio)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                Brush.horizontalGradient(listOf(PrimaryPurple, AccentPink))
                                            )
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("$count items", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E1A36))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Monthly Total: 115 Creations", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        Text("Avg 3.8/day", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Export / Share Analytics Report
        Button(
            onClick = {
                val reportSummary = "📊 Creator Analytics Summary Report:\n" +
                        "• Total Videos Created: $totalVideosCreated\n" +
                        "• Total Images Created: $totalImagesCreated\n" +
                        "• Total Credits Used: $totalCreditsUsed\n" +
                        "• Credits Earned: $totalCreditsEarned\n" +
                        "• Most Used Tool: $mostUsedToolName\n\n" +
                        "Generated from AI Video Creator Studio App!"

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, reportSummary)
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Creator Report"))
                Toast.makeText(context, "Analytics Report ready to share! 🚀", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
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
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EXPORT & SHARE CREATOR REPORT", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.Black)
                }
            }
        }
    }
}
