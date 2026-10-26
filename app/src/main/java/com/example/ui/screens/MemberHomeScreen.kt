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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FirebaseManager
import com.example.data.MemberData
import com.example.model.MemberBottomTab
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
fun MemberHomeScreen(
    userName: String = "মুহাম্মদ আব্দুল্লাহ",
    member: MemberData? = null,
    onNavigateNoticeBoard: () -> Unit,
    onNavigateNoticeDetail: (String) -> Unit,
    onNavigateActivities: () -> Unit,
    onNavigateHadiya: () -> Unit,
    onNavigatePayOnline: () -> Unit,
    onNavigateMembers: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateIncomeExpense: () -> Unit = {},
    onNavigateBloodDonors: () -> Unit = {},
    onNavigateGallery: () -> Unit = {},
    onNavigateMeeting: () -> Unit = {},
    onNavigateNotifications: () -> Unit = {},
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var memberData by remember(member) {
        mutableStateOf(member ?: FirebaseManager.currentLoggedInMember)
    }

    LaunchedEffect(userName) {
        val data = FirebaseManager.getCurrentOrFetchMember(context, userName)
        if (data != null) {
            memberData = data
        }
    }

    var showBloodDonorsSheet by remember { mutableStateOf(false) }
    var showGalleryDialog by remember { mutableStateOf(false) }

    BackHandler {
        // Double press or toast to exit/logout
        onLogout()
    }

    Scaffold(
        modifier = Modifier.testTag("member_home_screen"),
        bottomBar = {
            MemberBottomNav(
                selectedTab = MemberBottomTab.HOME,
                onTabSelected = { tab ->
                    when (tab) {
                        MemberBottomTab.HOME -> { /* Already home */ }
                        MemberBottomTab.NOTICE -> onNavigateNoticeBoard()
                        MemberBottomTab.HADIYA -> onNavigateHadiya()
                        MemberBottomTab.MEMBERS -> onNavigateMembers()
                        MemberBottomTab.PROFILE -> onNavigateProfile()
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            // Navy Header Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = NavyPrimary,
                            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    Column {
                        // User Welcome Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "আসসালামু আলাইকুম",
                                    fontFamily = HindSiliguri,
                                    fontSize = 13.sp,
                                    color = Color(0xFFBAC7E5)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = memberData?.name ?: userName,
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }

                            // Notification Bell Icon in soft circular button
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2B3A70)),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = onNavigateNotifications,
                                    modifier = Modifier.testTag("member_bell_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Floating Due Hadiya Card (Matching 05.png)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("hadiya_due_card"),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(18.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 18.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "২০২৬ সালের মোট বকেয়া হাদিয়া",
                                        fontFamily = HindSiliguri,
                                        fontSize = 13.sp,
                                        color = SubtitleGray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "৳ ৫০০",
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 26.sp,
                                        color = BloodRed
                                    )
                                }

                                Button(
                                    onClick = onNavigatePayOnline,
                                    modifier = Modifier
                                        .height(44.dp)
                                        .testTag("pay_hadiya_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NavyPrimary,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "পরিশোধ করুন",
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Body Content
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    // 8 Quick Action Items Grid (4 columns x 2 rows, Unlocked)
                    // Row 1: হাদিয়া, নোটিশ, কার্যক্রম, সদস্য
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            title = "হাদিয়া",
                            icon = Icons.Default.CreditCard,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateHadiya,
                            tag = "member_action_hadiya"
                        )
                        QuickActionButton(
                            title = "নোটিশ",
                            icon = Icons.Default.Description,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateNoticeBoard,
                            tag = "member_action_notices"
                        )
                        QuickActionButton(
                            title = "কার্যক্রম",
                            icon = Icons.Default.CalendarMonth,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateActivities,
                            tag = "member_action_activities"
                        )
                        QuickActionButton(
                            title = "সদস্য",
                            icon = Icons.Default.Group,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateMembers,
                            tag = "member_action_members"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 2: রক্তদাতা, আয়-ব্যয়, মিটিং, গ্যালারি
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            title = "রক্তদাতা",
                            icon = Icons.Default.WaterDrop,
                            iconBg = SoftRedBg,
                            iconTint = BloodRed,
                            isLocked = false,
                            onClick = onNavigateBloodDonors,
                            tag = "member_action_donors"
                        )
                        QuickActionButton(
                            title = "আয়-ব্যয়",
                            icon = Icons.Default.BarChart,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateIncomeExpense,
                            tag = "member_action_expenses"
                        )
                        QuickActionButton(
                            title = "মিটিং",
                            icon = Icons.Default.Check,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateMeeting,
                            tag = "member_action_meeting"
                        )
                        QuickActionButton(
                            title = "গ্যালারি",
                            icon = Icons.Default.PhotoLibrary,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateGallery,
                            tag = "member_action_gallery"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // "সর্বশেষ নোটিশ" Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "সর্বশেষ নোটিশ",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NavyPrimary
                        )

                        Text(
                            text = "সব দেখুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = NavyPrimary,
                            modifier = Modifier
                                .clickable(onClick = onNavigateNoticeBoard)
                                .padding(vertical = 4.dp)
                                .testTag("member_view_all_notices")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notice Card (Matching 05.png) - Tapping opens Notice Detail
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateNoticeDetail("1") }
                            .testTag("member_latest_notice_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "৫ অক্টোবর ২০২৬",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = SubtitleGray
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "মাসিক সাধারণ সভার নোটিশ",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NavyPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "সকল সদস্যকে সভায় উপস্থিত থাকার জন্য অনুরোধ করা হলো।",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = SubtitleGray,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Blood Donors Bottom Sheet
    if (showBloodDonorsSheet) {
        BloodDonorsBottomSheet(onDismiss = { showBloodDonorsSheet = false })
    }

    // Gallery Bottom Sheet
    if (showGalleryDialog) {
        GalleryBottomSheet(onDismiss = { showGalleryDialog = false })
    }


}
