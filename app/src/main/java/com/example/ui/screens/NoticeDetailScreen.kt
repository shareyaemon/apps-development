package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FirebaseManager
import com.example.model.NoticeItem
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BloodRed
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SoftRedBg
import com.example.ui.theme.SoftSkyBlue
import com.example.ui.theme.SubtitleGray
import com.example.ui.theme.SuccessGreen

@Composable
fun NoticeDetailScreen(
    noticeId: String = "1",
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var isConfirmed by remember { mutableStateOf(false) }

    var notice by remember(noticeId) {
        mutableStateOf(
            FirebaseManager.defaultNotices.find { it.id == noticeId }
                ?: FirebaseManager.defaultNotices.first()
        )
    }

    LaunchedEffect(noticeId) {
        val fetched = FirebaseManager.getNoticeById(context, noticeId)
        if (fetched != null) {
            notice = fetched
        }
    }

    BackHandler {
        onNavigateBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("notice_detail_screen")
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
                        modifier = Modifier.testTag("notice_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NavyPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "নোটিশ",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 18.dp)
                    .navigationBarsPadding()
            ) {
                // Badge & Date Row
                val (tagBg, tagColor) = when (notice.tag) {
                    "জরুরি" -> Pair(SoftRedBg, BloodRed)
                    "অনুষ্ঠান" -> Pair(SoftSkyBlue, NavyPrimary)
                    "হাদিয়া" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
                    else -> Pair(Color(0xFFE0F2FE), Color(0xFF0284C7))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(tagBg, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = notice.tag.ifBlank { "সাধারণ" },
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = tagColor
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = notice.date,
                        fontFamily = HindSiliguri,
                        fontSize = 13.sp,
                        color = SubtitleGray
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                Text(
                    text = notice.title,
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = NavyPrimary,
                    lineHeight = 28.sp
                )

                if (notice.publisher.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "প্রকাশক: ${notice.publisher}",
                        fontFamily = HindSiliguri,
                        fontSize = 14.sp,
                        color = SubtitleGray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

                Spacer(modifier = Modifier.height(16.dp))

                // Body text
                Text(
                    text = notice.description,
                    fontFamily = HindSiliguri,
                    fontSize = 14.5.sp,
                    color = NavyPrimary,
                    lineHeight = 23.sp
                )

                val hasMeetingDetails = notice.meetingDate.isNotBlank() || notice.meetingTime.isNotBlank() || notice.meetingPlace.isNotBlank()
                if (hasMeetingDetails) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Details Card (Date, Time, Location)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            if (notice.meetingDate.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = NavyPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "তারিখ: ${notice.meetingDate}",
                                        fontFamily = HindSiliguri,
                                        fontSize = 14.sp,
                                        color = NavyPrimary
                                    )
                                }
                            }

                            if (notice.meetingTime.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = NavyPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "সময়: ${notice.meetingTime}",
                                        fontFamily = HindSiliguri,
                                        fontSize = 14.sp,
                                        color = NavyPrimary
                                    )
                                }
                            }

                            if (notice.meetingPlace.isNotBlank()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = NavyPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "স্থান: ${notice.meetingPlace}",
                                        fontFamily = HindSiliguri,
                                        fontSize = 14.sp,
                                        color = NavyPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f, fill = false))
                Spacer(modifier = Modifier.height(36.dp))

                // Bottom action button: "পড়েছি, উপস্থিত থাকব"
                Button(
                    onClick = {
                        isConfirmed = !isConfirmed
                        Toast.makeText(
                            context,
                            if (isConfirmed) "ধন্যবাদ! আপনার উপস্থিতি নিশ্চিত করা হয়েছে।" else "উপস্থিতি বাতিল করা হয়েছে।",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_attendance_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isConfirmed) SuccessGreen else NavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isConfirmed) "উপস্থিতি নিশ্চিত করা হয়েছে ✓" else "পড়েছি, উপস্থিত থাকব",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
