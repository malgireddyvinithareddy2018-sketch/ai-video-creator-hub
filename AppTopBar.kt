package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.User
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple

@Composable
fun AppTopBar(
    user: User,
    onWatchAdClick: () -> Unit,
    onUpgradeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title & Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onProfileClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PrimaryPurple, AccentPink)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "App Logo",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            ColumnTitle()
        }

        // Actions: Credit Badge + Watch Ad + Profile
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Credit Pill Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E1A36))
                    .border(1.dp, Brush.horizontalGradient(listOf(PrimaryPurple, AccentCyan)), RoundedCornerShape(20.dp))
                    .clickable { if (user.isPremium) onProfileClick() else onUpgradeClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("credit_pill_badge")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user.isPremium) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "VIP",
                            tint = AccentGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PRO VIP",
                            color = AccentGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = "⚡ ${user.credits}",
                            color = AccentCyan,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Watch Ad Button
            if (!user.isPremium) {
                Button(
                    onClick = onWatchAdClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPink
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .heightIn(max = 32.dp)
                        .testTag("watch_ad_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.OndemandVideo,
                            contentDescription = "Watch Ad",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "+2", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))
            }

            // Profile Avatar Button
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("profile_top_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Profile",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun ColumnTitle() {
    androidx.compose.foundation.layout.Column {
        Text(
            text = "AI VIDEO HUB",
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = Color.White,
            letterSpacing = 1.sp
        )
        Text(
            text = "Studio Creator 2026",
            fontSize = 10.sp,
            color = AccentCyan
        )
    }
}
