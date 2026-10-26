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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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

private data class SimpleTransaction(
    val title: String,
    val amount: String,
    val isIncome: Boolean
)

@Composable
fun FinancesScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedMonth by remember { mutableStateOf("অক্টোবর") }
    var monthDropdownExpanded by remember { mutableStateOf(false) }

    val months = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )

    BackHandler {
        onNavigateBack()
    }

    val transactions = remember {
        listOf(
            SimpleTransaction("মাসিক হাদিয়া সংগ্রহ", "+ ৳ ১০,০০০", true),
            SimpleTransaction("সাধারণ অনুদান", "+ ৳ ২,৫০০", true),
            SimpleTransaction("মাহফিল প্রস্তুতি খরচ", "− ৳ ৫,০০০", false),
            SimpleTransaction("মসজিদ সাজসজ্জা", "− ৳ ২,০০০", false),
            SimpleTransaction("কবরস্থান পরিষ্কার", "− ৳ ১,২০০", false)
        )
    }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.testTag("finances_screen"),
        // ব্যাকগ্রাউন্ড #F4F7FC, বটম নেভ নেই
        containerColor = Color(0xFFF4F7FC),
        bottomBar = {
            // একেবারে নিচে স্থায়ীভাবে দৃশ্যমান সাদা আউটলাইন বাটন
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "$selectedMonth মাসের হিসাব ডাউনলোড সম্পন্ন হয়েছে",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("download_monthly_report_button"),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, Color(0xFF1B2559)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1B2559)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "ডাউনলোড",
                        tint = Color(0xFF1B2559),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "মাসিক হিসাব ডাউনলোড",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1B2559)
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
            // সাদা হেডার: বাঁয়ে ব্যাক তীর, শিরোনাম "আয়-ব্যয় হিসাব" ২২sp Bold নেভি (#1B2559), ডানে "অক্টোবর ⌄" বাটন
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
                                .testTag("finances_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "ফিরে যান",
                                tint = Color(0xFF1B2559),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // শিরোনাম "আয়-ব্যয় হিসাব" ২২sp Bold
                        Text(
                            text = "আয়-ব্যয় হিসাব",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF1B2559)
                        )
                    }

                    // ডানে "অক্টোবর" বাটনের লেখা ১৬sp Medium, বাটন উচ্চতা ৪৪dp
                    Box {
                        Row(
                            modifier = Modifier
                                .height(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .clickable { monthDropdownExpanded = true }
                                .padding(horizontal = 14.dp)
                                .testTag("month_dropdown_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedMonth,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color(0xFF1B2559)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "মাস নির্বাচন",
                                tint = Color(0xFF1B2559),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = monthDropdownExpanded,
                            onDismissRequest = { monthDropdownExpanded = false }
                        ) {
                            months.forEach { month ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = month,
                                            fontFamily = HindSiliguri,
                                            fontWeight = if (month == selectedMonth) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 15.sp,
                                            color = Color(0xFF1B2559)
                                        )
                                    },
                                    onClick = {
                                        selectedMonth = month
                                        monthDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // হেডার নিচে হালকা বর্ডার
            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            // পুরো কনটেন্ট একটি স্ক্রলযোগ্য Column-এ
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(2.dp))

                // ৩টি কার্ড পাশাপাশি সমান প্রস্থে, কর্নার ১৬dp, ভেতরে লেবেল ১৪sp ও নিচে টাকা ২২sp Bold
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // ১. মোট আয় হালকা সবুজ (#DCFCE7) ও গাঢ় সবুজ লেখা "৳ ১২,৫০০"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 10.dp, vertical = 14.dp)
                            .testTag("total_income_card")
                    ) {
                        Column {
                            Text(
                                text = "মোট আয়",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color(0xFF166534)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "৳ ১২,৫০০",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color(0xFF166534)
                            )
                        }
                    }

                    // ২. মোট ব্যয় হালকা লাল (#FEE2E2) ও গাঢ় লাল (#B91C1C) "৳ ৮,২০০"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFFEE2E2))
                            .padding(horizontal = 10.dp, vertical = 14.dp)
                            .testTag("total_expense_card")
                    ) {
                        Column {
                            Text(
                                text = "মোট ব্যয়",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color(0xFFB91C1C)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "৳ ৮,২০০",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color(0xFFB91C1C)
                            )
                        }
                    }

                    // ৩. জমা হালকা নীল (#E6F5FD) ও নেভি "৳ ৪,৩০০" (বাংলা সংখ্যায়)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE6F5FD))
                            .padding(horizontal = 10.dp, vertical = 14.dp)
                            .testTag("balance_card")
                    ) {
                        Column {
                            Text(
                                text = "জমা",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color(0xFF1B2559)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "৳ ৪,৩০০",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color(0xFF1B2559)
                            )
                        }
                    }
                }

                // সাদা কার্ড (কর্নার ২০dp, হালকা বর্ডার): শিরোনাম "আয় বনাম ব্যয়" ১৮sp Bold নেভি। শুধু ২টি বার
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("income_expense_comparison_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "আয় বনাম ব্যয়",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1B2559)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // শুধুমাত্র ২টি বার: সবুজ (#16A34A) আয় ও গাঢ় লাল (#B91C1C) ব্যয় (কোনো টাকার লেখা উপরে নয়)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // ১. সবুজ বার (#16A34A)
                            Box(
                                modifier = Modifier
                                    .width(64.dp)
                                    .height(135.dp)
                                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                    .background(Color(0xFF16A34A))
                            )

                            // ২. গাঢ় লাল বার (#B91C1C)
                            Box(
                                modifier = Modifier
                                    .width(64.dp)
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                    .background(Color(0xFFB91C1C))
                            )
                        }

                        // নিচে ধূসর বেসলাইন
                        HorizontalDivider(
                            color = Color(0xFFCBD5E1),
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // "আয়" ও "ব্যয়" লেবেল
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = "আয়",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559),
                                modifier = Modifier.width(64.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Text(
                                text = "ব্যয়",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559),
                                modifier = Modifier.width(64.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                // "সাম্প্রতিক লেনদেন" ১৮sp Bold নেভি
                Text(
                    text = "সাম্প্রতিক লেনদেন",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B2559),
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )

                // নিচে একটি সাদা কার্ডে ৫টি সারি (সারির উচ্চতা ৭০dp, মাঝে হালকা ডিভাইডার)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recent_transactions_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        transactions.forEachIndexed { index, tx ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(horizontal = 16.dp)
                                    .testTag("tx_row_${tx.title}"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // বাঁয়ে ৪৪dp গোল আইকন (সবুজ ব্যাকগ্রাউন্ডে ↑ আয়ের জন্য, লাল ব্যাকগ্রাউন্ডে ↓ ব্যয়ের জন্য)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (tx.isIncome) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (tx.isIncome) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                            contentDescription = if (tx.isIncome) "আয়" else "ব্যয়",
                                            tint = if (tx.isIncome) Color(0xFF16A34A) else Color(0xFFB91C1C),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    // মাঝে ১৬sp লেখা
                                    Text(
                                        text = tx.title,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp,
                                        color = Color(0xFF1B2559)
                                    )
                                }

                                // ডানে ১৬sp Bold টাকা (সবুজ / লাল)
                                Text(
                                    text = tx.amount,
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (tx.isIncome) Color(0xFF16A34A) else Color(0xFFB91C1C)
                                )
                            }

                            if (index < transactions.size - 1) {
                                HorizontalDivider(
                                    color = Color(0xFFE2E8F0),
                                    thickness = 0.8.dp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }

                // স্ক্রল এলাকার নিচে বাটনের উচ্চতা (৫৬dp) ও ১৬dp ফাঁকা padding যোগ (মোট ৭২dp), যেন বাটন কোনো লেনদেন ঢেকে না দেয়
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}
