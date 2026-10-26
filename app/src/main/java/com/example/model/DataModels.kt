package com.example.model

sealed class Screen {
    object Splash : Screen()
    object GuestHome : Screen()
    object Login : Screen()
    object MemberApplication : Screen()
    object AboutOrganization : Screen()
    // Member Flow Screens
    object MemberHome : Screen()
    object NoticeBoard : Screen()
    data class NoticeDetail(val noticeId: String = "1") : Screen()
    object Activities : Screen()
    // 3 New Hadiya Screens
    object HadiyaTracking : Screen()
    object OnlinePayment : Screen()
    object HadiyaReceipt : Screen()
    // 3 New Screens: আয়-ব্যয়, সদস্য তালিকা, প্রোফাইল
    object Finances : Screen()
    object MemberList : Screen()
    object Profile : Screen()
    // 3 New Screens: রক্তদাতা তালিকা, গ্যালারি, মিটিং হাজিরা ও রেজুলেশন
    object BloodDonors : Screen()
    object Gallery : Screen()
    object MeetingAttendance : Screen()
    // সেটিংস ও নোটিফিকেশন স্ক্রিন
    object Settings : Screen()
    object Notifications : Screen()
    // ২ নতুন প্রোফাইল সাব-স্ক্রিন
    object BloodDonationStatus : Screen()
    object DigitalIdCard : Screen()
    object AdminPanel : Screen()
    data class MemberPlaceholder(val tab: MemberBottomTab, val title: String) : Screen()
}

data class ApplicationItem(
    val id: String,
    val fullName: String,
    val fatherName: String = "",
    val mobile: String,
    val bloodGroup: String = "",
    val address: String = "",
    val status: String = "pending",
    val createdAt: String = ""
)

enum class MemberBottomTab {
    HOME,
    NOTICE,
    HADIYA,
    MEMBERS,
    PROFILE
}

enum class HadiyaPaymentState {
    PAID,
    DUE,
    PARTIAL,
    UPCOMING
}

data class MonthHadiya(
    val monthName: String,
    val amount: String = "৳ ২০০",
    val state: HadiyaPaymentState,
    val statusText: String
)

data class NoticeItem(
    val id: String = "",
    val title: String = "",
    val tag: String = "জরুরি",
    val date: String = "৫ অক্টোবর ২০২৬",
    val description: String = "",
    val publisher: String = "সাধারণ সম্পাদক",
    val meetingDate: String = "",
    val meetingTime: String = "",
    val meetingPlace: String = "",
    val isImportant: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val meetingTimestamp: Long? = null
)

data class ActivityItem(
    val id: String,
    val title: String,
    val date: String = "তারিখ: [ঘোষণা হবে]",
    val isRedIcon: Boolean = false,
    val isCompleted: Boolean = false
)

data class EventItem(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val location: String,
    val description: String
)

data class BloodDonor(
    val id: String,
    val name: String,
    val bloodGroup: String,
    val phone: String,
    val area: String,
    val lastDonation: String,
    val isAvailable: Boolean = true
)

data class CommitteeMember(
    val name: String,
    val role: String,
    val phone: String
)

data class ClubMember(
    val id: String,
    val memberCode: String,
    val name: String,
    val role: String,
    val phone: String,
    val bloodGroup: String,
    val isActive: Boolean = true,
    val area: String = "স্বর্ণটিলা, সিলেট",
    val joiningYear: String = "২০২০"
)

data class TransactionItem(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val amount: String,
    val isIncome: Boolean,
    val method: String
)

data class MonthlyFinanceBar(
    val monthName: String,
    val incomeAmount: Float,
    val expenseAmount: Float,
    val incomeDisplay: String,
    val expenseDisplay: String
)

