package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary

@Composable
fun SplashScreen(
    onStartClick: () -> Unit
) {
    androidx.compose.runtime.LaunchedEffect(Unit) {
        try {
            kotlinx.coroutines.delay(2500)
            onStartClick()
        } catch (_: Exception) {
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyPrimary)
            .testTag("splash_screen")
    ) {
        // Decorative background watermarks / circular waves
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Top-right subtle circular ring
            drawCircle(
                color = Color(0x1A29B6F0),
                radius = width * 0.55f,
                center = androidx.compose.ui.geometry.Offset(width * 0.9f, height * 0.12f)
            )
            // Bottom-left subtle circular ring
            drawCircle(
                color = Color(0x1429B6F0),
                radius = width * 0.65f,
                center = androidx.compose.ui.geometry.Offset(width * 0.1f, height * 0.88f)
            )
        }

        // Centered Content Layout: Logo -> Name -> Established -> "শুরু করুন" Button
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.club_logo),
                contentDescription = "Club Logo",
                modifier = Modifier
                    .size(175.dp)
                    .clip(CircleShape)
                    .testTag("splash_logo"),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(28.dp))

            // App Name (24sp bold white)
            Text(
                text = "হিলফুল ফুযুল স্বর্ণটিলা যুব সংঘ",
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Established (14sp light blue)
            Text(
                text = "স্থাপিত ২০১৭",
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF9FB2DB),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Start Button: "শুরু করুন" (width 60%, height 52dp, corner 16dp, background #29B6F0, text #1B2559 bold 18sp)
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(52.dp)
                    .testTag("start_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF29B6F0),
                    contentColor = Color(0xFF1B2559)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "শুরু করুন",
                    fontFamily = HindSiliguri,
                    color = Color(0xFF1B2559),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
