package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HindSiliguri

@Composable
fun GalleryScreen(
    onNavigateBack: () -> Unit
) {
    BackHandler {
        onNavigateBack()
    }

    var selectedCategory by remember { mutableStateOf("সব") }
    val categories = listOf("সব", "মাহফিল", "জুলুস", "রক্তদান", "সমাজসেবা")

    // ১২টি রঙিন প্লেসহোল্ডার গ্রিড আইটেম (ছবির মতো অলটারনেটিং নীল ও ল্যাভেন্ডার রং)
    val skyBlue = Color(0xFFD6F0FA)
    val softLavender = Color(0xFFE2E0F8)

    val placeholderItems = remember {
        listOf(
            skyBlue, softLavender, skyBlue,
            softLavender, skyBlue, softLavender,
            skyBlue, softLavender, skyBlue,
            softLavender, skyBlue, softLavender
        )
    }

    Scaffold(
        modifier = Modifier.testTag("gallery_screen"),
        containerColor = Color(0xFFF4F7FC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FC))
                .padding(innerPadding)
        ) {
            // সাদা হেডার: বাঁয়ে ব্যাক তীর, "গ্যালারি" ২২sp Bold নেভি (#1B2559)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("gallery_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = Color(0xFF1B2559),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "গ্যালারি",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1B2559)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            Spacer(modifier = Modifier.height(14.dp))

            // ক্যাটাগরি চিপ (সব, মাহফিল, জুলুস, রক্তদান, সমাজসেবা)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFF1B2559) else Color.White)
                            .then(
                                if (!isSelected) {
                                    Modifier.border(
                                        BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                        RoundedCornerShape(20.dp)
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 20.dp)
                            .testTag("gallery_chip_$category"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontFamily = HindSiliguri,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White else Color(0xFF1B2559)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ৩ কলামের রঙিন প্লেসহোল্ডার গ্রিড
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(placeholderItems.size) { index ->
                    val bgColor = placeholderItems[index]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "ছবি প্লেসহোল্ডার",
                            tint = Color(0xFF1B2559),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // নিচে ক্যাপশন: "সংগঠনের কার্যক্রমের ছবি এখানে দেখা যাবে"
            Text(
                text = "সংগঠনের কার্যক্রমের ছবি এখানে দেখা যাবে",
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
        }
    }
}
