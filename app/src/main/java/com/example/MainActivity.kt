package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.FirebaseManager
import com.example.data.MemberData
import com.example.model.MemberBottomTab
import com.example.model.Screen
import com.example.ui.screens.AboutOrganizationScreen
import com.example.ui.screens.ActivitiesScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.BloodDonationStatusScreen
import com.example.ui.screens.BloodDonorsScreen
import com.example.ui.screens.DigitalIdCardScreen
import com.example.ui.screens.FinancesScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.GuestHomeScreen
import com.example.ui.screens.HadiyaReceiptScreen
import com.example.ui.screens.HadiyaTrackingScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MemberApplicationScreen
import com.example.ui.screens.MemberHomeScreen
import com.example.ui.screens.MemberListScreen
import com.example.ui.screens.MemberPlaceholderScreen
import com.example.ui.screens.MeetingAttendanceScreen
import com.example.ui.screens.NoticeBoardScreen
import com.example.ui.screens.NoticeDetailScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnlinePaymentScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            FirebaseManager.ensureFirebaseInitialized(applicationContext)
        } catch (t: Throwable) {
            android.util.Log.e("MainActivity", "Firebase init safe catch: ${t.message}", t)
        }
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundLight
                ) {
                    HilfulFujulApp()
                }
            }
        }
    }
}

