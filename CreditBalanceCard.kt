package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.PrimaryGradientEnd
import com.example.ui.theme.PrimaryPurple

@Composable
fun CreditBalanceCard(
    user: User,
    onWatchAdClick: () -> Unit,
    onUpgradeClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(PrimaryPurple, AccentCyan)),
                RoundedCornerShape(20.dp)
            )
            .testTag("credit_balance_card")
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACCOUNT BALANCE",
                        fontSize = 11.sp,
                        color = AccentCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (user.isPremium) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "UNLIMITED CREDITS",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentGold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.Star, contentDescription = "VIP", tint = AccentGold)
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${user.credits}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Credits Left",
                                fontSize = 16.sp,
                                color = Color.LightGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(PrimaryPurple, PrimaryGradientEnd))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Credits",
                        tint = AccentCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rates Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1B1736))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("10s = 1c", fontSize = 12.sp, color = Color.LightGray, fontWeight = FontWeight.Medium)
                Text("15s = 2c", fontSize = 12.sp, color = Color.LightGray, fontWeight = FontWeight.Medium)
                Text("30s = 3c", fontSize = 12.sp, color = Color.LightGray, fontWeight = FontWeight.Medium)
                Text("60s = 6c", fontSize = 12.sp, color = Color.LightGray, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!user.isPremium) {
                    Button(
                        onClick = onWatchAdClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("watch_ad_card_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPink),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.OndemandVideo, contentDescription = "Ad", tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Watch Ad (+2)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedButton(
                    onClick = onUpgradeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("upgrade_card_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Pro", tint = AccentGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (user.isPremium) "Manage VIP" else "Get Unlimited", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                    }
                }
            }
        }
    }
}
