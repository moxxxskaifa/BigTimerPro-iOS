package com.bigtimer.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigtimer.app.platform.HapticFeedback
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen() {
    var minutes by remember { mutableIntStateOf(5) }
    var seconds by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var totalSecs by remember { mutableIntStateOf(300) }
    val haptic = remember { HapticFeedback() }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (totalSecs > 0) { delay(1000L); totalSecs--; minutes = totalSecs / 60; seconds = totalSecs % 60 }
            if (totalSecs == 0) { isRunning = false; haptic.heavy() }
        }
    }

    val pad = { v: Int -> v.toString().padStart(2, '0') }
    Box(Modifier.fillMaxSize().background(Color(0xFF1A1A2E)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${pad(minutes)}:${pad(seconds)}", fontSize = 96.sp, fontWeight = FontWeight.Bold,
                 color = Color.White, textAlign = TextAlign.Center, letterSpacing = 4.sp)
            Spacer(Modifier.height(40.dp))
            if (!isRunning && totalSecs == if (minutes*60+seconds == 0) 0 else 1) {
                // Setting mode
                Slider(value = minutes.toFloat(), onValueChange = { minutes = it.toInt(); totalSecs = minutes * 60 + seconds },
                       valueRange = 1f..99f, modifier = Modifier.width(300.dp))
                Text("$minutes min", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.height(20.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { isRunning = !isRunning },
                       modifier = Modifier.height(56.dp), shape = RoundedCornerShape(28.dp),
                       colors = ButtonDefaults.buttonColors(containerColor = if (isRunning) Color(0xFFE74C3C) else Color(0xFF27AE60))) {
                    Text(if (isRunning) "STOP" else "START", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                if (!isRunning) {
                    OutlinedButton(onClick = { minutes = 5; seconds = 0; totalSecs = 300 },
                        modifier = Modifier.height(56.dp), shape = RoundedCornerShape(28.dp)) { Text("Reset") }
                }
            }
        }
    }
}