@Composable
fun HilfulFujulApp() {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Splash) }
    var loggedInMember by remember { mutableStateOf<MemberData?>(FirebaseManager.currentLoggedInMember) }
    var loggedInUser by remember { mutableStateOf(loggedInMember?.name ?: "মুহাম্মদ আব্দুল্লাহ") }

    val currentScreen = backStack.lastOrNull() ?: Screen.Splash

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    fun replaceRoot(screen: Screen) {
        backStack.clear()
        backStack.add(screen)
    }

    fun handleMemberTabSelect(tab: MemberBottomTab) {
        val targetScreen: Screen = when (tab) {
            MemberBottomTab.HOME -> Screen.MemberHome
            MemberBottomTab.NOTICE -> Screen.NoticeBoard
            MemberBottomTab.HADIYA -> Screen.HadiyaTracking
            MemberBottomTab.MEMBERS -> Screen.MemberList
            MemberBottomTab.PROFILE -> Screen.Profile
        }
        replaceRoot(targetScreen)
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            is Screen.Splash -> {
                SplashScreen(
                    onStartClick = {
                        replaceRoot(Screen.GuestHome)
                    }
                )
            }

            is Screen.GuestHome -> {
                GuestHomeScreen(
                    onNavigateLogin = {
                        navigateTo(Screen.Login)
                    },
                    onNavigateApplication = {
                        navigateTo(Screen.MemberApplication)
                    },
                    onNavigateAbout = {
                        navigateTo(Screen.AboutOrganization)
                    },
                    onNavigateBloodDonors = {
                        navigateTo(Screen.BloodDonors)
                    },
                    onNavigateGallery = {
                        navigateTo(Screen.Gallery)
                    }
                )
            }

            is Screen.Login -> {
                LoginScreen(
                    onNavigateHome = {
                        navigateBack()
                    },
                    onNavigateApplication = {
                        navigateTo(Screen.MemberApplication)
                    },
                    onLoginSuccess = { member ->
                        loggedInMember = member
                        loggedInUser = member.name
                        replaceRoot(Screen.MemberHome)
                    }
                )
            }

            is Screen.MemberApplication -> {
                MemberApplicationScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            is Screen.AboutOrganization -> {
                AboutOrganizationScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // সদস্য হোম (Member Home)
            is Screen.MemberHome -> {
                MemberHomeScreen(
                    userName = loggedInUser,
                    member = loggedInMember,
                    onNavigateNoticeBoard = {
                        handleMemberTabSelect(MemberBottomTab.NOTICE)
                    },
                    onNavigateNoticeDetail = { id ->
                        navigateTo(Screen.NoticeDetail(id))
                    },
                    onNavigateActivities = {
                        navigateTo(Screen.Activities)
                    },
                    onNavigateHadiya = {
                        handleMemberTabSelect(MemberBottomTab.HADIYA)
                    },
                    onNavigatePayOnline = {
                        navigateTo(Screen.OnlinePayment)
                    },
                    onNavigateMembers = {
                        handleMemberTabSelect(MemberBottomTab.MEMBERS)
                    },
                    onNavigateProfile = {
                        handleMemberTabSelect(MemberBottomTab.PROFILE)
                    },
                    onNavigateIncomeExpense = {
                        navigateTo(Screen.Finances)
                    },
                    onNavigateBloodDonors = {
                        navigateTo(Screen.BloodDonors)
                    },
                    onNavigateGallery = {
                        navigateTo(Screen.Gallery)
                    },
                    onNavigateMeeting = {
                        navigateTo(Screen.MeetingAttendance)
                    },
                    onNavigateNotifications = {
                        navigateTo(Screen.Notifications)
                    },
                    onLogout = {
                        replaceRoot(Screen.GuestHome)
                    }
                )
            }

            // নোটিশ বোর্ড (Notice Board)
            is Screen.NoticeBoard -> {
                NoticeBoardScreen(
                    onNavigateHome = {
                        handleMemberTabSelect(MemberBottomTab.HOME)
                    },
                    onNavigateNoticeDetail = { id ->
                        navigateTo(Screen.NoticeDetail(id))
                    },
                    onNavigateHadiya = {
                        handleMemberTabSelect(MemberBottomTab.HADIYA)
                    },
                    onNavigateMembers = {
                        handleMemberTabSelect(MemberBottomTab.MEMBERS)
                    },
                    onNavigateProfile = {
                        handleMemberTabSelect(MemberBottomTab.PROFILE)
                    }
                )
            }

            // নোটিশ বিস্তারিত (Notice Details)
            is Screen.NoticeDetail -> {
                NoticeDetailScreen(
                    noticeId = screen.noticeId,
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // কার্যক্রম (Activities)
            is Screen.Activities -> {
                ActivitiesScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // ৩টি নতুন হাদিয়া পেইজ:
            // ১. মাসিক হাদিয়া ট্র্যাকিং (Hadiya Tracking)
            is Screen.HadiyaTracking -> {
                HadiyaTrackingScreen(
                    onNavigateBack = {
                        handleMemberTabSelect(MemberBottomTab.HOME)
                    },
                    onNavigatePayOnline = {
                        navigateTo(Screen.OnlinePayment)
                    },
                    onNavigateReceipt = {
                        navigateTo(Screen.HadiyaReceipt)
                    },
                    onTabSelected = { tab ->
                        handleMemberTabSelect(tab)
                    }
                )
            }

            // ২. অনলাইনে পরিশোধ (Online Payment)
            is Screen.OnlinePayment -> {
                OnlinePaymentScreen(
                    onNavigateBack = {
                        navigateBack()
                    },
                    onPaymentSuccess = {
                        navigateTo(Screen.HadiyaReceipt)
                    }
                )
            }

            // ৩. হাদিয়া রসিদ (Hadiya Receipt)
            is Screen.HadiyaReceipt -> {
                HadiyaReceiptScreen(
                    onNavigateHome = {
                        replaceRoot(Screen.MemberHome)
                    }
                )
            }

            // ৪. আয়-ব্যয় হিসাব (Finances)
            is Screen.Finances -> {
                FinancesScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // ৫. সদস্য তালিকা (Member List)
            is Screen.MemberList -> {
                MemberListScreen(
                    onNavigateHome = {
                        handleMemberTabSelect(MemberBottomTab.HOME)
                    },
                    onTabSelected = { tab ->
                        handleMemberTabSelect(tab)
                    }
                )
            }

            // ৬. প্রোফাইল (Profile)
            is Screen.Profile -> {
                ProfileScreen(
                    userName = loggedInUser,
                    member = loggedInMember,
                    onNavigateHome = {
                        handleMemberTabSelect(MemberBottomTab.HOME)
                    },
                    onTabSelected = { tab ->
                        handleMemberTabSelect(tab)
                    },
                    onLogout = {
                        replaceRoot(Screen.GuestHome)
                    },
                    onNavigateSettings = {
                        navigateTo(Screen.Settings)
                    },
                    onNavigateHadiyaRecord = {
                        navigateTo(Screen.HadiyaTracking)
                    },
                    onNavigateMeetingAttendance = {
                        navigateTo(Screen.MeetingAttendance)
                    },
                    onNavigateBloodDonationStatus = {
                        navigateTo(Screen.BloodDonationStatus)
                    },
                    onNavigateDigitalIdCard = {
                        navigateTo(Screen.DigitalIdCard)
                    },
                    onNavigateAdminPanel = {
                        navigateTo(Screen.AdminPanel)
                    },
                    onMemberUpdated = { updated ->
                        loggedInMember = updated
                        loggedInUser = updated.name
                    }
                )
            }

            // ৭. রক্তদাতা তালিকা (Blood Donors)
            is Screen.BloodDonors -> {
                BloodDonorsScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // ৮. গ্যালারি (Gallery)
            is Screen.Gallery -> {
                GalleryScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // ৯. মিটিং হাজিরা ও রেজুলেশন (Meeting Attendance & Resolution)
            is Screen.MeetingAttendance -> {
                MeetingAttendanceScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // ১০. সেটিংস (Settings)
            is Screen.Settings -> {
                SettingsScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // ১১. নোটিফিকেশন (Notifications)
            is Screen.Notifications -> {
                NotificationsScreen(
                    onNavigateBack = {
                        navigateBack()
                    }
                )
            }

            // ১২. রক্তদানের তথ্য ও স্ট্যাটাস (Blood Donation Status)
            is Screen.BloodDonationStatus -> {
                BloodDonationStatusScreen(
                    userName = loggedInUser,
                    member = loggedInMember,
                    onNavigateBack = {
                        navigateBack()
                    },
                    onTabSelected = { tab ->
                        handleMemberTabSelect(tab)
                    },
                    onMemberUpdated = { updated ->
                        loggedInMember = updated
                        loggedInUser = updated.name
                    }
                )
            }

            // ১৩. ডিজিটাল আইডি কার্ড (Digital ID Card)
            is Screen.DigitalIdCard -> {
                DigitalIdCardScreen(
                    userName = loggedInUser,
                    member = loggedInMember,
                    onNavigateBack = {
                        navigateBack()
                    },
                    onTabSelected = { tab ->
                        handleMemberTabSelect(tab)
                    }
                )
            }

            // ১৪. অ্যাডমিন প্যানেল ড্যাশবোর্ড (Admin Dashboard Panel)
            is Screen.AdminPanel -> {
                AdminDashboardScreen(
                    onNavigateBack = {
                        navigateBack()
                    },
                    onNavigateNotifications = {
                        navigateTo(Screen.Notifications)
                    }
                )
            }

            // ১০. প্লেসহোল্ডার স্ক্রিন (Fallback if needed)
            is Screen.MemberPlaceholder -> {
                MemberPlaceholderScreen(
                    title = screen.title,
                    currentTab = screen.tab,
                    onTabSelected = { tab ->
                        handleMemberTabSelect(tab)
                    },
                    onNavigateBack = {
                        handleMemberTabSelect(MemberBottomTab.HOME)
                    },
                    showLogout = (screen.tab == MemberBottomTab.PROFILE),
                    onLogout = {
                        replaceRoot(Screen.GuestHome)
                    }
                )
            }
        }
    }
}
