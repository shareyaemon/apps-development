package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HindSiliguri

private data class NotificationItemData(
    val id: String,
    val title: String,
    val time: String,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val isUnread: Boolean
)

@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onNavigateBack()
    }

    // অপঠিত স্ট্যাটাস ট্র্যাকিং (ডিফল্টে প্রথম ৩টি অপঠিত)
    var unreadSet by remember { mutableStateOf(setOf("1", "2", "3")) }

    // "আজ" বিভাগের আইটেম
    val todayItems = remember {
        listOf(
            NotificationItemData(
                id = "1",
                title = "নতুন নোটিশ: মাসিক সাধারণ সভা",
                time = "২ ঘণ্টা আগে",
                icon = Icons.Default.Campaign,
                iconBg = Color(0xFFE0F2FE),
                iconTint = Color(0xFF0284C7),
                isUnread = true
            ),
            NotificationItemData(
                id = "2",
                title = "হাদিয়া বকেয়া: ৩ মাসের ৳৫০০",
                time = "৫ ঘণ্টা আগে",
                icon = Icons.Default.CreditCard,
                iconBg = Color(0xFFFEE2E2),
                iconTint = Color(0xFFB91C1C),
                isUnread = true
            )
        )
    }

    // "গতকাল" বিভাগের আইটেম
    val yesterdayItems = remember {
        listOf(
            NotificationItemData(
                id = "3",
                title = "O+ রক্তের জরুরি অনুরোধ",
                time = "গতকাল, সন্ধ্যা ৬:৪০",
                icon = Icons.Default.WaterDrop,
                iconBg = Color(0xFFFEE2E2),
                iconTint = Color(0xFFDC2626),
                isUnread = true
            ),
            NotificationItemData(
                id = "4",
                title = "আপনার আবেদন অনুমোদিত হয়েছে",
                time = "গতকাল, সকাল ১০:৩০",
                icon = Icons.Default.CheckCircle,
                iconBg = Color(0xFFDCFCE7),
                iconTint = Color(0xFF16A34A),
                isUnread = false
            )
        )
    }

    // "আগের" বিভাগের আইটেম
    val earlierItems = remember {
        listOf(
            NotificationItemData(
                id = "5",
                title = "কবরস্থান পরিষ্কার কর্মসূচির স্মরণিকা",
                time = "১২ অক্টোবর",
                icon = Icons.Default.Groups,
                iconBg = Color(0xFFFEF3C7),
                iconTint = Color(0xFFD97706),
                isUnread = false
            ),
            NotificationItemData(
                id = "6",
                title = "জুন মাসের হাদিয়ার রসিদ প্রস্তুত",
                time = "০৮ অক্টোবর",
                icon = Icons.Default.Description,
                iconBg = Color(0xFFF3E8FF),
                iconTint = Color(0xFF7E22CE),
                isUnread = false
            )
        )
    }

    Scaffold(
        modifier = Modifier.testTag("notifications_screen"),
        containerColor = Color(0xFFF4F7FC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FC))
                .padding(innerPadding)
        ) {
            // ১. সাদা হেডার: ব্যাক তীর ও শিরোনাম "নোটিফিকেশন" (২২sp Bold), ডানে "সব পড়া হয়েছে" লিংক
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("notifications_back_button")
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
                            text = "নোটিফিকেশন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF1B2559),
                            modifier = Modifier.testTag("notifications_title")
                        )
                    }

                    // ডানে "সব পড়া হয়েছে" লিংক
                    Text(
                        text = "সব পড়া হয়েছে",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFF1B2559),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                unreadSet = emptySet()
                                Toast.makeText(
                                    context,
                                    "সকল নোটিফিকেশন পঠিত হিসেবে চিহ্নিত করা হয়েছে",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("mark_all_read_link")
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            // ২. স্ক্রলযোগ্য সেকশনভিত্তিক নোটিফিকেশন তালিকা
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // ১. "আজ" সেকশন
                NotificationSection(
                    sectionTitle = "আজ",
                    items = todayItems,
                    unreadSet = unreadSet,
                    onItemClick = { id ->
                        unreadSet = unreadSet - id
                    }
                )

                // ২. "গতকাল" সেকশন
                NotificationSection(
                    sectionTitle = "গতকাল",
                    items = yesterdayItems,
                    unreadSet = unreadSet,
                    onItemClick = { id ->
                        unreadSet = unreadSet - id
                    }
                )

                // ৩. "আগের" সেকশন
                NotificationSection(
                    sectionTitle = "আগের",
                    items = earlierItems,
                    unreadSet = unreadSet,
                    onItemClick = { id ->
                        unreadSet = unreadSet - id
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun NotificationSection(
    sectionTitle: String,
    items: List<NotificationItemData>,
    unreadSet: Set<String>,
    onItemClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = sectionTitle,
            fontFamily = HindSiliguri,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF1B2559),
            modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, item ->
                    val isUnread = unreadSet.contains(item.id)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item.id) }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                            .testTag("notification_row_${item.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // বাঁয়ে রঙিন গোল আইকন
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(item.iconBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = item.iconTint,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // মাঝে শিরোনাম ও সময়
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = item.title,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.time,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        // অপঠিত আইটেমের (প্রথম তিনটি) ডানে নেভি ডট
                        if (isUnread) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1B2559))
                                    .testTag("unread_dot_${item.id}")
                            )
                        }
                    }

                    if (index < items.size - 1) {
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
}
