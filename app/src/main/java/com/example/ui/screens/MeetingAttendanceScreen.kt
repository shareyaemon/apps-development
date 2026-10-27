package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.data.FirebaseManager
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HindSiliguri

private data class MeetingItem(
    val title: String,
    val date: String,
    val statusText: String,
    val statusBg: Color,
    val statusTextColor: Color
)

private data class ResolutionItem(
    val title: String,
    val date: String,
    val summary: String,
    val status: String
)

@Composable
fun MeetingAttendanceScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: হাজিরা, 1: রেজুলেশন

    BackHandler {
        onNavigateBack()
    }

    var meetings by remember { mutableStateOf<List<MeetingItem>>(emptyList()) }
    var resolutions by remember { mutableStateOf<List<ResolutionItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val db = FirebaseManager.getFirestore(context)
            if (db != null) {
                val meetSnap = db.collection("meetings").get().await()
                if (!meetSnap.isEmpty) {
                    meetings = meetSnap.documents.map { doc ->
                        val status = doc.getString("status") ?: "উপস্থিত"
                        val (statusText, bg, fg) = when {
                            status.contains("অনুপস্থিত") -> Triple("✕ অনুপস্থিত", Color(0xFFFEE2E2), Color(0xFFB91C1C))
                            status.contains("ছুটি") -> Triple("− ছুটি", Color(0xFFFEF3C7), Color(0xFF92400E))
                            else -> Triple("✓ উপস্থিত", Color(0xFFDCFCE7), Color(0xFF166534))
                        }
                        MeetingItem(
                            title = doc.getString("title") ?: "সাধারণ সভা",
                            date = doc.getString("date") ?: "২০২৬",
                            statusText = statusText,
                            statusBg = bg,
                            statusTextColor = fg
                        )
                    }
                } else {
                    meetings = emptyList()
                }

                val resSnap = db.collection("resolutions").get().await()
                if (!resSnap.isEmpty) {
                    resolutions = resSnap.documents.map { doc ->
                        ResolutionItem(
                            title = doc.getString("title") ?: "রেজুলেশন",
                            date = doc.getString("date") ?: "২০২৬",
                            summary = doc.getString("summary") ?: "",
                            status = doc.getString("status") ?: "অনুমোদিত"
                        )
                    }
                } else {
                    resolutions = emptyList()
                }
            }
        } catch (_: Exception) {
            meetings = emptyList()
            resolutions = emptyList()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier.testTag("meeting_attendance_screen"),
        containerColor = Color(0xFFF4F7FC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FC))
                .padding(innerPadding)
        ) {
            // ১. সাদা হেডার: বাঁয়ে ব্যাক তীর, "মিটিং হাজিরা ও রেজুলেশন" ২২sp Bold নেভি (#1B2559)
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
                            .testTag("meeting_back_button")
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
                        text = "মিটিং হাজিরা ও রেজুলেশন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1B2559)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ২. সেগমেন্টেড ট্যাব: "হাজিরা" ও "রেজুলেশন"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        // ট্যাব ১: হাজিরা
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .then(
                                    if (selectedTab == 0) {
                                        Modifier
                                            .shadow(2.dp, RoundedCornerShape(12.dp))
                                            .background(Color.White, RoundedCornerShape(12.dp))
                                    } else {
                                        Modifier
                                    }
                                )
                                .clickable { selectedTab = 0 }
                                .testTag("tab_attendance"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "হাজিরা",
                                fontFamily = HindSiliguri,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 15.sp,
                                color = if (selectedTab == 0) Color(0xFF1B2559) else Color(0xFF64748B)
                            )
                        }

                        // ট্যাব ২: রেজুলেশন
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .then(
                                    if (selectedTab == 1) {
                                        Modifier
                                            .shadow(2.dp, RoundedCornerShape(12.dp))
                                            .background(Color.White, RoundedCornerShape(12.dp))
                                    } else {
                                        Modifier
                                    }
                                )
                                .clickable { selectedTab = 1 }
                                .testTag("tab_resolution"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "রেজুলেশন",
                                fontFamily = HindSiliguri,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 15.sp,
                                color = if (selectedTab == 1) Color(0xFF1B2559) else Color(0xFF64748B)
                            )
                        }
                    }
                }

                if (selectedTab == 0) {
                    // === হাজিরা ভিউ ===
                    // ৩. শীর্ষ নেভি কার্ড: "২০২৬ সালে উপস্থিতি", "৮ / ১০ সভা", ডানে ৮০% সার্কুলার প্রগ্রেস
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("attendance_summary_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2559)),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 22.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val totalMeetings = meetings.size
                            val presentMeetings = meetings.count { it.statusText.contains("উপস্থিত") }
                            val attendancePercentage = if (totalMeetings > 0) (presentMeetings * 100 / totalMeetings) else 0
                            val sweepAngle = attendancePercentage * 3.6f

                            Column {
                                Text(
                                    text = "২০২৬ সালে উপস্থিতি",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Color(0xFF93C5FD)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${FirebaseManager.toBengaliDigits(presentMeetings.toString())} / ${FirebaseManager.toBengaliDigits(totalMeetings.toString())} সভা",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    color = Color.White
                                )
                            }

                            // ডানে সার্কুলার প্রগ্রেস বার
                            Box(
                                modifier = Modifier.size(68.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val strokeWidth = 6.dp.toPx()
                                    // Background Track
                                    drawCircle(
                                        color = Color.White.copy(alpha = 0.15f),
                                        style = Stroke(width = strokeWidth)
                                    )
                                    // Progress Arc
                                    drawArc(
                                        color = Color(0xFF06B6D4),
                                        startAngle = -90f,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                    )
                                }

                                Text(
                                    text = "${FirebaseManager.toBengaliDigits(attendancePercentage.toString())}%",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // ৪. মিটিং তালিকা সাদা কার্ড (১৮dp কর্নার)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("meetings_list_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF1B2559),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        } else if (meetings.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "বর্তমানে কোনো সভার হাজিরা তথ্য নেই",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                            meetings.forEachIndexed { index, meeting ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = meeting.title,
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp,
                                            color = Color(0xFF1B2559)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = meeting.date,
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 13.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }

                                    // ব্যাজ
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(meeting.statusBg)
                                            .padding(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = meeting.statusText,
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = meeting.statusTextColor
                                        )
                                    }
                                }

                                if (index < meetings.size - 1) {
                                    HorizontalDivider(
                                        color = Color(0xFFE2E8F0),
                                        thickness = 0.8.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                        }
                    }
                }

                    // ৫. নিচে বাটন কার্ড: "সর্বশেষ সভার রেজুলেশন দেখুন"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTab = 1 }
                            .testTag("view_latest_resolution_button"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE)),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = Color(0xFF1B2559),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "সর্বশেষ সভার রেজুলেশন দেখুন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1B2559)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color(0xFF1B2559),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                } else {
                    // === রেজুলেশন ভিউ ===
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("resolutions_list_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFF1B2559),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        } else if (resolutions.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "বর্তমানে কোনো রেজুলেশন তথ্য নেই",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                            resolutions.forEachIndexed { index, res ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            Toast.makeText(context, "${res.title} ফাইল শীঘ্রই পাওয়া যাবে", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 16.dp, vertical = 14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = res.title,
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp,
                                            color = Color(0xFF1B2559),
                                            modifier = Modifier.weight(1f)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFDCFCE7))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = res.status,
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 12.sp,
                                                color = Color(0xFF166534)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Text(
                                        text = res.date,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = res.summary,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 20.sp
                                    )
                                }

                                if (index < resolutions.size - 1) {
                                    HorizontalDivider(
                                        color = Color(0xFFE2E8F0),
                                        thickness = 0.8.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
