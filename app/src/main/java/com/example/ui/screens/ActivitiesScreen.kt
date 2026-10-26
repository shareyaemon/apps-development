package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActivityItem
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BloodRed
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtitleGray
import com.example.ui.theme.SuccessGreen

@Composable
fun ActivitiesScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: আসন্ন, 1: সম্পন্ন
    var showVolunteerDialog by remember { mutableStateOf(false) }
    var volunteerName by remember { mutableStateOf("") }
    var volunteerPhone by remember { mutableStateOf("") }

    BackHandler {
        onNavigateBack()
    }

    val upcomingActivities = remember {
        listOf(
            ActivityItem(
                id = "1",
                title = "বার্ষিক কুরআন মাহফিল",
                date = "তারিখ: [ঘোষণা হবে]",
                isRedIcon = false
            ),
            ActivityItem(
                id = "2",
                title = "বার্ষিক জশনে জুলুস",
                date = "তারিখ: [ঘোষণা হবে]",
                isRedIcon = false
            ),
            ActivityItem(
                id = "3",
                title = "রক্তদান ক্যাম্প",
                date = "তারিখ: [ঘোষণা হবে]",
                isRedIcon = true
            ),
            ActivityItem(
                id = "4",
                title = "কবরস্থান পরিষ্কার কর্মসূচি",
                date = "তারিখ: [ঘোষণা হবে]",
                isRedIcon = false
            ),
            ActivityItem(
                id = "5",
                title = "মসজিদ সাজসজ্জা (ঈদ ও কোরবানি)",
                date = "তারিখ: [ঘোষণা হবে]",
                isRedIcon = false
            )
        )
    }

    val completedActivities = remember {
        listOf(
            ActivityItem(
                id = "6",
                title = "ডেঙ্গু সচেতনতা ও ফ্রি মেডিকেল ক্যাম্প",
                date = "সম্পন্ন: সেপ্টেম্বর ২০২৬",
                isRedIcon = true,
                isCompleted = true
            ),
            ActivityItem(
                id = "7",
                title = "রমজান ইফতার ও খাদ্য বিতরণ",
                date = "সম্পন্ন: রমজান ১৪৪৭ হি.",
                isRedIcon = false,
                isCompleted = true
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("activities_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("activities_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NavyPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "কার্যক্রম",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Segmented Tab: [ আসন্ন ] [ সম্পন্ন ]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE5E9F2), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Tab 0: আসন্ন
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = if (selectedTab == 0) Color.White else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .then(
                                if (selectedTab == 0) Modifier.shadow(1.dp, RoundedCornerShape(10.dp))
                                else Modifier
                            )
                            .clickable { selectedTab = 0 }
                            .testTag("tab_upcoming"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "আসন্ন",
                            fontFamily = HindSiliguri,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (selectedTab == 0) NavyPrimary else SubtitleGray
                        )
                    }

                    // Tab 1: সম্পন্ন
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = if (selectedTab == 1) Color.White else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .then(
                                if (selectedTab == 1) Modifier.shadow(1.dp, RoundedCornerShape(10.dp))
                                else Modifier
                            )
                            .clickable { selectedTab = 1 }
                            .testTag("tab_completed"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "সম্পন্ন",
                            fontFamily = HindSiliguri,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (selectedTab == 1) NavyPrimary else SubtitleGray
                        )
                    }
                }
            }

            // Activities Cards List
            val currentList = if (selectedTab == 0) upcomingActivities else completedActivities

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(currentList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("activity_card_${item.id}"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icon Box (56dp)
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = if (item.isRedIcon) BloodRed else NavyPrimary,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (item.isRedIcon) Icons.Default.WaterDrop else Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            // Middle Info
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NavyPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.date,
                                    fontFamily = HindSiliguri,
                                    fontSize = 13.sp,
                                    color = SubtitleGray
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Badge: [ আসন্ন ] or [ সম্পন্ন ]
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (item.isCompleted) Color(0xFFE2E8F0) else Color(0xFFD1FAE5),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (item.isCompleted) "সম্পন্ন" else "আসন্ন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (item.isCompleted) SubtitleGray else SuccessGreen
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Bottom Full-Width Button: "স্বেচ্ছাসেবক হিসেবে নাম দিন"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = {
                        showVolunteerDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("volunteer_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "স্বেচ্ছাসেবক হিসেবে নাম দিন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }

    // Volunteer Registration Dialog
    if (showVolunteerDialog) {
        AlertDialog(
            onDismissRequest = { showVolunteerDialog = false },
            title = {
                Text(
                    text = "স্বেচ্ছাসেবক নিবন্ধন",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NavyPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "আসন্ন কার্যক্রমে স্বেচ্ছাসেবা প্রদানে আপনার নাম ও মোবাইল নম্বর দিন:",
                        fontFamily = HindSiliguri,
                        fontSize = 13.sp,
                        color = SubtitleGray
                    )
                    OutlinedTextField(
                        value = volunteerName,
                        onValueChange = { volunteerName = it },
                        placeholder = { Text("আপনার নাম", fontFamily = HindSiliguri) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                    OutlinedTextField(
                        value = volunteerPhone,
                        onValueChange = { volunteerPhone = it },
                        placeholder = { Text("মোবাইল নম্বর", fontFamily = HindSiliguri) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showVolunteerDialog = false
                        Toast.makeText(context, "স্বেচ্ছাসেবক হিসেবে নিবন্ধনের জন্য ধন্যবাদ!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "নিবন্ধন করুন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showVolunteerDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = SubtitleGray)
                ) {
                    Text(text = "বাতিল", fontFamily = HindSiliguri)
                }
            }
        )
    }
}
