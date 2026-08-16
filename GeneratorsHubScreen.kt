package com.example.ui.screens.generators

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple

data class GeneratorHubItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val costTag: String,
    val icon: ImageVector,
    val accentColor: Color
)

val allGeneratorsHubList = listOf(
    GeneratorHubItem("text_to_video", "1. Text to Video Generator", "All languages supported • 10s, 15s, 30s, 60s HD", "1 - 6 Credits", Icons.Default.Movie, AccentCyan),
    GeneratorHubItem("image_to_video", "2. Image to Video Generator", "Upload image & generate animated 3D video", "1 - 6 Credits", Icons.Default.Image, AccentPink),
    GeneratorHubItem("text_to_image", "3. Text to Image Generator", "AI image creation with multiple artistic styles", "1 Credit", Icons.Default.AutoAwesome, PrimaryPurple),
    GeneratorHubItem("prompt_to_video", "4. Prompt to Video Generator", "Generate cinematic videos directly from story prompts", "1 - 6 Credits", Icons.Default.Movie, AccentGold),
    GeneratorHubItem("prompt_images_video", "5. Prompt + Images to Video", "Upload multiple photos & generate story videos", "2 - 6 Credits", Icons.Default.Image, AccentCyan),
    GeneratorHubItem("character_gen", "6. AI Character Master System", "Sheet Generator, Front/Side/Back Views, Expressions, Actions, Lock & Consistent Face", "1 Credit", Icons.Default.Face, AccentPink),
    GeneratorHubItem("voice_gen", "7. AI Voice Generator", "Male, Female, Telugu, English & Multi-language", "1 Credit", Icons.Default.RecordVoiceOver, PrimaryPurple),
    GeneratorHubItem("subtitles_gen", "8. Auto Subtitles Generator", "Telugu, English & Multi-language SRT captions", "1 Credit", Icons.Default.Subtitles, AccentGold),
    GeneratorHubItem("music_gen", "9. AI Music Studio", "Background, Cinematic, Motivational, Podcast & Ad Music (Royalty Free MP3)", "1 Credit", Icons.Default.MusicNote, AccentGold),
    GeneratorHubItem("product_to_video", "10. AI Product Ad Studio", "Amazon, Flipkart, Meesho, Product URL/Image to Video, AI Sales Script & MP4", "1 - 6 Credits", Icons.Default.ShoppingBag, AccentGold),
    GeneratorHubItem("talking_photo", "11. AI Talking Photo", "Upload photo, Telugu/English script, Lip-sync AI video", "1 - 6 Credits", Icons.Default.Face, AccentPink),
    GeneratorHubItem("thumbnail_gen", "12. AI Thumbnail Generator", "YouTube & Instagram thumbnails + AI title suggestions", "1 - 7 Credits", Icons.Default.Image, AccentGold),
    GeneratorHubItem("video_dubbing", "13. AI Video Dubbing", "Upload video, Auto language detect, Lip-sync dubbing & SRT subtitles", "2 - 8 Credits", Icons.Default.RecordVoiceOver, AccentCyan),
    GeneratorHubItem("avatar_gen", "14. AI Avatar Generator", "Photo to Avatar, AI Human, Cartoon, Anime, Business Avatar & Face Lock", "1 - 6 Credits", Icons.Default.Face, AccentPink),
    GeneratorHubItem("story_gen", "15. AI Story Generator", "Ideas, Scene-by-Scene Script, Characters, Video Prompts, Voiceover & SRT", "1 - 6 Credits", Icons.Default.AutoAwesome, AccentGold),
    GeneratorHubItem("video_templates", "16. AI Video Templates Library", "Product Ads, Motivation, Podcast, Story, Luxury, Travel, Kids, Education & News", "1 - 5 Credits", Icons.Default.Movie, AccentCyan),
    GeneratorHubItem("reel_maker", "17. AI Reel Maker", "Instagram Reels, YouTube Shorts, TikTok, Auto Hooks, Captions, Trending Music & Hashtags", "1 - 6 Credits", Icons.Default.Movie, AccentPink),
    GeneratorHubItem("podcast_gen", "18. AI Podcast Generator", "Topic to Podcast Script, AI Host (Male & Female), Multilingual, Intro/Outro, BGM & MP3/MP4 Export", "2 - 8 Credits", Icons.Default.RecordVoiceOver, AccentGold),
    GeneratorHubItem("character_consistency_pro", "19. Character Consistency Pro", "Create Master Character, ID System, Consistent Face across Images/Videos, Expressions, Poses, Outfits & Library", "1 Credit", Icons.Default.Face, AccentPink),
    GeneratorHubItem("voice_clone", "20. AI Voice Clone Studio", "Upload Voice Sample, Create Custom AI Voice, Multi-Lang Speech, Emotional Voices & Library", "2 Credits", Icons.Default.Mic, PrimaryPurple),
    GeneratorHubItem("referral", "21. Referral & Affiliate Program", "Invite Friends, Share Code, Earn +5 Credits/Ref, Referral Dashboard & Affiliate Cashout", "Free + Earn", Icons.Default.CardGiftcard, AccentGold),
    GeneratorHubItem("script_writer", "22. AI Script Writer Studio", "YouTube, Shorts/Reels, Product Ad, Story & Podcast Scripts with Multi-Lang & Export", "1 Credit", Icons.Default.EditNote, AccentCyan),
    GeneratorHubItem("hook_gen", "23. AI Hook Generator Studio", "Viral, Curiosity, Emotional, Sales, Motivation & Story Hooks with 1-Click Copy", "1 Credit", Icons.Default.Bolt, AccentPink),
    GeneratorHubItem("title_hashtag", "24. AI Title & Hashtag Studio", "YouTube & Shorts Titles, Instagram Captions, Viral Hashtags, SEO Suggestions & Multi-Lang", "1 Credit", Icons.Default.Tag, AccentGold),
    GeneratorHubItem("prompt_library", "25. AI Prompt Library", "Search, Save Favorites & Copy High-Converting Prompts for Videos, Images, Ads & Stories", "Free Hub", Icons.Default.AutoAwesome, AccentCyan),
    GeneratorHubItem("creator_analytics", "26. Creator Analytics Dashboard", "Total Videos/Images Created, Credits Used & Earned, Most Used Tool, Weekly & Monthly Trends", "Free Hub", Icons.Default.BarChart, AccentGold),
    GeneratorHubItem("project_manager", "27. AI Project Manager", "Save Projects, Draft Projects, Recent Projects, Search, Duplicate & Delete Projects", "Free Hub", Icons.Default.FolderSpecial, AccentCyan),
    GeneratorHubItem("social_auto_post", "28. Social Media Auto Posting", "YouTube, Instagram, Facebook & Telegram Instant Publish, Scheduling, Auto Metadata & History", "Free Hub", Icons.AutoMirrored.Filled.Send, AccentCyan),
    GeneratorHubItem("cloud_backup", "29. Cloud Backup System", "Firebase Storage Sync, Auto Backup, Restore Projects, Sync Across Devices, Images, Videos & Prompts", "Free Hub", Icons.Default.CloudDone, AccentGold),
    GeneratorHubItem("team_workspace", "30. Team Workspace & Roles", "Create Team, Invite Members, Shared Projects, Team Chat, Roles (Admin, Editor, Viewer) & Activity History", "Free Hub", Icons.Default.GroupAdd, AccentCyan),
    GeneratorHubItem("course_creator", "31. AI Course Creator", "Course Outline, Lesson Generator, Quizzes, Workbooks, Teleprompter Scripts & PDF Export", "Free Hub", Icons.Default.School, AccentGold),
    GeneratorHubItem("long_to_short", "32. Long Video to Short Reels", "Convert Long MP4/YouTube to 9:16 Short Reels with Auto Captions, Viral Hooks & Trim", "1 - 6 Credits", Icons.Default.Movie, AccentPink),
    GeneratorHubItem("short_to_long", "33. Short Video to Long Expander", "Extend 5s/10s short clips into 30s, 60s, or 120s long videos with AI frame interpolation", "2 - 8 Credits", Icons.Default.AutoAwesome, PrimaryPurple)
)

@Composable
fun GeneratorsHubScreen(
    onSelectGenerator: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .testTag("generators_hub_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("AI GENERATORS SUITE", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
            Text("Select an AI tool to start creating studio content", fontSize = 12.sp, color = AccentCyan)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(allGeneratorsHubList) { tool ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF2A254B), RoundedCornerShape(18.dp))
                    .clickable { onSelectGenerator(tool.id) }
                    .testTag("hub_item_${tool.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(tool.accentColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(tool.icon, contentDescription = tool.title, tint = tool.accentColor, modifier = Modifier.size(24.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(tool.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(tool.subtitle, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(tool.accentColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(tool.costTag, fontSize = 10.sp, color = tool.accentColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
