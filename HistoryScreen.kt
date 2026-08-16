package com.example.ui.screens.history

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.models.GenerationType
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HistoryScreen(viewModel: MainViewModel) {
    val history by viewModel.history.collectAsState()
    var selectedFilter by remember { mutableIntStateOf(0) } // 0: All, 1: Videos, 2: Images, 3: Prompts

    val filteredList = history.filter { item ->
        when (selectedFilter) {
            1 -> item.type in listOf(GenerationType.TEXT_TO_VIDEO, GenerationType.IMAGE_TO_VIDEO, GenerationType.PROMPT_TO_VIDEO, GenerationType.PRODUCT_TO_VIDEO)
            2 -> item.type == GenerationType.TEXT_TO_IMAGE || item.type == GenerationType.CHARACTER
            3 -> item.type in listOf(GenerationType.SCRIPT, GenerationType.VIRAL_HOOK, GenerationType.YT_TITLE, GenerationType.IG_CAPTION)
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .testTag("history_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("CREATION HISTORY", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                Text("${history.size} Generated Assets Saved", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedFilter,
            containerColor = DarkSurface,
            contentColor = Color.White
        ) {
            listOf("All (${history.size})", "Videos", "Images", "Prompts/Scripts").forEachIndexed { index, label ->
                Tab(
                    selected = selectedFilter == index,
                    onClick = { selectedFilter = index },
                    text = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (selectedFilter == index) AccentCyan else Color.Gray) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.History, contentDescription = "History", tint = AccentPink, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No saved items in history yet", color = Color.LightGray, fontSize = 13.sp)
                    Text("Start creating AI videos & images to save them locally", color = Color.Gray, fontSize = 11.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredList) { item ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Type: ${item.type.name} • ${item.durationSeconds}s",
                                fontSize = 11.sp,
                                color = AccentPink,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { viewModel.deleteHistoryItem(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                        VideoPlayerCard(item = item)
                    }
                }
            }
        }
    }
}
