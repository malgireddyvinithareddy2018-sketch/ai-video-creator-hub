package com.example.ui.screens.referral

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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReferralScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val stats by viewModel.referralStats.collectAsState()
    val history by viewModel.referralHistory.collectAsState()
    val user by viewModel.user.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0: Invite & Code, 1: Referral Dashboard & History, 2: Affiliate Earnings

    // Invite Form
    var friendName by remember { mutableStateOf("") }
    var friendEmail by remember { mutableStateOf("") }

    // Redeem Form
    var redeemCode by remember { mutableStateOf("") }

    // Cashout Form
    var cashoutAmount by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("UPI / GPay") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("referral_screen")
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
                    Text("REFERRAL & AFFILIATE STUDIO", fontWeight = FontWeight.Black, fontSize = 17.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("5 CREDITS/REF", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }
                Text("Invite friends, earn free video credits & cashout revenue share", fontSize = 11.sp, color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Banner: Earn 5 Credits per Referral
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, Brush.horizontalGradient(listOf(AccentGold, PrimaryPurple)), RoundedCornerShape(18.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(PrimaryPurple.copy(alpha = 0.35f), DarkSurface),
                            radius = 600f
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(AccentGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color.Black, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("EARN 5 CREDITS PER REFERRAL", fontWeight = FontWeight.Black, fontSize = 15.sp, color = AccentGold)
                                Text("Your friend gets +5 Credits on signup too!", fontSize = 11.sp, color = Color.LightGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E1A36))
                                .padding(10.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                Text("MY REFERRAL CODE", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text(stats.referralCode, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }

                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(stats.referralCode))
                                Toast.makeText(context, "Referral code copied: ${stats.referralCode}", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Join AI Video Creator Hub! Use my referral code '${stats.referralCode}' to get 5 FREE credits instantly for AI videos & voice cloning! Download now."
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Referral Code"))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1. Invite Friends", "2. Dashboard & History", "3. Affiliate ($${stats.affiliateEarningsUsd})").forEachIndexed { idx, title ->
                val isSel = activeTab == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) PrimaryPurple else DarkSurface)
                        .border(1.dp, if (isSel) AccentCyan else Color(0xFF2E2954), RoundedCornerShape(12.dp))
                        .clickable { activeTab = idx }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) Color.White else Color.LightGray,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            // TAB 0: INVITE FRIENDS & REDEEM CODE
            0 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("INVITE FRIENDS VIA EMAIL / DIRECT LINK", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AccentCyan)
                        Text("Send a personalized invite link & grant 5 credits on signup.", fontSize = 11.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = friendName,
                            onValueChange = { friendName = it },
                            placeholder = { Text("Friend's Full Name (e.g. Ramesh Kumar)", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentCyan,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = friendEmail,
                            onValueChange = { friendEmail = it },
                            placeholder = { Text("Friend's Email Address (e.g. ramesh@gmail.com)", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentCyan,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.inviteFriend(friendName, friendEmail) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    if (success) {
                                        friendName = ""
                                        friendEmail = ""
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SEND INVITE & EARN 5 CREDITS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // REDEEM A FRIEND'S REFERRAL CODE
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("REDEEM A FRIEND'S REFERRAL CODE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AccentGold)
                        Text("Have a referral code from a friend? Enter it below to claim +5 Credits.", fontSize = 11.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = redeemCode,
                                onValueChange = { redeemCode = it },
                                placeholder = { Text("e.g. AIVIDEO-REF-1234", color = Color.Gray, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = AccentGold,
                                    unfocusedBorderColor = Color(0xFF2E2954)
                                )
                            )

                            Button(
                                onClick = {
                                    viewModel.redeemReferralCode(redeemCode) { success, msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                        if (success) redeemCode = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text("Claim +5", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }

            // TAB 1: REFERRAL DASHBOARD & HISTORY
            1 -> {
                // STATS GRID
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Invites", fontSize = 10.sp, color = Color.Gray)
                            Text("${stats.totalInvites}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Successful", fontSize = 10.sp, color = Color.Gray)
                            Text("${stats.successfulSignups}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = AccentCyan)
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Credits Earned", fontSize = 10.sp, color = Color.Gray)
                            Text("+${stats.totalCreditsEarned}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = AccentGold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // REFERRAL HISTORY
                Text("REFERRAL ACTIVITY LOG", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))

                if (history.isEmpty()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                            Text("No referrals yet. Invite friends to start earning credits!", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        history.forEach { item ->
                            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                            val dateStr = sdf.format(Date(item.timestamp))

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(PrimaryPurple.copy(alpha = 0.3f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(item.friendName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                            Text("${item.friendEmail} • $dateStr", fontSize = 10.sp, color = Color.Gray)
                                        }
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (item.creditsEarned > 0) AccentGold.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(item.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (item.creditsEarned > 0) AccentGold else Color.LightGray)
                                        }
                                        if (item.affiliateCommission > 0) {
                                            Text("+$${String.format("%.2f", item.affiliateCommission)} USD", fontSize = 10.sp, color = AccentCyan, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 2: AFFILIATE EARNINGS SCREEN
            2 -> {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("AFFILIATE BALANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                                Text("$${String.format("%.2f", stats.affiliateEarningsUsd)} USD", fontSize = 24.sp, fontWeight = FontWeight.Black, color = AccentCyan)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AccentGold.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(stats.affiliateTier, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = Color(0xFF2E2954))
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("CASHOUT / REVENUE SHARE PAYOUT", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        Text("Withdraw earnings directly to UPI, PayPal, or convert to 100 AI Credits.", fontSize = 11.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = cashoutAmount,
                            onValueChange = { cashoutAmount = it },
                            placeholder = { Text("Enter Cashout Amount in USD (Min $10)", color = Color.Gray, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentCyan,
                                unfocusedBorderColor = Color(0xFF2E2954)
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Select Payment Method", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))

                        listOf("UPI / GPay / PhonePe", "PayPal / International Wire", "Convert $10 to 100 Credits").forEach { method ->
                            val isSel = paymentMethod == method
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) PrimaryPurple.copy(alpha = 0.3f) else Color(0xFF1E1A36))
                                    .border(1.dp, if (isSel) AccentCyan else Color.Transparent, RoundedCornerShape(10.dp))
                                    .clickable { paymentMethod = method }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = isSel, onClick = { paymentMethod = method })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(method, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val amt = cashoutAmount.toDoubleOrNull() ?: 10.0
                                viewModel.cashoutAffiliateEarnings(amt, paymentMethod) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    if (success) cashoutAmount = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("SUBMIT CASHOUT REQUEST", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
