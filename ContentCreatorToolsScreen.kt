package com.example.ui.screens.tools

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ContentIdea
import com.example.data.models.GenerationType
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.viewmodel.GenerationState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ContentCreatorToolsScreen(viewModel: MainViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Viral Hooks",
        "Video Ideas",
        "Scripts",
        "YT Titles",
        "Hashtags",
        "AI Prompts",
        "Product Scripts",
        "AI Calendar",
        "Saved Calendar"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("content_creator_tools_screen")
    ) {
        // Tab Bar
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AccentCyan,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) AccentCyan else Color.Gray
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            when (selectedTab) {
                0 -> SingleToolForm(viewModel, "VIRAL HOOKS GENERATOR", "Gemini 2.5 Flash: Create 3-second scroll-stopping hooks", GenerationType.VIRAL_HOOK, "hooks")
                1 -> SingleToolForm(viewModel, "VIDEO IDEAS GENERATOR", "Gemini 2.5 Flash: Generate 10 trending content ideas for Reels & Shorts", GenerationType.CONTENT_IDEA, "ideas")
                2 -> SingleToolForm(viewModel, "FULL SCRIPT GENERATOR", "Gemini 2.5 Flash: Complete video script with camera angles & visual cues", GenerationType.SCRIPT, "script")
                3 -> SingleToolForm(viewModel, "YOUTUBE TITLES GENERATOR", "Gemini 2.5 Flash: High-CTR click-worthy titles", GenerationType.YT_TITLE, "titles")
                4 -> SingleToolForm(viewModel, "HASHTAG GENERATOR", "Gemini 2.5 Flash: Find top performing hashtags by reach & niche", GenerationType.HASHTAGS, "hashtags")
                5 -> SingleToolForm(viewModel, "AI PROMPT GENERATOR", "Gemini 2.5 Flash: Expand simple ideas into hyper-detailed Veo/Kling prompts", GenerationType.PROMPT_GEN, "prompts")
                6 -> SingleToolForm(viewModel, "PRODUCT VIDEO SCRIPT GENERATOR", "Gemini 2.5 Flash: High-converting TikTok Shop & e-commerce promo scripts", GenerationType.PRODUCT_SCRIPT, "product_scripts")
                7 -> SingleToolForm(viewModel, "CONTENT CALENDAR GENERATOR", "Gemini 2.5 Flash: 7-day automated video posting strategy & schedule", GenerationType.CONTENT_CALENDAR, "calendar_gen")
                8 -> ContentCalendarTab(viewModel)
            }
        }
    }
}

@Composable
fun SingleToolForm(
    viewModel: MainViewModel,
    title: String,
    subtitle: String,
    type: GenerationType,
    tagPrefix: String
) {
    var topicInput by remember { mutableStateOf("") }
    val state by viewModel.generationState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(title, fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
        Text(subtitle, fontSize = 12.sp, color = AccentCyan)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("NICHE / TOPIC KEYWORDS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentPink)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = topicInput,
                    onValueChange = { topicInput = it },
                    placeholder = { Text("e.g. AI tools, fitness hacks, real estate investing, gaming gear...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("${tagPrefix}_input"),
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (topicInput.isNotBlank()) {
                    viewModel.generateCreatorTool(
                        type = type,
                        toolTitle = title,
                        inputTopic = topicInput,
                        systemInstruction = "You are an elite viral content strategist for YouTube Shorts and Instagram Reels."
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("${tagPrefix}_generate_button"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            enabled = topicInput.isNotBlank()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Generate", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GENERATE $title", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (val currentState = state) {
            is GenerationState.Loading -> CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.CenterHorizontally))
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
                            Text("RESULT", fontWeight = FontWeight.Bold, color = AccentCyan, fontSize = 13.sp)
                            Button(
                                onClick = {
                                    viewModel.saveContentIdea(
                                        title = topicInput.take(30),
                                        platform = "YouTube & Instagram",
                                        category = "Content Idea",
                                        date = "Scheduled Soon",
                                        hook = currentState.item.resultText.take(100),
                                        script = currentState.item.resultText,
                                        tags = "#AI #Creator #Viral"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPink),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("+ Save to Calendar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1B1736))
                                .padding(12.dp)
                        ) {
                            Text(currentState.item.resultText, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun ContentCalendarTab(viewModel: MainViewModel) {
    val ideas by viewModel.contentCalendar.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("CONTENT CALENDAR", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("Plan and track your scheduled video uploads", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (ideas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar", tint = AccentPink, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No scheduled items in Content Calendar yet", color = Color.LightGray, fontSize = 13.sp)
                    Text("Generate scripts or hooks and tap 'Save to Calendar'", color = Color.Gray, fontSize = 11.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ideas) { idea ->
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
                                Text(idea.title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                                IconButton(onClick = { viewModel.deleteContentIdea(idea.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Platform: ${idea.platform} • Scheduled: ${idea.scheduledDate}", fontSize = 11.sp, color = AccentCyan)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(idea.scriptText, color = Color.LightGray, fontSize = 12.sp, maxLines = 3)
                        }
                    }
                }
            }
        }
    }
}
