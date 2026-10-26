package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.window.DialogProperties
import com.example.data.AdminDashboardStats
import com.example.data.ApprovalResult
import com.example.data.DonationRequestItem
import com.example.data.FirebaseManager
import com.example.data.MemberData
import com.example.model.ApplicationItem
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
import kotlinx.coroutines.launch

@Composable
fun AdminDashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateNotifications: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var stats by remember { mutableStateOf(AdminDashboardStats()) }
    var pendingApplications by remember { mutableStateOf<List<ApplicationItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var processingAppId by remember { mutableStateOf<String?>(null) }
    var approvalResult by remember { mutableStateOf<ApprovalResult?>(null) }

    var pendingDonations by remember { mutableStateOf<List<DonationRequestItem>>(emptyList()) }
    var isDonationsLoading by remember { mutableStateOf(true) }
    var donationsErrorMessage by remember { mutableStateOf<String?>(null) }
    var processingDonationId by remember { mutableStateOf<String?>(null) }
    var donationListenerKey by remember { mutableStateOf(0) }

    var applicationListenerKey by remember { mutableStateOf(0) }
    var applicationsErrorMessage by remember { mutableStateOf<String?>(null) }

    var showNoticeManagementDialog by remember { mutableStateOf(false) }
    var noticesList by remember { mutableStateOf<List<NoticeItem>>(emptyList()) }
    var isNoticesLoading by remember { mutableStateOf(false) }
    var noticesErrorMessage by remember { mutableStateOf<String?>(null) }
    var noticeListenerKey by remember { mutableStateOf(0) }

    var showNoticeFormDialog by remember { mutableStateOf(false) }
    var editingNotice by remember { mutableStateOf<NoticeItem?>(null) }
    var noticeToDelete by remember { mutableStateOf<NoticeItem?>(null) }
    var isNoticeSaving by remember { mutableStateOf(false) }
    var isNoticeDeleting by remember { mutableStateOf(false) }

    var showMemberManagementDialog by remember { mutableStateOf(false) }
    var membersList by remember { mutableStateOf<List<MemberData>>(emptyList()) }
    var isMembersLoading by remember { mutableStateOf(false) }
    var membersErrorMessage by remember { mutableStateOf<String?>(null) }
    var memberListenerKey by remember { mutableStateOf(0) }
    var selectedMemberForDetail by remember { mutableStateOf<MemberData?>(null) }

    DisposableEffect(memberListenerKey) {
        isMembersLoading = true
        membersErrorMessage = null
        val registration = FirebaseManager.listenToMembers(
            context = context,
            onSuccess = { list ->
                membersList = list
                isMembersLoading = false
                membersErrorMessage = null
                if (list.isNotEmpty()) {
                    stats = stats.copy(totalMembers = FirebaseManager.toBengaliDigits(list.size.toString()))
                }
            },
            onError = { err ->
                isMembersLoading = false
                membersErrorMessage = err.message
            }
        )
        onDispose {
            registration?.remove()
        }
    }

    DisposableEffect(noticeListenerKey) {
        isNoticesLoading = true
        noticesErrorMessage = null
        val registration = FirebaseManager.listenToNotices(
            context = context,
            onSuccess = { list ->
                noticesList = list
                isNoticesLoading = false
                noticesErrorMessage = null
            },
            onError = { _ ->
                isNoticesLoading = false
                noticesErrorMessage = "নোটিশ লোড করতে ব্যর্থ হয়েছে"
            }
        )
        onDispose {
            registration?.remove()
        }
    }

    DisposableEffect(applicationListenerKey) {
        isLoading = true
        applicationsErrorMessage = null
        val registration = FirebaseManager.listenToPendingApplications(
            context = context,
            onSuccess = { list ->
                pendingApplications = list
                isLoading = false
                applicationsErrorMessage = null
                stats = stats.copy(pendingApplications = FirebaseManager.toBengaliDigits(list.size.toString()))
            },
            onError = { _ ->
                isLoading = false
                applicationsErrorMessage = "আবেদন লোড করতে ব্যর্থ হয়েছে, আবার চেষ্টা করুন"
            }
        )
        onDispose {
            registration?.remove()
        }
    }

    DisposableEffect(donationListenerKey) {
        isDonationsLoading = true
        donationsErrorMessage = null
        val registration = FirebaseManager.listenToPendingDonations(
            context = context,
            onSuccess = { list ->
                pendingDonations = list
                isDonationsLoading = false
                donationsErrorMessage = null
            },
            onError = { _ ->
                isDonationsLoading = false
                donationsErrorMessage = "রক্তদান অনুরোধ লোড করতে ব্যর্থ হয়েছে, আবার চেষ্টা করুন"
            }
        )
        onDispose {
            registration?.remove()
        }
    }

    fun refreshData() {
        scope.launch {
            val fetchedStats = FirebaseManager.getAdminDashboardStats(context)
            stats = fetchedStats.copy(pendingApplications = FirebaseManager.toBengaliDigits(pendingApplications.size.toString()))
        }
    }

    LaunchedEffect(Unit) {
        refreshData()
    }

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = Modifier.testTag("admin_dashboard_screen"),
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            // ১. নেভি হেডার (#1B2559): "অ্যাডমিন প্যানেল", সংঘের নাম, ডানে নোটিফিকেশন আইকন
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyPrimary)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("admin_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "ফিরে যান",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Column {
                            Text(
                                text = "অ্যাডমিন প্যানেল",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White,
                                modifier = Modifier.testTag("admin_panel_title")
                            )

                            Text(
                                text = "হিলফুল ফুজুল স্বর্ণটিলা যুব সংঘ",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    // ডানে নোটিফিকেশন আইকন (বৃত্তাকার কন্টেইনার)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF27336B))
                            .clickable {
                                onNavigateNotifications()
                            }
                            .testTag("admin_notification_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "নোটিফিকেশন",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ২. স্ক্রলযোগ্য কনটেন্ট
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(2.dp))

                    // চারটি কার্ড: মোট সদস্য, এ মাসের হাদিয়া, অপেক্ষমাণ আবেদন, বর্তমান জমা
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // সারি ১: মোট সদস্য ও এ মাসের হাদিয়া
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AdminStatCard(
                                title = "মোট সদস্য",
                                value = stats.totalMembers,
                                valueColor = NavyPrimary,
                                testTag = "stat_card_total_members",
                                modifier = Modifier.weight(1f)
                            )

                            AdminStatCard(
                                title = "এ মাসের হাদিয়া",
                                value = stats.thisMonthHadiya,
                                valueColor = Color(0xFF00796B),
                                testTag = "stat_card_this_month_hadiya",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // সারি ২: অপেক্ষমাণ আবেদন ও বর্তমান জমা
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AdminStatCard(
                                title = "অপেক্ষমাণ আবেদন",
                                value = stats.pendingApplications,
                                valueColor = Color(0xFFC2410C),
                                testTag = "stat_card_pending_applications",
                                modifier = Modifier.weight(1f)
                            )

                            AdminStatCard(
                                title = "বর্তমান জমা",
                                value = stats.currentBalance,
                                valueColor = NavyPrimary,
                                testTag = "stat_card_current_balance",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // ৩. "অনুমোদনের অপেক্ষায়" তালিকা
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "অনুমোদনের অপেক্ষায়",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NavyPrimary,
                            modifier = Modifier
                                .padding(start = 2.dp, bottom = 2.dp)
                                .testTag("pending_applications_title")
                        )

                        if (pendingApplications.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${FirebaseManager.toBengaliDigits(pendingApplications.size.toString())} টি আবেদন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFFD97706)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pending_applications_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                    ) {
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = NavyPrimary,
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        } else if (applicationsErrorMessage != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = applicationsErrorMessage ?: "আবেদন লোড করতে ব্যর্থ হয়েছে, আবার চেষ্টা করুন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = Color(0xFFDC2626)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = { applicationListenerKey++ },
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, NavyPrimary),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "পুনরায় চেষ্টা",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "আবার চেষ্টা করুন",
                                        fontFamily = HindSiliguri,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        } else if (pendingApplications.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "কোনো অপেক্ষমাণ আবেদন নেই",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = SubtitleGray
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 360.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                pendingApplications.forEachIndexed { index, app ->
                                    val isProcessing = processingAppId == app.id

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp)
                                            .testTag("pending_app_item_${app.id}"),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = app.fullName,
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                color = NavyPrimary
                                            )

                                            if (app.mobile.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "মোবাইল: ${app.mobile}",
                                                    fontFamily = HindSiliguri,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 13.sp,
                                                    color = SubtitleGray
                                                )
                                            }

                                            if (app.bloodGroup.isNotBlank()) {
                                                Text(
                                                    text = "রক্তের গ্রুপ: ${app.bloodGroup}",
                                                    fontFamily = HindSiliguri,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 12.sp,
                                                    color = Color(0xFFDC2626)
                                                )
                                            }
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // "অনুমোদন" বাটন (নেভি ফিল্ড)
                                            Button(
                                                onClick = {
                                                    processingAppId = app.id
                                                    scope.launch {
                                                        val res = FirebaseManager.approveApplication(context, app)
                                                        processingAppId = null
                                                        if (res.isSuccess) {
                                                            approvalResult = res.getOrNull()
                                                            pendingApplications = pendingApplications.filter { it.id != app.id }
                                                            stats = stats.copy(pendingApplications = FirebaseManager.toBengaliDigits(pendingApplications.size.toString()))
                                                        } else {
                                                            Toast.makeText(context, "অনুমোদন ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                },
                                                modifier = Modifier
                                                    .height(36.dp)
                                                    .testTag("approve_button_${app.id}"),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = NavyPrimary,
                                                    contentColor = Color.White
                                                ),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                                    horizontal = 12.dp,
                                                    vertical = 0.dp
                                                ),
                                                enabled = !isProcessing
                                            ) {
                                                if (isProcessing) {
                                                    CircularProgressIndicator(
                                                        color = Color.White,
                                                        strokeWidth = 2.dp,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                } else {
                                                    Text(
                                                        text = "অনুমোদন",
                                                        fontFamily = HindSiliguri,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp
                                                    )
                                                }
                                            }

                                            // "বাতিল" বাটন (লাল আউটলাইন)
                                            OutlinedButton(
                                                onClick = {
                                                    processingAppId = app.id
                                                    scope.launch {
                                                        FirebaseManager.rejectApplication(context, app.id)
                                                        processingAppId = null
                                                        pendingApplications = pendingApplications.filter { it.id != app.id }
                                                        stats = stats.copy(pendingApplications = FirebaseManager.toBengaliDigits(pendingApplications.size.toString()))
                                                        Toast.makeText(context, "আবেদন বাতিল করা হয়েছে", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier
                                                    .height(36.dp)
                                                    .testTag("reject_button_${app.id}"),
                                                shape = RoundedCornerShape(8.dp),
                                                border = BorderStroke(1.dp, Color(0xFFDC2626)),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = Color(0xFFDC2626)
                                                ),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                                    horizontal = 12.dp,
                                                    vertical = 0.dp
                                                ),
                                                enabled = !isProcessing
                                            ) {
                                                Text(
                                                    text = "বাতিল",
                                                    fontFamily = HindSiliguri,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFFDC2626)
                                                )
                                            }
                                        }
                                    }

                                    if (index < pendingApplications.lastIndex) {
                                        HorizontalDivider(
                                            color = CardBorder.copy(alpha = 0.6f),
                                            thickness = 0.8.dp,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ৪. "রক্তদান অনুরোধ" সেকশন (Firestore "donations" থেকে status == "pending", রিয়েল-টাইম)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "রক্তদান অনুরোধ",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NavyPrimary,
                            modifier = Modifier
                                .padding(start = 2.dp, bottom = 2.dp)
                                .testTag("blood_donation_requests_title")
                        )

                        if (pendingDonations.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFEE2E2))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${pendingDonations.size} টি অপেক্ষমাণ",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pending_donations_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                    ) {
                        if (isDonationsLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = NavyPrimary,
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        } else if (donationsErrorMessage != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = donationsErrorMessage ?: "রক্তদান অনুরোধ লোড করতে ব্যর্থ হয়েছে, আবার চেষ্টা করুন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = Color(0xFFDC2626)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = { donationListenerKey++ },
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, NavyPrimary),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "পুনরায় চেষ্টা",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "আবার চেষ্টা করুন",
                                        fontFamily = HindSiliguri,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        } else if (pendingDonations.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "কোনো অপেক্ষমাণ রক্তদান অনুরোধ নেই",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = SubtitleGray
                                )
                            }
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                pendingDonations.forEachIndexed { index, donation ->
                                    val isProcessing = processingDonationId == donation.id

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp)
                                            .testTag("pending_donation_item_${donation.id}"),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // বাঁয়ে রক্তের গ্রুপ বক্স
                                        val bgText = donation.bloodGroup.ifBlank { "—" }
                                        Box(
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFDC2626)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = bgText,
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = if (bgText.length > 2) 14.sp else 16.sp,
                                                color = Color.White
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        // মাঝের তথ্য
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = donation.name,
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = NavyPrimary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "আইডি: ${donation.memberId}",
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 12.sp,
                                                color = SubtitleGray
                                            )
                                            Text(
                                                text = "রক্তদানের তারিখ: ${donation.donationDateFormatted.ifBlank { "নির্দিষ্ট নেই" }}",
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 12.sp,
                                                color = Color(0xFF0284C7)
                                            )
                                        }

                                        // ডানে বাটনসমূহ
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // "অনুমোদন" বাটন
                                            Button(
                                                onClick = {
                                                    processingDonationId = donation.id
                                                    scope.launch {
                                                        val res = FirebaseManager.approveDonationRequest(context, donation)
                                                        processingDonationId = null
                                                        if (res.isSuccess) {
                                                            Toast.makeText(context, "রক্তদান অনুমোদন সফল হয়েছে", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            Toast.makeText(context, "অনুমোদন ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                },
                                                modifier = Modifier
                                                    .height(34.dp)
                                                    .testTag("approve_donation_${donation.id}"),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = NavyPrimary,
                                                    contentColor = Color.White
                                                ),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                                    horizontal = 10.dp,
                                                    vertical = 0.dp
                                                ),
                                                enabled = !isProcessing
                                            ) {
                                                if (isProcessing) {
                                                    CircularProgressIndicator(
                                                        color = Color.White,
                                                        strokeWidth = 2.dp,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                } else {
                                                    Text(
                                                        text = "অনুমোদন",
                                                        fontFamily = HindSiliguri,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }

                                            // "বাতিল" বাটন
                                            OutlinedButton(
                                                onClick = {
                                                    processingDonationId = donation.id
                                                    scope.launch {
                                                        val res = FirebaseManager.rejectDonationRequest(context, donation.id)
                                                        processingDonationId = null
                                                        if (res.isSuccess) {
                                                            Toast.makeText(context, "অনুরোধ বাতিল করা হয়েছে", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            Toast.makeText(context, "বাতিল করতে সমস্যা হয়েছে", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                },
                                                modifier = Modifier
                                                    .height(34.dp)
                                                    .testTag("reject_donation_${donation.id}"),
                                                shape = RoundedCornerShape(8.dp),
                                                border = BorderStroke(1.dp, Color(0xFFDC2626)),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = Color(0xFFDC2626)
                                                ),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                                    horizontal = 10.dp,
                                                    vertical = 0.dp
                                                ),
                                                enabled = !isProcessing
                                            ) {
                                                Text(
                                                    text = "বাতিল",
                                                    fontFamily = HindSiliguri,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp,
                                                    color = Color(0xFFDC2626)
                                                )
                                            }
                                        }
                                    }

                                    if (index < pendingDonations.lastIndex) {
                                        HorizontalDivider(
                                            color = CardBorder.copy(alpha = 0.6f),
                                            thickness = 0.8.dp,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ৫. "ব্যবস্থাপনা" সেকশন (৬টি মেনু কার্ড: সদস্য, নোটিশ, হাদিয়া এন্ট্রি, আয়-ব্যয়, কার্যক্রম, গ্যালারি)
                item {
                    Text(
                        text = "ব্যবস্থাপনা",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary,
                        modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // সারি ১: সদস্য, নোটিশ, হাদিয়া এন্ট্রি
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminManagementMenuCard(
                                title = "সদস্য",
                                icon = Icons.Default.People,
                                testTag = "admin_menu_members",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    showMemberManagementDialog = true
                                }
                            )

                            AdminManagementMenuCard(
                                title = "নোটিশ",
                                icon = Icons.Default.Description,
                                testTag = "admin_menu_notices",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    showNoticeManagementDialog = true
                                }
                            )

                            AdminManagementMenuCard(
                                title = "হাদিয়া এন্ট্রি",
                                icon = Icons.Default.CreditCard,
                                testTag = "admin_menu_hadiya_entry",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    Toast.makeText(context, "হাদিয়া এন্ট্রি", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        // সারি ২: আয়-ব্যয়, কার্যক্রম, গ্যালারি
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminManagementMenuCard(
                                title = "আয়-ব্যয়",
                                icon = Icons.Default.BarChart,
                                testTag = "admin_menu_finances",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    Toast.makeText(context, "আয়-ব্যয় ব্যবস্থাপনা", Toast.LENGTH_SHORT).show()
                                }
                            )

                            AdminManagementMenuCard(
                                title = "কার্যক্রম",
                                icon = Icons.Default.CalendarToday,
                                testTag = "admin_menu_activities",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    Toast.makeText(context, "কার্যক্রম ব্যবস্থাপনা", Toast.LENGTH_SHORT).show()
                                }
                            )

                            AdminManagementMenuCard(
                                title = "গ্যালারি",
                                icon = Icons.Default.Image,
                                testTag = "admin_menu_gallery",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    Toast.makeText(context, "গ্যালারি ব্যবস্থাপনা", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // ৫. অনুমোদনের ফলাফল ডায়ালগ (সদস্য আইডি ও অস্থায়ী পাসওয়ার্ড প্রদর্শন)
    approvalResult?.let { result ->
        Dialog(onDismissRequest = { approvalResult = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .testTag("approval_success_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "সফল",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(54.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "সদস্যপদ অনুমোদিত হয়েছে!",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = NavyPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "নতুন সদস্যের তথ্য Firebase Auth ও ডাটাবেজে যুক্ত হয়েছে।",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = SubtitleGray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // তথ্য বক্স
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(BorderStroke(1.dp, CardBorder), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoRow(label = "সদস্যের নাম", value = result.memberName)
                            InfoRow(label = "সদস্য আইডি", value = result.memberId, isBoldValue = true, valueColor = NavyPrimary)
                            InfoRow(label = "মোবাইল নম্বর", value = FirebaseManager.toBengaliDigits(result.mobile))
                            if (result.bloodGroup.isNotBlank()) {
                                InfoRow(label = "রক্তের গ্রুপ", value = result.bloodGroup)
                            }
                            InfoRow(
                                label = "অস্থায়ী পাসওয়ার্ড",
                                value = result.temporaryPassword,
                                isBoldValue = true,
                                valueColor = Color(0xFF00796B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // ক্লিপবোর্ডে কপি বাটন
                        OutlinedButton(
                            onClick = {
                                val clipData = "হিলফুল ফুজুল সদস্য লগইন তথ্য:\nনাম: ${result.memberName}\nআইডি: ${result.memberId}\nমোবাইল: ${result.mobile}\nপাসওয়ার্ড: ${result.temporaryPassword}"
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Member Credentials", clipData))
                                Toast.makeText(context, "আইডি ও পাসওয়ার্ড কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("copy_credentials_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, CardBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "কপি",
                                tint = NavyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "কপি করুন",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = NavyPrimary
                            )
                        }

                        // ঠিক আছে বাটন
                        Button(
                            onClick = { approvalResult = null },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("dismiss_approval_dialog_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NavyPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "ঠিক আছে",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showNoticeManagementDialog) {
        NoticeManagementDialog(
            notices = noticesList,
            isLoading = isNoticesLoading,
            errorMessage = noticesErrorMessage,
            onDismiss = { showNoticeManagementDialog = false },
            onAddNotice = {
                editingNotice = null
                showNoticeFormDialog = true
            },
            onEditNotice = { notice ->
                editingNotice = notice
                showNoticeFormDialog = true
            },
            onDeleteNotice = { notice ->
                noticeToDelete = notice
            },
            onRefresh = {
                noticeListenerKey++
            }
        )
    }

    if (showNoticeFormDialog) {
        NoticeFormDialog(
            notice = editingNotice,
            isSaving = isNoticeSaving,
            onDismiss = {
                if (!isNoticeSaving) {
                    showNoticeFormDialog = false
                    editingNotice = null
                }
            },
            onSave = { updatedNotice ->
                scope.launch {
                    isNoticeSaving = true
                    if (editingNotice == null) {
                        val result = FirebaseManager.createNotice(context, updatedNotice)
                        if (result.isSuccess) {
                            Toast.makeText(context, "নোটিশ সফলভাবে প্রকাশিত হয়েছে", Toast.LENGTH_SHORT).show()
                            showNoticeFormDialog = false
                            editingNotice = null
                        } else {
                            Toast.makeText(context, "ত্রুটি: ${result.exceptionOrNull()?.localizedMessage ?: "সংরক্ষণ ব্যর্থ হয়েছে"}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val result = FirebaseManager.updateNotice(context, updatedNotice)
                        if (result.isSuccess) {
                            Toast.makeText(context, "নোটিশ সফলভাবে আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show()
                            showNoticeFormDialog = false
                            editingNotice = null
                        } else {
                            Toast.makeText(context, "ত্রুটি: ${result.exceptionOrNull()?.localizedMessage ?: "আপডেট ব্যর্থ হয়েছে"}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    isNoticeSaving = false
                }
            }
        )
    }

    if (noticeToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                if (!isNoticeDeleting) {
                    noticeToDelete = null
                }
            },
            title = {
                Text(
                    text = "নোটিশ মুছে ফেলতে চান?",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NavyPrimary
                )
            },
            text = {
                Text(
                    text = "\"${noticeToDelete?.title}\" নোটিশটি স্থায়ীভাবে মুছে ফেলা হবে। আপনি কি নিশ্চিত?",
                    fontFamily = HindSiliguri,
                    fontSize = 14.sp,
                    color = SubtitleGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = noticeToDelete ?: return@Button
                        scope.launch {
                            isNoticeDeleting = true
                            val result = FirebaseManager.deleteNotice(context, target.id)
                            if (result.isSuccess) {
                                Toast.makeText(context, "নোটিশ মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "নোটিশ মোছা ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show()
                            }
                            isNoticeDeleting = false
                            noticeToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isNoticeDeleting,
                    modifier = Modifier.testTag("confirm_delete_notice_button")
                ) {
                    if (isNoticeDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "মুছুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { noticeToDelete = null },
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isNoticeDeleting
                ) {
                    Text(
                        text = "বাতিল",
                        fontFamily = HindSiliguri,
                        color = NavyPrimary
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        )
    }

    if (showMemberManagementDialog) {
        MemberManagementDialog(
            members = membersList,
            isLoading = isMembersLoading,
            errorMessage = membersErrorMessage,
            onDismiss = { showMemberManagementDialog = false },
            onMemberClick = { member ->
                selectedMemberForDetail = member
            },
            onRefresh = {
                memberListenerKey++
            }
        )
    }

    if (selectedMemberForDetail != null) {
        MemberDetailDialog(
            member = selectedMemberForDetail!!,
            currentAdmin = FirebaseManager.currentLoggedInMember,
            onDismiss = { selectedMemberForDetail = null },
            onSuccess = {
                memberListenerKey++
            }
        )
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    valueColor: Color,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = title,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = SubtitleGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = valueColor
            )
        }
    }
}

@Composable
private fun AdminManagementMenuCard(
    title: String,
    icon: ImageVector,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NavyPrimary,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyPrimary
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    isBoldValue: Boolean = false,
    valueColor: Color = NavyPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = HindSiliguri,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = SubtitleGray
        )

        Text(
            text = value,
            fontFamily = HindSiliguri,
            fontWeight = if (isBoldValue) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp,
            color = valueColor
        )
    }
}

private fun getBengaliDateToday(): String {
    val cal = java.util.Calendar.getInstance()
    val day = FirebaseManager.toBengaliDigits(cal.get(java.util.Calendar.DAY_OF_MONTH).toString())
    val months = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )
    val month = months.getOrElse(cal.get(java.util.Calendar.MONTH)) { "অক্টোবর" }
    val year = FirebaseManager.toBengaliDigits(cal.get(java.util.Calendar.YEAR).toString())
    return "$day $month $year"
}

@Composable
private fun NoticeManagementDialog(
    notices: List<NoticeItem>,
    isLoading: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onAddNotice: () -> Unit,
    onEditNotice: (NoticeItem) -> Unit,
    onDeleteNotice: (NoticeItem) -> Unit,
    onRefresh: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 660.dp)
                .testTag("notice_management_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "নোটিশ ব্যবস্থাপনা",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onRefresh) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "রিফ্রেশ",
                                tint = NavyPrimary
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "বন্ধ করুন",
                                tint = SubtitleGray
                            )
                        }
                    }
                }

                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

                Spacer(modifier = Modifier.height(12.dp))

                // Stats & Create Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "মোট নোটিশ: ${FirebaseManager.toBengaliDigits(notices.size.toString())}টি",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = SubtitleGray
                    )

                    Button(
                        onClick = onAddNotice,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("add_notice_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "নতুন",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "নতুন নোটিশ",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Notices List
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NavyPrimary)
                    }
                } else if (errorMessage != null && notices.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage,
                            fontFamily = HindSiliguri,
                            fontSize = 14.sp,
                            color = BloodRed
                        )
                    }
                } else if (notices.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "বর্তমানে কোনো নোটিশ নেই। নতুন নোটিশ তৈরি করুন।",
                            fontFamily = HindSiliguri,
                            fontSize = 14.sp,
                            color = SubtitleGray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(notices.size) { index ->
                            val notice = notices[index]
                            NoticeAdminItemCard(
                                notice = notice,
                                onEdit = { onEditNotice(notice) },
                                onDelete = { onDeleteNotice(notice) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoticeAdminItemCard(
    notice: NoticeItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val (tagBg, tagColor) = when (notice.tag) {
        "জরুরি" -> Pair(SoftRedBg, BloodRed)
        "অনুষ্ঠান" -> Pair(SoftSkyBlue, NavyPrimary)
        "হাদিয়া" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
        else -> Pair(Color(0xFFE0F2FE), Color(0xFF0284C7))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_notice_card_${notice.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(tagBg, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = notice.tag.ifBlank { "সাধারণ" },
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = tagColor
                    )
                }

                Text(
                    text = notice.date,
                    fontFamily = HindSiliguri,
                    fontSize = 12.sp,
                    color = SubtitleGray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = notice.title,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = NavyPrimary
            )

            if (notice.publisher.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "প্রকাশক: ${notice.publisher}",
                    fontFamily = HindSiliguri,
                    fontSize = 12.sp,
                    color = SubtitleGray
                )
            }

            if (notice.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notice.description,
                    fontFamily = HindSiliguri,
                    fontSize = 13.sp,
                    color = NavyPrimary.copy(alpha = 0.85f),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("edit_notice_${notice.id}"),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "সম্পাদনা",
                        tint = NavyPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "সম্পাদনা",
                        fontFamily = HindSiliguri,
                        fontSize = 12.sp,
                        color = NavyPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("delete_notice_${notice.id}"),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BloodRed.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "মুছুন",
                        tint = BloodRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "মুছুন",
                        fontFamily = HindSiliguri,
                        fontSize = 12.sp,
                        color = BloodRed
                    )
                }
            }
        }
    }
}

private fun formatMillisToBengaliDate(millis: Long): String {
    val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    cal.timeInMillis = millis
    val day = FirebaseManager.toBengaliDigits(cal.get(java.util.Calendar.DAY_OF_MONTH).toString())
    val months = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )
    val month = months.getOrElse(cal.get(java.util.Calendar.MONTH)) { "অক্টোবর" }
    val year = FirebaseManager.toBengaliDigits(cal.get(java.util.Calendar.YEAR).toString())
    return "$day $month $year"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoticeFormDialog(
    notice: NoticeItem?,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (NoticeItem) -> Unit
) {
    var title by remember(notice) { mutableStateOf(notice?.title ?: "") }
    var tag by remember(notice) { mutableStateOf(notice?.tag?.ifBlank { "জরুরি" } ?: "জরুরি") }
    var description by remember(notice) { mutableStateOf(notice?.description ?: "") }
    var publisher by remember(notice) { mutableStateOf(notice?.publisher ?: "সাধারণ সম্পাদক") }
    var meetingDate by remember(notice) { mutableStateOf(notice?.meetingDate ?: "") }
    var meetingTime by remember(notice) { mutableStateOf(notice?.meetingTime ?: "") }
    var meetingPlace by remember(notice) { mutableStateOf(notice?.meetingPlace ?: "") }
    var validationError by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember(notice) {
        mutableStateOf<Long?>(
            notice?.meetingTimestamp ?: FirebaseManager.parseBengaliDateToMillis(notice?.meetingDate ?: "")
        )
    }

    val tagOptions = listOf("জরুরি", "সভা", "অনুষ্ঠান", "হাদিয়া", "সাধারণ")

    if (showDatePicker) {
        val initialMillis = selectedDateMillis ?: System.currentTimeMillis()
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDateMillis = millis
                            meetingDate = formatMillisToBengaliDate(millis)
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "ঠিক আছে",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = NavyPrimary
                    ),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "বাতিল",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = NavyPrimary,
                    headlineContentColor = NavyPrimary,
                    weekdayContentColor = NavyPrimary,
                    subheadContentColor = SubtitleGray,
                    yearContentColor = NavyPrimary,
                    currentYearContentColor = NavyPrimary,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = NavyPrimary,
                    dayContentColor = NavyPrimary,
                    disabledDayContentColor = SubtitleGray.copy(alpha = 0.38f),
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = NavyPrimary,
                    todayDateBorderColor = NavyPrimary,
                    todayContentColor = NavyPrimary
                )
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 680.dp)
                .testTag("notice_form_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (notice == null) "নতুন নোটিশ প্রকাশ" else "নোটিশ সম্পাদনা",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )

                    IconButton(onClick = onDismiss, enabled = !isSaving) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = SubtitleGray
                        )
                    }
                }

                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Form Fields
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Category / Tag chips
                    Text(
                        text = "ক্যাটাগরি বা ধরন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = NavyPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tagOptions.forEach { option ->
                            val isSelected = tag == option
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (isSelected) NavyPrimary else Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { tag = option }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .testTag("tag_chip_$option")
                            ) {
                                Text(
                                    text = option,
                                    fontFamily = HindSiliguri,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else NavyPrimary
                                )
                            }
                        }
                    }

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            validationError = null
                        },
                        label = { Text("নোটিশের শিরোনাম *", fontFamily = HindSiliguri) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("notice_input_title"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        ),
                        singleLine = true
                    )

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            validationError = null
                        },
                        label = { Text("নোটিশের বিস্তারিত বিবরণ *", fontFamily = HindSiliguri) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("notice_input_description"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        ),
                        minLines = 3,
                        maxLines = 6
                    )

                    // Publisher
                    OutlinedTextField(
                        value = publisher,
                        onValueChange = { publisher = it },
                        label = { Text("প্রকাশক / পদবী", fontFamily = HindSiliguri) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("notice_input_publisher"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        ),
                        singleLine = true
                    )

                    // Meeting details (optional)
                    Text(
                        text = "সভার তথ্য (ঐচ্ছিক)",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = NavyPrimary
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = meetingDate,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("তারিখ (যেমন: ১৫ অক্টোবর ২০২৬)", fontFamily = HindSiliguri) },
                            placeholder = { Text("তারিখ নির্বাচন করতে চাপুন", fontFamily = HindSiliguri, color = SubtitleGray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("notice_input_meeting_date"),
                            trailingIcon = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 4.dp)
                                ) {
                                    if (meetingDate.isNotBlank()) {
                                        IconButton(
                                            onClick = {
                                                meetingDate = ""
                                                selectedDateMillis = null
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .testTag("clear_meeting_date_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "তারিখ মুছুন",
                                                tint = BloodRed,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = { showDatePicker = true },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .testTag("calendar_icon_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = "ক্যালেন্ডার খুলুন",
                                            tint = NavyPrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NavyPrimary,
                                unfocusedBorderColor = CardBorder,
                                focusedTextColor = NavyPrimary,
                                unfocusedTextColor = NavyPrimary
                            ),
                            singleLine = true
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(end = if (meetingDate.isNotBlank()) 80.dp else 48.dp)
                                .clickable { showDatePicker = true }
                        )
                    }

                    OutlinedTextField(
                        value = meetingTime,
                        onValueChange = { meetingTime = it },
                        label = { Text("সময় (যেমন: বাদ মাগরিব)", fontFamily = HindSiliguri) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("notice_input_meeting_time"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = meetingPlace,
                        onValueChange = { meetingPlace = it },
                        label = { Text("স্থান (যেমন: সংঘের কার্যালয়)", fontFamily = HindSiliguri) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("notice_input_meeting_place"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        ),
                        singleLine = true
                    )

                    if (validationError != null) {
                        Text(
                            text = validationError ?: "",
                            fontFamily = HindSiliguri,
                            fontSize = 12.sp,
                            color = BloodRed,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("cancel_notice_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = NavyPrimary
                        ),
                        enabled = !isSaving
                    ) {
                        Text(
                            text = "বাতিল",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NavyPrimary
                        )
                    }

                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                validationError = "নোটিশের শিরোনাম লিখুন"
                                return@Button
                            }
                            if (description.isBlank()) {
                                validationError = "নোটিশের বিবরণ লিখুন"
                                return@Button
                            }
                            val currentDate = if (notice == null || notice.date.isBlank()) {
                                getBengaliDateToday()
                            } else {
                                notice.date
                            }
                            val item = NoticeItem(
                                id = notice?.id ?: "",
                                tag = tag,
                                date = currentDate,
                                title = title.trim(),
                                description = description.trim(),
                                publisher = publisher.trim().ifBlank { "সাধারণ সম্পাদক" },
                                meetingDate = meetingDate.trim(),
                                meetingTime = meetingTime.trim(),
                                meetingPlace = meetingPlace.trim(),
                                createdAt = notice?.createdAt ?: System.currentTimeMillis(),
                                meetingTimestamp = selectedDateMillis ?: FirebaseManager.parseBengaliDateToMillis(meetingDate)
                            )
                            onSave(item)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("save_notice_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyPrimary,
                            contentColor = Color.White,
                            disabledContainerColor = NavyPrimary.copy(alpha = 0.6f),
                            disabledContentColor = Color.White
                        ),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "নোটিশ সংরক্ষণ করুন",
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
}
