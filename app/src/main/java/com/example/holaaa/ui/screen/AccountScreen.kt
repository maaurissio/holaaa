package com.example.holaaa.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // User Profile Section
        ProfileCard(onCopy = { Toast.makeText(context, "Email copied!", Toast.LENGTH_SHORT).show() })
        Spacer(modifier = Modifier.height(32.dp))

        // Settings Section
        SettingsSection()
        Spacer(modifier = Modifier.height(32.dp))

        // Support Section
        SupportSection()
    }
}

@Composable
fun ProfileCard(onCopy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Reemplazamos la imagen por un Box para evitar crasheos si la imagen no existe
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Alex Richards", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("alex.richards@***ple.com", color = Color.Gray, fontSize = 14.sp)
        }
        IconButton(onClick = onCopy) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Email", tint = Color.Gray)
        }
    }
}

@Composable
fun SettingsSection() {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Settings", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
        SettingItem(
            icon = Icons.Default.PersonOutline,
            title = "Account Details",
            onClick = { Toast.makeText(context, "Account Details Clicked", Toast.LENGTH_SHORT).show() }
        )
        SettingItem(
            icon = Icons.Default.NotificationsNone,
            title = "Notifications",
            onClick = { Toast.makeText(context, "Notifications Clicked", Toast.LENGTH_SHORT).show() }
        )
        SettingItem(
            icon = Icons.Default.Email,
            title = "Email",
            onClick = { Toast.makeText(context, "Email Clicked", Toast.LENGTH_SHORT).show() }
        )
        SettingItem(
            icon = Icons.Default.LocationOn,
            title = "Location Services",
            onClick = { Toast.makeText(context, "Location Services Clicked", Toast.LENGTH_SHORT).show() }
        )
        SettingItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Log Out",
            isLogout = true,
            onClick = { Toast.makeText(context, "Log Out Clicked", Toast.LENGTH_SHORT).show() }
        )
    }
}

@Composable
fun SupportSection() {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Support", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
        SettingItem(
            icon = Icons.Default.HelpOutline,
            title = "Contact Us",
            onClick = { Toast.makeText(context, "Contact Us Clicked", Toast.LENGTH_SHORT).show() }
        )
    }
}

@Composable
fun SettingItem(icon: ImageVector, title: String, isLogout: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isLogout) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = if (isLogout) Color.Red else MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (!isLogout) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Go",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
