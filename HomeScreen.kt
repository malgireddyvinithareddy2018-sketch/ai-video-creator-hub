package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.GenerationItem
import com.example.data.models.User
import com.example.ui.components.CreditBalanceCard
import com.example.ui.components.VideoPlayerCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryGradientEnd
import com.example.ui.theme.PrimaryPurple

data class ToolQuickItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val badge: String? = null,
    val accentColor: Color
)

val aiGeneratorsList = listOf(
    ToolQuickItem("text_to_video", "Text to Video", "10s - 60s HD Videos", Icons.Default.Movie, "HOT", AccentCyan),
    ToolQuickItem("image_to_video", "Image to Video", "Animate any Photo", Icons.Default.Image, null, PrimaryPurple),
    ToolQuickItem("text_to_image", "Text to Image", "AI Art & Styles", Icons.Default.AutoAwesome, null, AccentPink),
    ToolQuickItem("product_to_video", "Product to Video", "Paste Product Link", Icons.Default.ShoppingBag, "NEW", AccentGold),
    ToolQuickItem("character_gen", "AI Character", "Consistent Models", Icons.Default.Face, null, AccentCyan),
    ToolQuickItem("voice_gen", "AI Voiceover", "Telugu & English", Icons.Default.RecordVoiceOver, null, PrimaryPurple),
    ToolQuickItem("subtitles_gen", "Auto Subtitles", "Multi-language SRT", Icons.Default.Subtitles, null, AccentPink),
    ToolQuickItem("music_gen", "AI Music", "Background & Cinematic", Icons.Default.MusicNote, null, AccentGold)
)

val creatorToolsList = listOf(
    ToolQuickItem("video_ideas", "Video Ideas", "Viral Topic Starters", Icons.Default.Lightbulb, null, AccentCyan),
    ToolQuickItem("viral_hooks", "Viral Hooks", "3-Sec Hook Formula", Icons.Default.AutoAwesome, "POPULAR", AccentPink),
    ToolQuickItem("script_gen", "Script Generator", "Full Video Scripts", Icons.Default.VideoLibrary, null, PrimaryPurple),
    ToolQuickItem("yt_titles", "YouTube Titles", "High CTR Headlines", Icons.Default.OndemandVideo, null, AccentGold),
    ToolQuickItem("ig_captions", "IG Captions", "Engaging Posts", Icons.Default.AutoAwesome, null, AccentCyan)
)

@Composable
fun HomeScreen(
    user: User,
    recentGenerations: List<GenerationItem>,
    onWatchAdClick: () -> Unit,
    onUpgradeClick: () -> Unit,
    onToolClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("home_screen_column"),
        contentPadding = PaddingValues(bottom = 32.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Banner
        item {
            HeroBannerCard(onQuickCreate = { onToolClick("text_to_video") })
        }

        // Credit Balance & Rewarded Ad Card
        item {
            CreditBalanceCard(
                user = user,
                onWatchAdClick = onWatchAdClick,
                onUpgradeClick = onUpgradeClick
            )
        }

        // AI Features Grid Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AI GENERATORS",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "11 Tools Available",
                    fontSize = 12.sp,
                    color = AccentCyan
                )
            }
        }

        // AI Features Grid
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(aiGeneratorsList) { item ->
                    ToolGridCard(item = item, onClick = { onToolClick(item.id) })
                }
            }
        }

        // Content Creator Tools Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CREATOR SUITE",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Hooks, Scripts & Ideas",
                    fontSize = 12.sp,
                    color = AccentPink
                )
            }
        }

        // Creator Tools Horizontal Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(creatorToolsList) { item ->
                    CreatorRowCard(item = item, onClick = { onToolClick(item.id) })
                }
            }
        }

        // Recent Creations Header
        if (recentGenerations.isNotEmpty()) {
            item {
                Text(
                    text = "RECENT CREATIONS",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }

            items(recentGenerations.take(2)) { genItem ->
                VideoPlayerCard(item = genItem)
            }
        }
    }
}

@Composable
fun HeroBannerCard(onQuickCreate: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clickable { onQuickCreate() }
            .testTag("hero_banner_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF28104E),
                            PrimaryGradientEnd,
                            Color(0xFF091326)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterStart),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(AccentCyan.copy(alpha = 0.2f))
                        .border(1.dp, AccentCyan, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✨ NEXT-GEN VEO 3.1 AI ENGINE",
                        color = AccentCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = "Create Viral AI Videos in Seconds",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Text to Video • Image Animation • Voice & Music",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Generate Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Start",
                        tint = AccentCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ToolGridCard(item: ToolQuickItem, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .border(1.dp, Color(0xFF221E3D), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("tool_card_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(item.accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    if (item.badge != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(AccentPink)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(text = item.badge, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Text(
                    text = item.subtitle,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun CreatorRowCard(item: ToolQuickItem, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .width(140.dp)
            .height(110.dp)
            .border(1.dp, Color(0xFF252142), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("creator_card_${item.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(item.accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = item.subtitle,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}
