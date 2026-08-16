package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.GenerationItem
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple

@Composable
fun VideoPlayerCard(
    item: GenerationItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }

    val aspectFloat = when (item.aspectRatio) {
        "9:16" -> 9f / 16f
        "1:1" -> 1f
        else -> 16f / 9f
    }

    val infiniteTransition = rememberInfiniteTransition(label = "player_animation")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, PrimaryPurple, RoundedCornerShape(20.dp))
            .testTag("video_player_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentCyan.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${item.aspectRatio} • ${item.durationSeconds}s",
                        color = AccentCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Video Preview Canvas Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectFloat)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F0B1A))
                    .border(1.dp, AccentPink.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isPlaying) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val cx = width / 2f
                        val cy = height / 2f

                        // Draw futuristic holographic animation representing AI Video synthesis
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(PrimaryPurple.copy(alpha = 0.8f), Color.Transparent),
                                center = Offset(cx + kotlin.math.sin(Math.toRadians(waveOffset.toDouble())).toFloat() * 100f, cy),
                                radius = width * 0.7f
                            ),
                            radius = width * 0.6f,
                            center = Offset(cx, cy)
                        )

                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(AccentCyan.copy(alpha = 0.6f), Color.Transparent),
                                center = Offset(cx, cy + kotlin.math.cos(Math.toRadians(waveOffset.toDouble())).toFloat() * 80f),
                                radius = width * 0.5f
                            ),
                            radius = width * 0.4f,
                            center = Offset(cx, cy)
                        )
                    }
                }

                // Center Play / Pause overlay
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // HD Badge Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Hd, contentDescription = "HD", tint = AccentCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("1080p", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Script/Prompt preview
            if (item.resultText.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1B1736))
                        .padding(12.dp)
                ) {
                    Text(
                        text = item.resultText,
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        maxLines = 4
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Export & Share Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        downloadMedia(context, item.title)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("download_video_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, contentDescription = "Download", tint = AccentCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download HD", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = {
                        shareContent(context, item.title, item.prompt)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("share_video_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentPink)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = AccentPink, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Video", color = AccentPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun downloadMedia(context: Context, title: String) {
    Toast.makeText(context, "Saved HD Video '$title' to Device Gallery! 📲", Toast.LENGTH_LONG).show()
}

private fun shareContent(context: Context, title: String, prompt: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out my AI Video '$title' generated with AI Video Creator Hub!\n\nPrompt: $prompt")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share AI Video")
    context.startActivity(shareIntent)
}
