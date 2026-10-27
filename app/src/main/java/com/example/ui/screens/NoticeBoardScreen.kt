package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.model.MemberBottomTab
import com.example.model.NoticeItem
import com.example.ui.components.MemberBottomNav
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BloodRed
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SoftRedBg
import com.example.ui.theme.SoftSkyBlue
import com.example.ui.theme.SubtitleGray

@Composable
fun NoticeBoardScreen(
    onNavigateHome: () -> Unit,
    onNavigateNoticeDetail: (String) -> Unit,
    onNavigateHadiya: () -> Unit,
    onNavigateMembers: () -> Unit,
    onNavigateProfile: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("সব") }

    BackHandler {
        onNavigateHome()
    }

    val filterOptions = listOf("সব", "জরুরি", "সভা", "অনুষ্ঠান")

    val context = LocalContext.current
    var notices by remember { mutableStateOf<List<NoticeItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val registration = FirebaseManager.listenToNotices(
            context = context,
            useFallback = false,
            onSuccess = { list ->
                notices = list
                isLoading = false
            },
            onError = {
                notices = emptyList()
                isLoading = false
            }
        )
        onDispose {
            registration?.remove()
        }
    }

    val filteredNotices = when (selectedFilter) {
        "সব" -> notices
        "জরুরি" -> notices.filter { it.tag == "জরুরি" }
        "সভা" -> notices.filter { it.title.contains("সভা") || it.tag == "সভা" }
        "অনুষ্ঠান" -> notices.filter { it.tag == "অনুষ্ঠান" || it.title.contains("মাহফিল") }
        else -> notices
    }

    Scaffold(
        modifier = Modifier.testTag("notice_board_screen"),
        bottomBar = {
            MemberBottomNav(
                selectedTab = MemberBottomTab.NOTICE,
                onTabSelected = { tab ->
                    when (tab) {
                        MemberBottomTab.HOME -> onNavigateHome()
                        MemberBottomTab.NOTICE -> { /* Already on notice */ }
                        MemberBottomTab.HADIYA -> onNavigateHadiya()
                        MemberBottomTab.MEMBERS -> onNavigateMembers()
                        MemberBottomTab.PROFILE -> onNavigateProfile()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            // Top App Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "নোটিশ বোর্ড",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NavyPrimary
                )
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) NavyPrimary else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NavyPrimary else CardBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 18.dp, vertical = 7.dp)
                            .testTag("filter_chip_$filter")
                    ) {
                        Text(
                            text = filter,
                            fontFamily = HindSiliguri,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White else NavyPrimary
                        )
                    }
                }
            }

            // Notice Cards List
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NavyPrimary)
                }
            } else if (filteredNotices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "এখনো কোনো নোটিশ নেই",
                        fontFamily = HindSiliguri,
                        fontSize = 15.sp,
                        color = SubtitleGray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredNotices) { notice ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateNoticeDetail(notice.id) }
                            .testTag("notice_item_${notice.id}"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Top Tag & Date Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Tag Badge
                                val (tagBg, tagColor) = when (notice.tag) {
                                    "জরুরি" -> Pair(SoftRedBg, BloodRed)
                                    "অনুষ্ঠান" -> Pair(SoftSkyBlue, NavyPrimary)
                                    "হাদিয়া" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
                                    else -> Pair(Color(0xFFE0F2FE), Color(0xFF0284C7))
                                }

                                Box(
                                    modifier = Modifier
                                        .background(tagBg, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = notice.tag,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = tagColor
                                    )
                                }

                                Text(
                                    text = notice.date,
                                    fontFamily = HindSiliguri,
                                    fontSize = 13.sp,
                                    color = SubtitleGray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = notice.title,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NavyPrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = notice.description,
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = SubtitleGray,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
}


