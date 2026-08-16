package com.example.ui.screens.auth

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.LoginType
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryPurple

@Composable
fun AuthScreen(
    onLoginSuccess: (String, String, LoginType) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(PrimaryPurple, AccentPink))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Bolt, contentDescription = "Logo", tint = Color.White, modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("AI VIDEO CREATOR HUB", fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color.White)
        Text("Sign in to sync credits & creations", fontSize = 13.sp, color = AccentCyan)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = AccentCyan) },
                    modifier = Modifier.fillMaxWidth().testTag("auth_name_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B1736),
                        unfocusedContainerColor = Color(0xFF1B1736),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = AccentPink) },
                    modifier = Modifier.fillMaxWidth().testTag("auth_email_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B1736),
                        unfocusedContainerColor = Color(0xFF1B1736),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = PrimaryPurple) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B1736),
                        unfocusedContainerColor = Color(0xFF1B1736),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Login Button
                Button(
                    onClick = {
                        val finalName = name.ifBlank { "Creator" }
                        val finalEmail = email.ifBlank { "creator@aivideo.hub" }
                        onLoginSuccess(finalName, finalEmail, LoginType.EMAIL)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("email_login_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("SIGN IN WITH EMAIL", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Google Login Button
                OutlinedButton(
                    onClick = {
                        onLoginSuccess("Google Creator", "google.user@gmail.com", LoginType.GOOGLE)
                        Toast.makeText(context, "Signed in with Google! 🚀", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("google_login_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Continue with Google", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Guest Login Button
                Button(
                    onClick = {
                        onLoginSuccess("Guest Creator", "guest@aivideo.hub", LoginType.GUEST)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("guest_login_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF282442)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Continue as Guest (20 Free Credits)", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
