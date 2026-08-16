package com.example.ui.screens.subscription

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.models.PlanType
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.viewmodel.MainViewModel

@Composable
fun UpgradeScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    var selectedPlan by remember { mutableStateOf(PlanType.YEARLY_PREMIUM) }
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("upgrade_screen")
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PrimaryPurple, Color(0xFF2E0066))
                    )
                )
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Star, contentDescription = "VIP", tint = AccentGold, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("UPGRADE TO UNLIMITED PRO", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Unlock Veo 3.1 Ultra rendering with zero wait times & unlimited credits", fontSize = 12.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Subscription Plan Cards
        // Yearly Plan Card
        val isYearly = selectedPlan == PlanType.YEARLY_PREMIUM
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    if (isYearly) AccentGold else Color(0xFF2A254B),
                    RoundedCornerShape(20.dp)
                )
                .clickable { selectedPlan = PlanType.YEARLY_PREMIUM }
                .testTag("yearly_plan_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("YEARLY PASS", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AccentPink)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("SAVE 50%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("\$69.99 / Year (\$5.83/mo)", fontSize = 13.sp, color = AccentGold, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isYearly) AccentGold else Color.Transparent)
                        .border(1.dp, AccentGold, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isYearly) Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Black, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Monthly Plan Card
        val isMonthly = selectedPlan == PlanType.MONTHLY_PREMIUM
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    if (isMonthly) AccentGold else Color(0xFF2A254B),
                    RoundedCornerShape(20.dp)
                )
                .clickable { selectedPlan = PlanType.MONTHLY_PREMIUM }
                .testTag("monthly_plan_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("MONTHLY PASS", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("\$9.99 / Month", fontSize = 13.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isMonthly) AccentGold else Color.Transparent)
                        .border(1.dp, AccentGold, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isMonthly) Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Black, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Features List
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("PREMIUM UNLOCKED PERKS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentCyan)
                Spacer(modifier = Modifier.height(12.dp))

                listOf(
                    "∞ Unlimited Video & Image Generations",
                    "⚡ 10x Faster Priority Cloud Rendering",
                    "🎬 HD 1080p & 4K Video Exports",
                    "🚫 100% Ad-Free Studio Experience",
                    "🎭 Consistent Character Locking Enabled",
                    "🔊 Studio Multi-Language Voiceover Suite"
                ).forEach { perk ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Check", tint = AccentPink, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(perk, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.subscribePlan(selectedPlan)
                Toast.makeText(context, "🎉 Welcome to PRO UNLIMITED! All features unlocked.", Toast.LENGTH_LONG).show()
                onBackClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("activate_subscription_button"),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Activate", tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedPlan == PlanType.YEARLY_PREMIUM) "ACTIVATE YEARLY PRO (\$69.99)" else "ACTIVATE MONTHLY PRO (\$9.99)",
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}
