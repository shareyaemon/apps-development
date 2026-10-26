package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.core.net.toUri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HindSiliguri

private data class DonorItem(
    val id: String,
    val name: String,
    val bloodGroup: String,
    val statusText: String, // "প্রস্তুত" or "অপেক্ষমাণ"
    val isReady: Boolean,
    val phone: String = "01712345678",
    val lastDonationDate: String = "সর্বশেষ রক্তদান: [তারিখ]"
)

@Composable
fun BloodDonorsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onNavigateBack()
    }

    // ডিফল্টে O+ নির্বাচিত
    var selectedGroup by remember { mutableStateOf("O+") }

    // ৮টি রক্তের গ্রুপ: ৪টি করে দুই সারির গ্রিডে
    val row1 = listOf("A+", "A−", "B+", "B−")
    val row2 = listOf("O+", "O−", "AB+", "AB−")

    // সকল গ্রুপের জন্য ডেমো দাতা তালিকা
    val donorsMap = remember {
        mapOf(
            "O+" to listOf(
                DonorItem("1", "মুহাম্মদ আব্দুল্লাহ", "O+", "প্রস্তুত", true),
                DonorItem("2", "শাহরিয়ার আলম", "O+", "অপেক্ষমাণ", false),
                DonorItem("3", "তানভীর আহমেদ", "O+", "প্রস্তুত", true)
            ),
            "A+" to listOf(
                DonorItem("4", "রাকিবুল হাসান", "A+", "প্রস্তুত", true),
                DonorItem("5", "মাহবুবুর রহমান", "A+", "অপেক্ষমাণ", false),
                DonorItem("6", "জসিম উদ্দিন", "A+", "প্রস্তুত", true)
            ),
            "A−" to listOf(
                DonorItem("7", "ইমরান হোসেন", "A−", "প্রস্তুত", true),
                DonorItem("8", "তারেক জামান", "A−", "অপেক্ষমাণ", false)
            ),
            "B+" to listOf(
                DonorItem("9", "সাইফুল ইসলাম", "B+", "প্রস্তুত", true),
                DonorItem("10", "আরিফুল হক", "B+", "প্রস্তুত", true),
                DonorItem("11", "মোস্তফা কামাল", "B+", "অপেক্ষমাণ", false)
            ),
            "B−" to listOf(
                DonorItem("12", "জাহিদ হাসান", "B−", "প্রস্তুত", true),
                DonorItem("13", "কামরুল ইসলাম", "B−", "অপেক্ষমাণ", false)
            ),
            "O−" to listOf(
                DonorItem("14", "ফারুক আহমেদ", "O−", "অপেক্ষমাণ", false),
                DonorItem("15", "রফিকুল ইসলাম", "O−", "প্রস্তুত", true)
            ),
            "AB+" to listOf(
                DonorItem("16", "নাজমুল হোসেন", "AB+", "প্রস্তুত", true),
                DonorItem("17", "আশরাফুল আলম", "AB+", "অপেক্ষমাণ", false)
            ),
            "AB−" to listOf(
                DonorItem("18", "ফখরুল হাসান", "AB−", "অপেক্ষমাণ", false),
                DonorItem("19", "শামীম আহমেদ", "AB−", "প্রস্তুত", true)
            )
        )
    }

    val currentDonors = donorsMap[selectedGroup] ?: emptyList()

    Scaffold(
        modifier = Modifier.testTag("blood_donors_screen"),
        containerColor = Color(0xFFF4F7FC),
        bottomBar = {
            // একেবারে নিচে স্থির লাল (#B91C1C) বাটন, উচ্চতা ৫৬dp, কর্নার ১৬dp, রক্তের ফোঁটা আইকন ও সাদা Bold "জরুরি রক্তের অনুরোধ"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "জরুরি রক্তের অনুরোধ প্রক্রিয়াধীন রয়েছে",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("emergency_blood_request_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB91C1C),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "রক্তের ফোঁটা",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "জরুরি রক্তের অনুরোধ",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FC))
                .padding(innerPadding)
        ) {
            // ১. সাদা হেডার: ব্যাক তীর ও "রক্তদাতা তালিকা" ২২sp Bold (মোট লেখা নেই)
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
                            .testTag("blood_donors_back_button")
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
                        text = "রক্তদাতা তালিকা",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1B2559),
                        modifier = Modifier.testTag("blood_donors_title")
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ২. রক্তের গ্রুপ ৮টি বাটন ৪টি করে দুই সারির গ্রিডে
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // সারি ১: A+, A−, B+, B−
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row1.forEach { group ->
                            BloodGroupGridButton(
                                modifier = Modifier.weight(1f),
                                group = group,
                                isSelected = selectedGroup == group,
                                onClick = { selectedGroup = group }
                            )
                        }
                    }

                    // সারি ২: O+, O−, AB+, AB−
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row2.forEach { group ->
                            BloodGroupGridButton(
                                modifier = Modifier.weight(1f),
                                group = group,
                                isSelected = selectedGroup == group,
                                onClick = { selectedGroup = group }
                            )
                        }
                    }
                }

                // ৩. নির্বাচিত গ্রুপের দাতা তালিকা (সাদা কার্ডে)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("donors_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        currentDonors.forEachIndexed { index, donor ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                                    .testTag("donor_row_${donor.id}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // বাঁয়ে হালকা লাল গোল-কর্নার বক্সে লাল Bold রক্তের গ্রুপ
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFEE2E2)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = donor.bloodGroup,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFFB91C1C)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // মাঝে নাম ও "সর্বশেষ রক্তদান: [তারিখ]"
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = donor.name,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF1B2559)
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = donor.lastDonationDate,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // ডানে ব্যাজ (প্রস্তুত: হালকা সবুজ, অপেক্ষমাণ: হালকা হলুদ)
                                if (donor.isReady) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFDCFCE7))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = donor.statusText,
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF166534)
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFFEF3C7))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = donor.statusText,
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF92400E)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                // নেভি ৪৪dp বর্গ কল বাটন (সাদা ফোন আইকন)
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF1B2559))
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                                data = "tel:${donor.phone}".toUri()
                                            }
                                            context.startActivity(intent)
                                        }
                                        .testTag("call_donor_${donor.id}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "কল করুন",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            if (index < currentDonors.size - 1) {
                                HorizontalDivider(
                                    color = Color(0xFFE2E8F0),
                                    thickness = 0.8.dp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }

                // স্ক্রল এলাকার নিচে বাটনের জন্য পর্যাপ্ত স্পেস যেন বাটন কোনো কার্ডকে না ঢেকে দেয়
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
private fun BloodGroupGridButton(
    modifier: Modifier = Modifier,
    group: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFFFEE2E2) else Color.White)
            .border(
                border = BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) Color(0xFFB91C1C) else Color(0xFFE2E8F0)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .testTag("blood_group_btn_$group"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = group,
            fontFamily = HindSiliguri,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 15.sp,
            color = if (isSelected) Color(0xFFB91C1C) else Color(0xFF1B2559)
        )
    }
}
