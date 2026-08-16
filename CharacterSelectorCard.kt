package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple
import com.example.ui.viewmodel.MainViewModel

@Composable
fun CharacterSelectorCard(viewModel: MainViewModel) {
    val activeCharacter by viewModel.activeCharacter.collectAsState()
    val savedCharacters by viewModel.savedCharacters.collectAsState()
    var manualCharId by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("character_selector_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Character Lock",
                        tint = if (activeCharacter != null) AccentGold else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CHARACTER CONSISTENCY LOCK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeCharacter != null) AccentGold else Color.Gray
                    )
                }

                if (activeCharacter != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Red.copy(alpha = 0.2f))
                            .clickable { viewModel.setActiveCharacter(null) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Unlock", color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (activeCharacter != null) {
                val char = activeCharacter!!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryPurple.copy(alpha = 0.25f))
                        .border(1.dp, AccentGold, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(PrimaryPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Face, contentDescription = "Face", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "LOCKED: ${char.id} (${char.name})",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Text(
                                text = "${char.gender} • ${char.style} • Identical Face, Body & Outfit Lock Active",
                                fontSize = 11.sp,
                                color = AccentCyan
                            )
                        }
                        Icon(Icons.Default.Check, contentDescription = "Active", tint = AccentGold)
                    }
                }
            } else {
                Text(
                    text = "Select a saved Character ID to lock same face, hairstyle, clothing & colors across generations:",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )

                if (savedCharacters.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(savedCharacters) { char ->
                            val isSel = activeCharacter?.id == char.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) PrimaryPurple else Color(0xFF1E1A36))
                                    .border(1.dp, if (isSel) AccentGold else Color(0xFF2E2954), RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setActiveCharacter(char) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(char.id, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        Text(char.name, color = Color.Gray, fontSize = 9.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = manualCharId,
                        onValueChange = { manualCharId = it },
                        placeholder = { Text("Or enter Character ID e.g. CHAR-8A92B", color = Color.Gray, fontSize = 11.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1B1736),
                            unfocusedContainerColor = Color(0xFF1B1736),
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFF2E2954),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryPurple)
                            .clickable {
                                if (manualCharId.isNotBlank()) {
                                    viewModel.selectCharacterById(manualCharId)
                                }
                            }
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("LOCK", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
