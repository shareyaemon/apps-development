package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.ui.theme.HindSiliguri
import kotlinx.coroutines.launch

@Composable
fun BloodDonationStatusScreen(
    userName: String = "মুহাম্মদ আব্দুল্লাহ",
    member: MemberData? = null,
    onNavigateBack: () -> Unit,
    onTabSelected: (MemberBottomTab) -> Unit = {},
    onMemberUpdated: (MemberData) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var memberData by remember(member) {
        mutableStateOf(member ?: FirebaseManager.currentLoggedInMember)
    }

    var hasPendingRequest by remember { mutableStateOf(false) }
    var isSubmittingDonation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit, userName) {
        try {
            val fetched = FirebaseManager.getCurrentOrFetchMember(context, userName)
            if (fetched != null) {
                memberData = fetched
                onMemberUpdated(fetched)
            }
        } catch (t: Throwable) {
            android.util.Log.w("BloodDonationStatus", "Failed fetching member: ${t.message}")
        }
    }

    LaunchedEffect(memberData?.uid, memberData?.memberId) {
        val uid = memberData?.uid
        val mId = memberData?.memberId
        if (!uid.isNullOrBlank() || !mId.isNullOrBlank()) {
            hasPendingRequest = FirebaseManager.hasPendingDonationRequest(context, uid, mId)
        }
    }

    var selectedStatus by remember(memberData?.bloodDonationStatus) {
        mutableStateOf(memberData?.bloodDonationStatus?.ifBlank { "প্রস্তুত" } ?: "প্রস্তুত")
    }
    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.testTag("blood_donation_status_screen"),
        containerColor = Color(0xFFF4F7FC),
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
                .background(Color(0xFFF4F7FC))
                .padding(innerPadding)
        ) {
            // ১. সাদা হেডার: ব্যাক তীর, শিরোনাম ২২sp Bold নেভি
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
                            .testTag("blood_status_back_button")
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
                        text = "রক্তদানের তথ্য ও স্ট্যাটাস",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1B2559),
                        modifier = Modifier.testTag("blood_status_title")
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            // ২. স্ক্রলযোগ্য কনটেন্ট
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // উপরে নেভি কার্ড: বাঁয়ে বড় লাল গোল বক্সে রক্তের গ্রুপ, ডানে "আপনার রক্তের গ্রুপ" ও আসল গ্রুপ নাম
                val blood = memberData?.bloodGroup?.trim() ?: ""
                val circleBloodText = if (blood.isNotBlank() && blood != "যোগ করা হয়নি") blood else "—"
                val fullBloodText = when {
                    blood.isBlank() || blood == "যোগ করা হয়নি" -> "রক্তের গ্রুপ: যোগ করা হয়নি"
                    blood == "A+" -> "A+ (Positive)"
                    blood == "A-" -> "A− (Negative)"
                    blood == "B+" -> "B+ (Positive)"
                    blood == "B-" -> "B− (Negative)"
                    blood == "O+" -> "O+ (Positive)"
                    blood == "O-" -> "O− (Negative)"
                    blood == "AB+" -> "AB+ (Positive)"
                    blood == "AB-" -> "AB− (Negative)"
                    else -> blood
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("blood_group_banner_card"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2559)),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // বাঁয়ে বড় লাল গোল বক্সে রক্তের গ্রুপ
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDC2626)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = circleBloodText,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = if (circleBloodText.length > 2) 20.sp else 24.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // ডানে "আপনার রক্তের গ্রুপ" ও বিস্তারিত
                        Column {
                            Text(
                                text = "আপনার রক্তের গ্রুপ",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color(0xFFBAC7E5)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = fullBloodText,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                // একটি সাদা কার্ডে "রক্তদানের স্ট্যাটাস"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("blood_donation_status_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "রক্তদানের স্ট্যাটাস",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFF1B2559)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // "প্রস্তুত" ও "অপেক্ষমাণ" নামে ২টি অপশন (রেডিও), নির্বাচিত অপশন নেভি বর্ডার ও হালকা নীল ব্যাকগ্রাউন্ড
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // অপশন ১: "প্রস্তুত"
                            val isReadySelected = selectedStatus == "প্রস্তুত"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isReadySelected) Color(0xFFE6F5FD) else Color.White)
                                    .border(
                                        border = BorderStroke(
                                            width = if (isReadySelected) 1.5.dp else 1.dp,
                                            color = if (isReadySelected) Color(0xFF1B2559) else Color(0xFFCBD5E1)
                                        ),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedStatus = "প্রস্তুত" }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                                    .testTag("radio_ready"),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isReadySelected,
                                        onClick = { selectedStatus = "প্রস্তুত" },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF1B2559),
                                            unselectedColor = Color(0xFF64748B)
                                        ),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "প্রস্তুত",
                                        fontFamily = HindSiliguri,
                                        fontWeight = if (isReadySelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 15.sp,
                                        color = if (isReadySelected) Color(0xFF1B2559) else Color(0xFF475569)
                                    )
                                }
                            }

                            // অপশন ২: "অপেক্ষমাণ"
                            val isWaitingSelected = selectedStatus == "অপেক্ষমাণ"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isWaitingSelected) Color(0xFFE6F5FD) else Color.White)
                                    .border(
                                        border = BorderStroke(
                                            width = if (isWaitingSelected) 1.5.dp else 1.dp,
                                            color = if (isWaitingSelected) Color(0xFF1B2559) else Color(0xFFCBD5E1)
                                        ),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedStatus = "অপেক্ষমাণ" }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                                    .testTag("radio_waiting"),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isWaitingSelected,
                                        onClick = { selectedStatus = "অপেক্ষমাণ" },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF1B2559),
                                            unselectedColor = Color(0xFF64748B)
                                        ),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "অপেক্ষমাণ",
                                        fontFamily = HindSiliguri,
                                        fontWeight = if (isWaitingSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 15.sp,
                                        color = if (isWaitingSelected) Color(0xFF1B2559) else Color(0xFF475569)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // তিনটি তথ্য সারি:
                        // ১. "সর্বশেষ রক্তদান: [তারিখ]"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "তারিখ",
                                    tint = Color(0xFF0284C7),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "সর্বশেষ রক্তদান: ${memberData?.lastDonationDateDisplay ?: "এখনো রক্তদান করা হয়নি"}",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // ২. "মোট রক্তদান: [সংখ্যা] বার"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDCFCE7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "মোট রক্তদান",
                                    tint = Color(0xFF166534),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "মোট রক্তদান: ${memberData?.totalDonationsDisplay ?: "০ বার"}",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // ৩. "পরবর্তী রক্তদানের উপযুক্ত তারিখ: [তারিখ]"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventAvailable,
                                    contentDescription = "উপযুক্ত তারিখ",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "পরবর্তী রক্তদানের উপযুক্ত তারিখ: ${memberData?.nextEligibleDonationDateDisplay ?: "এখনই উপযুক্ত"}",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // নিচে নেভি "স্ট্যাটাস সংরক্ষণ করুন" বাটন
                Button(
                    onClick = {
                        isSaving = true
                        scope.launch {
                            val res = FirebaseManager.updateBloodDonationStatus(context, selectedStatus)
                            isSaving = false
                            if (res.isSuccess) {
                                val updated = (memberData ?: FirebaseManager.currentLoggedInMember ?: MemberData()).copy(bloodDonationStatus = selectedStatus)
                                memberData = updated
                                FirebaseManager.currentLoggedInMember = updated
                                onMemberUpdated(updated)
                                Toast.makeText(context, "স্ট্যাটাস সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "সংরক্ষণ হয়নি, আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_blood_status_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1B2559),
                        contentColor = Color.White
                    ),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "স্ট্যাটাস সংরক্ষণ করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // "রক্তদানের তারিখ জমা দিন" সেকশন কার্ড
                val isEligible = memberData?.isEligibleToDonateNow ?: true
                val canSubmitDate = isEligible && !hasPendingRequest

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_donation_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "রক্তদানের নতুন তথ্য জমা",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFF1B2559)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "রক্তদান সম্পন্ন করার পর তারিখটি জমা দিলে অ্যাডমিন যাচাই করে আপনার প্রোফাইলে যোগ করবেন।",
                            fontFamily = HindSiliguri,
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (hasPendingRequest) {
                                    Toast.makeText(context, "একটি অনুরোধ অপেক্ষমাণ আছে", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (!isEligible) {
                                    Toast.makeText(context, "পরবর্তী উপযুক্ত তারিখ: ${memberData?.nextEligibleDonationDateDisplay ?: "অপেক্ষমাণ"}", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val calendar = java.util.Calendar.getInstance()
                                val datePickerDialog = android.app.DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val chosenCal = java.util.Calendar.getInstance().apply {
                                            set(year, month, dayOfMonth, 12, 0, 0)
                                        }
                                        isSubmittingDonation = true
                                        scope.launch {
                                            val res = FirebaseManager.submitDonationRequest(context, chosenCal.time)
                                            isSubmittingDonation = false
                                            if (res.isSuccess) {
                                                hasPendingRequest = true
                                                Toast.makeText(context, "অনুরোধ জমা হয়েছে, অ্যাডমিনের অনুমোদনের অপেক্ষায়", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(context, "জমা হয়নি, আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    calendar.get(java.util.Calendar.YEAR),
                                    calendar.get(java.util.Calendar.MONTH),
                                    calendar.get(java.util.Calendar.DAY_OF_MONTH)
                                ).apply {
                                    datePicker.maxDate = System.currentTimeMillis() // আজ বা তার আগের তারিখ
                                }
                                datePickerDialog.show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_donation_date_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (canSubmitDate) Color(0xFFDC2626) else Color(0xFFCBD5E1),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE2E8F0),
                                disabledContentColor = Color(0xFF94A3B8)
                            ),
                            enabled = canSubmitDate && !isSubmittingDonation
                        ) {
                            if (isSubmittingDonation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = if (canSubmitDate) Color.White else Color(0xFF94A3B8),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "রক্তদানের তারিখ জমা দিন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (canSubmitDate) Color.White else Color(0xFF94A3B8)
                                )
                            }
                        }

                        // জমা নিয়ম ও সতর্কতা বার্তা
                        if (hasPendingRequest) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "একটি অনুরোধ অপেক্ষমাণ আছে",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = Color(0xFFD97706),
                                modifier = Modifier.testTag("pending_request_notice")
                            )
                        } else if (!isEligible) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "পরবর্তী উপযুক্ত তারিখ: ${memberData?.nextEligibleDonationDateDisplay ?: "অপেক্ষমাণ"}",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = Color(0xFFDC2626),
                                modifier = Modifier.testTag("not_eligible_notice")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
