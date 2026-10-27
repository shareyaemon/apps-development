package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.FirebaseManager
import com.example.model.NoticeItem
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BloodRed
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SoftRedBg
import com.example.ui.theme.SoftSkyBlue
import com.example.ui.theme.SubtitleGray

@Composable
fun GuestHomeScreen(
    onNavigateLogin: () -> Unit,
    onNavigateApplication: () -> Unit,
    onNavigateAbout: () -> Unit,
    onNavigateBloodDonors: () -> Unit = {},
    onNavigateGallery: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedBottomNav by remember { mutableIntStateOf(0) }

    var showNoticesSheet by remember { mutableStateOf(false) }
    var showActivitiesSheet by remember { mutableStateOf(false) }
    var showBloodDonorsSheet by remember { mutableStateOf(false) }
    var showGalleryDialog by remember { mutableStateOf(false) }

    var notices by remember { mutableStateOf<List<NoticeItem>>(emptyList()) }

    BackHandler {
        when {
            showNoticesSheet -> showNoticesSheet = false
            showActivitiesSheet -> showActivitiesSheet = false
            showBloodDonorsSheet -> showBloodDonorsSheet = false
            showGalleryDialog -> showGalleryDialog = false
            selectedBottomNav != 0 -> selectedBottomNav = 0
            else -> (context as? android.app.Activity)?.finish()
        }
    }

    DisposableEffect(context) {
        var listener: com.google.firebase.firestore.ListenerRegistration? = null
        try {
            listener = FirebaseManager.listenToNotices(
                context = context,
                useFallback = false,
                onSuccess = { list ->
                    notices = list
                },
                onError = {
                    notices = emptyList()
                }
            )
        } catch (t: Throwable) {
            android.util.Log.e("GuestHomeScreen", "Error setting up notices listener", t)
            notices = emptyList()
        }

        onDispose {
            try {
                listener?.remove()
            } catch (t: Throwable) {
                android.util.Log.e("GuestHomeScreen", "Error removing notices listener", t)
            }
        }
    }

    Scaffold(
        modifier = Modifier.testTag("guest_home_screen"),
        bottomBar = {
            // Guest bottom navigation bar: হোম, নোটিশ, কার্যক্রম, রক্তদাতা, লগইন
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(width = 0.8.dp, color = CardBorder)
                    .testTag("guest_bottom_nav")
            ) {
                // 1. হোম
                NavigationBarItem(
                    selected = selectedBottomNav == 0,
                    onClick = { selectedBottomNav = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "হোম",
                            fontFamily = HindSiliguri,
                            fontSize = 12.sp,
                            fontWeight = if (selectedBottomNav == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = NavyPrimary,
                        indicatorColor = SoftSkyBlue,
                        unselectedIconColor = SubtitleGray,
                        unselectedTextColor = SubtitleGray
                    )
                )

                // 2. নোটিশ
                NavigationBarItem(
                    selected = selectedBottomNav == 1,
                    onClick = {
                        selectedBottomNav = 1
                        showNoticesSheet = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Notices",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "নোটিশ",
                            fontFamily = HindSiliguri,
                            fontSize = 12.sp,
                            fontWeight = if (selectedBottomNav == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = NavyPrimary,
                        indicatorColor = SoftSkyBlue,
                        unselectedIconColor = SubtitleGray,
                        unselectedTextColor = SubtitleGray
                    )
                )

                // 3. কার্যক্রম
                NavigationBarItem(
                    selected = selectedBottomNav == 2,
                    onClick = {
                        selectedBottomNav = 2
                        showActivitiesSheet = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Activities",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "কার্যক্রম",
                            fontFamily = HindSiliguri,
                            fontSize = 12.sp,
                            fontWeight = if (selectedBottomNav == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = NavyPrimary,
                        indicatorColor = SoftSkyBlue,
                        unselectedIconColor = SubtitleGray,
                        unselectedTextColor = SubtitleGray
                    )
                )

                // 4. রক্তদাতা
                NavigationBarItem(
                    selected = selectedBottomNav == 3,
                    onClick = {
                        onNavigateBloodDonors()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = "Blood Donors",
                            modifier = Modifier.size(22.dp),
                            tint = if (selectedBottomNav == 3) BloodRed else SubtitleGray
                        )
                    },
                    label = {
                        Text(
                            text = "রক্তদাতা",
                            fontFamily = HindSiliguri,
                            fontSize = 12.sp,
                            fontWeight = if (selectedBottomNav == 3) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedBottomNav == 3) BloodRed else SubtitleGray
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BloodRed,
                        selectedTextColor = BloodRed,
                        indicatorColor = SoftRedBg,
                        unselectedIconColor = SubtitleGray,
                        unselectedTextColor = SubtitleGray
                    )
                )

                // 5. লগইন
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateLogin,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Login",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "লগইন",
                            fontFamily = HindSiliguri,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = NavyPrimary,
                        indicatorColor = SoftSkyBlue,
                        unselectedIconColor = SubtitleGray,
                        unselectedTextColor = SubtitleGray
                    )
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
        ) {
            // Navy Top Header Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = NavyPrimary,
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
                        // Logo and App Name Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.club_logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .testTag("guest_header_logo"),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "আসসালামু আলাইকুম",
                                    fontFamily = HindSiliguri,
                                    fontSize = 13.sp,
                                    color = Color(0xFFBAC7E5)
                                )
                                Text(
                                    text = "হিলফুল ফুজুল স্বর্ণটিলা যুব সংঘ",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Two action buttons: "লগইন" & "সদস্য হতে আবেদন"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Login button (Sky Blue)
                            Button(
                                onClick = onNavigateLogin,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("header_login_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SkyBlueAccent,
                                    contentColor = NavyPrimary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "লগইন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = NavyPrimary
                                )
                            }

                            // Member application button (Outlined with white border)
                            OutlinedButton(
                                onClick = onNavigateApplication,
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(44.dp)
                                    .testTag("header_apply_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = NavyPrimary,
                                    contentColor = Color.White
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color.White)
                            ) {
                                Text(
                                    text = "সদস্য হতে আবেদন",
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

            // Body Content
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // "সদস্যদের জন্য" Banner Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Toast.makeText(context, "সদস্য সুবিধা দেখতে লগইন করুন", Toast.LENGTH_SHORT).show()
                                onNavigateLogin()
                            }
                            .testTag("members_banner_card"),
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
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(SoftSkyBlue, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Lock",
                                    tint = NavyPrimary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "সদস্যদের জন্য",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NavyPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "হাদিয়া, প্রোফাইল ও আইডি কার্ড দেখতে লগইন করুন",
                                    fontFamily = HindSiliguri,
                                    fontSize = 14.sp,
                                    color = SubtitleGray
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Open",
                                tint = SubtitleGray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 8 Quick Action Items Grid (4 columns x 2 rows)
                    // Row 1: নোটিশ, কার্যক্রম, রক্তদাতা, গ্যালারি
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            title = "নোটিশ",
                            icon = Icons.Default.Description,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = { showNoticesSheet = true },
                            tag = "action_notices"
                        )
                        QuickActionButton(
                            title = "কার্যক্রম",
                            icon = Icons.Default.CalendarMonth,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = { showActivitiesSheet = true },
                            tag = "action_activities"
                        )
                        QuickActionButton(
                            title = "রক্তদাতা",
                            icon = Icons.Default.WaterDrop,
                            iconBg = SoftRedBg,
                            iconTint = BloodRed,
                            isLocked = false,
                            onClick = onNavigateBloodDonors,
                            tag = "action_donors"
                        )
                        QuickActionButton(
                            title = "গ্যালারি",
                            icon = Icons.Default.PhotoLibrary,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateGallery,
                            tag = "action_gallery"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 2: পরিচিতি, হাদিয়া (Locked), আয়-ব্যয় (Locked), সদস্য (Locked)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickActionButton(
                            title = "পরিচিতি",
                            icon = Icons.Default.Info,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = false,
                            onClick = onNavigateAbout,
                            tag = "action_about"
                        )
                        QuickActionButton(
                            title = "হাদিয়া",
                            icon = Icons.Default.CreditCard,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = true,
                            onClick = {
                                Toast.makeText(context, "হাদিয়া দেখতে লগইন করুন", Toast.LENGTH_SHORT).show()
                                onNavigateLogin()
                            },
                            tag = "action_hadiya_locked"
                        )
                        QuickActionButton(
                            title = "আয়-ব্যয়",
                            icon = Icons.Default.BarChart,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = true,
                            onClick = {
                                Toast.makeText(context, "আয়-ব্যয়ের হিসাব দেখতে লগইন করুন", Toast.LENGTH_SHORT).show()
                                onNavigateLogin()
                            },
                            tag = "action_expenses_locked"
                        )
                        QuickActionButton(
                            title = "সদস্য",
                            icon = Icons.Default.Group,
                            iconBg = SoftSkyBlue,
                            iconTint = NavyPrimary,
                            isLocked = true,
                            onClick = {
                                Toast.makeText(context, "সদস্য তালিকা দেখতে লগইন করুন", Toast.LENGTH_SHORT).show()
                                onNavigateLogin()
                            },
                            tag = "action_members_locked"
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
                                .clickable { showNoticesSheet = true }
                                .padding(vertical = 4.dp)
                                .testTag("view_all_notices_link")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notice Card (Matching 02.png)
                    val latestNotice = notices.firstOrNull()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showNoticesSheet = true }
                            .testTag("latest_notice_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if (latestNotice != null) {
                                if (latestNotice.date.isNotBlank()) {
                                    Text(
                                        text = latestNotice.date,
                                        fontFamily = HindSiliguri,
                                        fontSize = 14.sp,
                                        color = SubtitleGray
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                                Text(
                                    text = latestNotice.title,
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NavyPrimary
                                )
                                if (latestNotice.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = latestNotice.description,
                                        fontFamily = HindSiliguri,
                                        fontSize = 14.sp,
                                        color = SubtitleGray,
                                        lineHeight = 20.sp
                                    )
                                }
                            } else {
                                Text(
                                    text = "এখনো কোনো নোটিশ নেই",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = SubtitleGray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Upcoming Event Card (Matching 02.png)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showActivitiesSheet = true }
                            .testTag("upcoming_event_card"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(NavyPrimary, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "আসন্ন কার্যক্রম",
                                    fontFamily = HindSiliguri,
                                    fontSize = 14.sp,
                                    color = SubtitleGray
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "বার্ষিক কুরআন মাহফিল",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NavyPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Bottom Sheets
    if (showNoticesSheet) {
        NoticesBottomSheet(
            notices = notices,
            onDismiss = {
                showNoticesSheet = false
                selectedBottomNav = 0
            }
        )
    }

    if (showActivitiesSheet) {
        ActivitiesBottomSheet(onDismiss = {
            showActivitiesSheet = false
            selectedBottomNav = 0
        })
    }

    if (showBloodDonorsSheet) {
        BloodDonorsBottomSheet(onDismiss = {
            showBloodDonorsSheet = false
            selectedBottomNav = 0
        })
    }

    if (showGalleryDialog) {
        GalleryBottomSheet(onDismiss = { showGalleryDialog = false })
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    isLocked: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Main Icon Box
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(iconBg, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Dark Lock Badge on top right if locked
            if (isLocked) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                        .background(NavyPrimary, CircleShape)
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontFamily = HindSiliguri,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = NavyPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GalleryBottomSheet(onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "সংঘের চিত্র গ্যালারি",
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = NavyPrimary
            )
        },
        text = {
            Column {
                Text(
                    text = "হিলফুল ফুজুল স্বর্ণটিলা যুব সংঘের বিগত রক্তদান ক্যাম্প, বাৎসরিক মাহফিল ও সমাজসেবামূলক কর্মকাণ্ডের আলোকচিত্রসমূহ শিগগিরই গ্যালারিতে হালনাগাদ করা হবে।",
                    fontFamily = HindSiliguri,
                    fontSize = 14.sp,
                    color = SubtitleGray,
                    lineHeight = 20.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "ঠিক আছে",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    )
}
