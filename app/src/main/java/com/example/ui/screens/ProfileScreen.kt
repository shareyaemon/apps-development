package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.FirebaseManager
import com.example.data.MemberData
import com.example.model.MemberBottomTab
import com.example.ui.components.MemberBottomNav
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtitleGray
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    userName: String = "মুহাম্মদ আব্দুল্লাহ",
    member: MemberData? = null,
    onNavigateHome: () -> Unit,
    onTabSelected: (MemberBottomTab) -> Unit,
    onLogout: () -> Unit,
    onNavigateSettings: () -> Unit = {},
    onNavigateHadiyaRecord: () -> Unit = {},
    onNavigateMeetingAttendance: () -> Unit = {},
    onNavigateBloodDonationStatus: () -> Unit = {},
    onNavigateDigitalIdCard: () -> Unit = {},
    onNavigateAdminPanel: () -> Unit = {},
    onMemberUpdated: (MemberData) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var memberData by remember(member) {
        mutableStateOf(member ?: FirebaseManager.currentLoggedInMember)
    }
    var showEditBloodGroupDialog by remember { mutableStateOf(false) }
    var isSavingBloodGroup by remember { mutableStateOf(false) }

    LaunchedEffect(Unit, userName) {
        val fetched = FirebaseManager.getCurrentOrFetchMember(context, userName)
        if (fetched != null) {
            memberData = fetched
            onMemberUpdated(fetched)
        }
    }

    BackHandler {
        onNavigateHome()
    }

    Scaffold(
        modifier = Modifier.testTag("profile_screen"),
        bottomBar = {
            MemberBottomNav(
                selectedTab = MemberBottomTab.PROFILE,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            // ১. সাদা হেডার: শিরোনাম "প্রোফাইল" (২২sp Bold), ডানে সেটিংস আইকন
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "প্রোফাইল",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = NavyPrimary,
                        modifier = Modifier.testTag("profile_title")
                    )

                    IconButton(
                        onClick = onNavigateSettings,
                        modifier = Modifier.testTag("profile_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "সেটিংস",
                            tint = NavyPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))

                    // ২. ID কার্ড: উচ্চতা প্রায় ১৮০dp, ব্যাকগ্রাউন্ড গ্রেডিয়েন্ট #1B2559 থেকে #2471A3 (ডানে নিচে হালকা বৃত্তাকার আভা)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_id_card"),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF1B2559),
                                            Color(0xFF2471A3)
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset.Infinite
                                    )
                                )
                        ) {
                            // ডানে নিচে হালকা বৃত্তাকার আভা
                            Canvas(modifier = Modifier.matchParentSize()) {
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.08f),
                                    radius = size.width * 0.45f,
                                    center = Offset(size.width * 0.95f, size.height * 0.95f)
                                )
                                drawCircle(
                                    color = Color(0xFF29B6F0).copy(alpha = 0.12f),
                                    radius = size.width * 0.30f,
                                    center = Offset(size.width * 0.88f, size.height * 0.85f)
                                )
                            }

                            Column(modifier = Modifier.fillMaxWidth()) {
                                // উপরে সারি: উপরে বাঁয়ে সংঘের আসল লোগো (৪৪dp), উপরে ডানে সাদা bold "ID: HF-2026-042"
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.club_logo),
                                        contentDescription = "সংঘের লোগো",
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Fit
                                    )

                                    Text(
                                        text = "ID: ${memberData?.memberId ?: "HF-2026-001"}",
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }

                                // মাঝে সারি: মাঝে বাঁয়ে ৯০dp গোল অবতার (সাদা বর্ডার, হালকা নীল #E6F5FD ভেতর, নেভি ব্যক্তি আইকন),
                                // অবতারের ডানে নাম "মুহাম্মদ আব্দুল্লাহ" (২২sp bold সাদা),
                                // তার নিচে হালকা সবুজ #DCFCE7 ব্যাজে গাঢ় সবুজ "সক্রিয় সদস্য",
                                // তার নিচে রক্তের গ্রুপ
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // ৯০dp গোল অবতার
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE6F5FD))
                                            .border(BorderStroke(2.5.dp, Color.White), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "অবতার",
                                            tint = Color(0xFF1B2559),
                                            modifier = Modifier.size(52.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    // অবতারের ডানে তথ্য
                                    Column(
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        // নাম
                                        Text(
                                            text = memberData?.name?.ifBlank { userName } ?: userName.ifBlank { "মুহাম্মদ আব্দুল্লাহ" },
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp,
                                            color = Color.White,
                                            lineHeight = 26.sp
                                        )

                                        Spacer(modifier = Modifier.height(5.dp))

                                        // হালকা সবুজ #DCFCE7 ব্যাজে গাঢ় সবুজ স্ট্যাটাস
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFDCFCE7))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (memberData?.role == "super_admin") "সুপার অ্যাডমিন" else "সক্রিয় সদস্য",
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color(0xFF166534)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(5.dp))

                                        // রক্তের গ্রুপ ও ছোট সম্পাদনা বাটন
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = memberData?.bloodGroupDisplay ?: "রক্তের গ্রুপ: যোগ করা হয়নি",
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                color = Color.White
                                            )

                                            // ছোট "সম্পাদনা" বাটন
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color.White.copy(alpha = 0.22f))
                                                    .clickable {
                                                        showEditBloodGroupDialog = true
                                                    }
                                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                                                    .testTag("edit_blood_group_button"),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "রক্তের গ্রুপ সম্পাদনা",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(11.dp)
                                                    )
                                                    Text(
                                                        text = "সম্পাদনা",
                                                        fontFamily = HindSiliguri,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 11.sp,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // কার্ডের একেবারে নিচে গাঢ় ব্যান্ডে বাঁয়ে যোগদানের তারিখ (বাংলা সংখ্যায়), ডানে পুরো ফোন নম্বর
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0x59000000))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    val phoneFull = if (!memberData?.mobile.isNullOrBlank()) {
                                        FirebaseManager.toBengaliDigits(memberData!!.mobile)
                                    } else {
                                        "০১৭১২৩৪৫৬৭৮"
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "যোগদান: ${memberData?.joinDate ?: "২০২৬"}",
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 13.sp,
                                            color = Color.White.copy(alpha = 0.95f)
                                        )

                                        Text(
                                            text = "ফোন: $phoneFull",
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 13.sp,
                                            color = Color.White.copy(alpha = 0.95f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ৩. "মেম্বার মেনু" শিরোনাম ও নিচে সাদা কার্ডে ঠিক ৫টি সারি
                item {
                    Text(
                        text = "মেম্বার মেনু",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("member_menu_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // (ক) আমার হাদিয়া রেকর্ড (হালকা নীল বক্স, কার্ড আইকন)
                            MemberMenuRowItem(
                                title = "আমার হাদিয়া রেকর্ড",
                                icon = Icons.Default.CreditCard,
                                boxColor = Color(0xFFE0F2FE),
                                iconColor = Color(0xFF0284C7),
                                testTag = "menu_hadiya_record",
                                onClick = onNavigateHadiyaRecord
                            )

                            HorizontalDivider(
                                color = CardBorder.copy(alpha = 0.5f),
                                thickness = 0.6.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            // (খ) মিটিং হাজিরা ও রেজুলেশন (হালকা বেগুনি বক্স, ডকুমেন্ট আইকন)
                            MemberMenuRowItem(
                                title = "মিটিং হাজিরা ও রেজুলেশন",
                                icon = Icons.Default.Description,
                                boxColor = Color(0xFFF3E8FF),
                                iconColor = Color(0xFF7E22CE),
                                testTag = "menu_meeting_attendance",
                                onClick = onNavigateMeetingAttendance
                            )

                            HorizontalDivider(
                                color = CardBorder.copy(alpha = 0.5f),
                                thickness = 0.6.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            // (গ) রক্তদানের তথ্য ও স্ট্যাটাস (হালকা লাল বক্স, রক্তের ফোঁটা)
                            MemberMenuRowItem(
                                title = "রক্তদানের তথ্য ও স্ট্যাটাস",
                                icon = Icons.Default.WaterDrop,
                                boxColor = Color(0xFFFEE2E2),
                                iconColor = Color(0xFFDC2626),
                                testTag = "menu_blood_donation",
                                onClick = onNavigateBloodDonationStatus
                            )

                            HorizontalDivider(
                                color = CardBorder.copy(alpha = 0.5f),
                                thickness = 0.6.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            // (ঘ) আমার ডিজিটাল আইডি কার্ড (PDF) (হালকা হলুদ বক্স, ডাউনলোড আইকন)
                            MemberMenuRowItem(
                                title = "আমার ডিজিটাল আইডি কার্ড (PDF)",
                                icon = Icons.Default.Download,
                                boxColor = Color(0xFFFEF9C3),
                                iconColor = Color(0xFFCA8A04),
                                testTag = "menu_digital_id_card",
                                onClick = onNavigateDigitalIdCard
                            )

                            HorizontalDivider(
                                color = CardBorder.copy(alpha = 0.5f),
                                thickness = 0.6.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            // (ঙ) পাসওয়ার্ড পরিবর্তন (ধূসর বক্স, তালা)
                            MemberMenuRowItem(
                                title = "পাসওয়ার্ড পরিবর্তন",
                                icon = Icons.Default.Lock,
                                boxColor = Color(0xFFF1F5F9),
                                iconColor = Color(0xFF475569),
                                testTag = "menu_change_password",
                                onClick = onNavigateSettings
                            )

                            // (চ) অ্যাডমিন প্যানেল (শুধু super_admin, president, secretary, treasurer রোলের জন্য)
                            if (memberData?.role in listOf("super_admin", "president", "secretary", "treasurer")) {
                                HorizontalDivider(
                                    color = CardBorder.copy(alpha = 0.5f),
                                    thickness = 0.6.dp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                MemberMenuRowItem(
                                    title = "অ্যাডমিন প্যানেল",
                                    icon = Icons.Default.AdminPanelSettings,
                                    boxColor = Color(0xFFE0E7FF),
                                    iconColor = NavyPrimary,
                                    testTag = "menu_admin_panel",
                                    onClick = onNavigateAdminPanel
                                )
                            }
                        }
                    }
                }

                // ৪. "লগ আউট" বাটন: সাদা ব্যাকগ্রাউন্ড, ১.৫dp গাঢ় লাল (#B91C1C) বর্ডার, লাল bold লেখা ও আইকন, কর্নার ১৬dp, উচ্চতা ৪৮dp
                item {
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("profile_logout_button"),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, Color(0xFFB91C1C)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFFB91C1C)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "লগ আউট",
                            tint = Color(0xFFB91C1C),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "লগ আউট",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFB91C1C)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // রক্তের গ্রুপ সম্পাদনা ডায়ালগ (সাদা কার্ডে ৮টি গ্রিড বাটন)
    if (showEditBloodGroupDialog) {
        val bloodGroupsRow1 = listOf("A+", "A−", "B+", "B−")
        val bloodGroupsRow2 = listOf("O+", "O−", "AB+", "AB−")

        fun isSameGroup(g1: String, g2: String): Boolean {
            val norm1 = g1.replace("−", "-").trim()
            val norm2 = g2.replace("−", "-").trim()
            return norm1.equals(norm2, ignoreCase = true)
        }

        var tempSelectedGroup by remember {
            mutableStateOf(memberData?.bloodGroup ?: "")
        }

        Dialog(onDismissRequest = {
            if (!isSavingBloodGroup) showEditBloodGroupDialog = false
        }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .testTag("edit_blood_group_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "রক্তের গ্রুপ সম্পাদনা",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "তালিকা থেকে সঠিক রক্তের গ্রুপটি নির্বাচন করুন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = SubtitleGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 1: A+, A−, B+, B−
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        bloodGroupsRow1.forEach { bg ->
                            val isSelected = isSameGroup(tempSelectedGroup, bg)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) NavyPrimary else Color.White)
                                    .border(
                                        BorderStroke(1.5.dp, if (isSelected) NavyPrimary else CardBorder),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { tempSelectedGroup = bg }
                                    .padding(vertical = 10.dp)
                                    .testTag("blood_group_chip_$bg"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = bg,
                                    fontFamily = HindSiliguri,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = if (isSelected) Color.White else NavyPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 2: O+, O−, AB+, AB−
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        bloodGroupsRow2.forEach { bg ->
                            val isSelected = isSameGroup(tempSelectedGroup, bg)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) NavyPrimary else Color.White)
                                    .border(
                                        BorderStroke(1.5.dp, if (isSelected) NavyPrimary else CardBorder),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { tempSelectedGroup = bg }
                                    .padding(vertical = 10.dp)
                                    .testTag("blood_group_chip_$bg"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = bg,
                                    fontFamily = HindSiliguri,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = if (isSelected) Color.White else NavyPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Dialog Actions (বাতিল ও সংরক্ষণ)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showEditBloodGroupDialog = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("cancel_blood_group_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, CardBorder),
                            enabled = !isSavingBloodGroup
                        ) {
                            Text(
                                text = "বাতিল",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = SubtitleGray
                            )
                        }

                        Button(
                            onClick = {
                                if (tempSelectedGroup.isBlank()) {
                                    Toast.makeText(context, "একটি রক্তের গ্রুপ নির্বাচন করুন", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isSavingBloodGroup = true
                                scope.launch {
                                    val result = FirebaseManager.updateMemberBloodGroup(
                                        context = context,
                                        newBloodGroup = tempSelectedGroup
                                    )
                                    isSavingBloodGroup = false
                                    if (result.isSuccess) {
                                        val updated = (memberData ?: FirebaseManager.currentLoggedInMember ?: MemberData()).copy(bloodGroup = tempSelectedGroup)
                                        memberData = updated
                                        FirebaseManager.currentLoggedInMember = updated
                                        onMemberUpdated(updated)
                                        showEditBloodGroupDialog = false
                                        Toast.makeText(context, "রক্তের গ্রুপ সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "সংরক্ষণ হয়নি, আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("save_blood_group_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NavyPrimary,
                                contentColor = Color.White
                            ),
                            enabled = !isSavingBloodGroup
                        ) {
                            if (isSavingBloodGroup) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "সংরক্ষণ",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberMenuRowItem(
    title: String,
    icon: ImageVector,
    boxColor: Color,
    iconColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // বাঁয়ে ৪০dp রঙিন গোল-কর্নার আইকন বক্স
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(boxColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // মাঝে লেখা (১৫sp Medium)
            Text(
                text = title,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = NavyPrimary
            )
        }

        // ডানে তীর
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = SubtitleGray,
            modifier = Modifier.size(14.dp)
        )
    }
}
