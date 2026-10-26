package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HadiyaPaymentState
import com.example.model.MemberBottomTab
import com.example.model.MonthHadiya
import com.example.ui.components.MemberBottomNav
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BloodRed
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SoftRedBg
import com.example.ui.theme.SubtitleGray
import com.example.ui.theme.SuccessGreen

@Composable
fun HadiyaTrackingScreen(
    onNavigateBack: () -> Unit,
    onNavigatePayOnline: () -> Unit,
    onNavigateReceipt: () -> Unit,
    onTabSelected: (MemberBottomTab) -> Unit
) {
    val context = LocalContext.current
    var selectedYear by remember { mutableStateOf("২০২৬") }

    BackHandler {
        onNavigateBack()
    }

    val months = remember {
        listOf(
            MonthHadiya("জানুয়ারি", "৳ ২০০", HadiyaPaymentState.PAID, "✓ পরিশোধিত"),
            MonthHadiya("ফেব্রুয়ারি", "৳ ২০০", HadiyaPaymentState.PAID, "✓ পরিশোধিত"),
            MonthHadiya("মার্চ", "৳ ২০০", HadiyaPaymentState.PAID, "✓ পরিশোধিত"),
            MonthHadiya("এপ্রিল", "৳ ২০০", HadiyaPaymentState.DUE, "✕ বকেয়া"),
            MonthHadiya("মে", "৳ ২০০", HadiyaPaymentState.DUE, "✕ বকেয়া"),
            MonthHadiya("জুন", "৳ ২০০", HadiyaPaymentState.PARTIAL, "— আংশিক ৳১০০"),
            MonthHadiya("জুলাই", "৳ ২০০", HadiyaPaymentState.UPCOMING, "আগামী"),
            MonthHadiya("আগস্ট", "৳ ২০০", HadiyaPaymentState.UPCOMING, "আগামী"),
            MonthHadiya("সেপ্টেম্বর", "৳ ২০০", HadiyaPaymentState.UPCOMING, "আগামী"),
            MonthHadiya("অক্টোবর", "৳ ২০০", HadiyaPaymentState.UPCOMING, "আগামী"),
            MonthHadiya("নভেম্বর", "৳ ২০০", HadiyaPaymentState.UPCOMING, "আগামী"),
            MonthHadiya("ডিসেম্বর", "৳ ২০০", HadiyaPaymentState.UPCOMING, "আগামী")
        )
    }

    Scaffold(
        modifier = Modifier.testTag("hadiya_tracking_screen"),
        bottomBar = {
            MemberBottomNav(
                selectedTab = MemberBottomTab.HADIYA,
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
            // Top App Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.testTag("hadiya_tracking_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = NavyPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "মাসিক হাদিয়া ট্র্যাকিং",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = NavyPrimary
                        )
                    }

                    // Year Selector Pill
                    Box(
                        modifier = Modifier
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                Toast.makeText(context, "বছর: ২০২৬", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("year_selector_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedYear,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NavyPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = NavyPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top Summary Cards (মোট বকেয়া & মোট পরিশোধিত)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // মোট বকেয়া Card (Soft Red)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("total_due_card"),
                        colors = CardDefaults.cardColors(containerColor = SoftRedBg),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "মোট বকেয়া",
                                fontFamily = HindSiliguri,
                                fontSize = 13.sp,
                                color = BloodRed
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "৳ ৫০০",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = BloodRed
                            )
                        }
                    }

                    // মোট পরিশোধিত Card (Soft Green)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("total_paid_card"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "মোট পরিশোধিত",
                                fontFamily = HindSiliguri,
                                fontSize = 13.sp,
                                color = SuccessGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "৳ ৭০০",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = SuccessGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 12 Months Grid (3 columns x 4 rows)
                val rows = months.chunked(3)
                rows.forEach { rowMonths ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowMonths.forEach { item ->
                            MonthStatusBox(
                                item = item,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Two Action Buttons: "রসিদ ডাউনলোড" & "অনলাইনে পরিশোধ করুন"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateReceipt,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("download_receipt_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = NavyPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, NavyPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = NavyPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "রসিদ ডাউনলোড",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NavyPrimary
                        )
                    }

                    Button(
                        onClick = onNavigatePayOnline,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp)
                            .testTag("pay_online_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "অনলাইনে পরিশোধ করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun MonthStatusBox(
    item: MonthHadiya,
    modifier: Modifier = Modifier
) {
    val borderColor = when (item.state) {
        HadiyaPaymentState.PAID -> SuccessGreen
        HadiyaPaymentState.DUE -> BloodRed
        HadiyaPaymentState.PARTIAL -> Color(0xFFD97706)
        HadiyaPaymentState.UPCOMING -> Color(0xFF94A3B8)
    }

    val statusColor = when (item.state) {
        HadiyaPaymentState.PAID -> SuccessGreen
        HadiyaPaymentState.DUE -> BloodRed
        HadiyaPaymentState.PARTIAL -> Color(0xFFD97706)
        HadiyaPaymentState.UPCOMING -> SubtitleGray
    }

    Box(
        modifier = modifier
            .height(98.dp)
            .background(Color.White, RoundedCornerShape(14.dp))
            .then(
                if (item.state == HadiyaPaymentState.UPCOMING) {
                    Modifier
                } else {
                    Modifier.border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                }
            )
            .testTag("month_box_${item.monthName}"),
        contentAlignment = Alignment.Center
    ) {
        // If upcoming, draw dashed border
        if (item.state == HadiyaPaymentState.UPCOMING) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = Stroke(
                    width = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
                drawRoundRect(
                    color = Color(0xFFCBD5E1),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(14.dp.toPx()),
                    style = stroke
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            Text(
                text = item.monthName,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = NavyPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.amount,
                fontFamily = HindSiliguri,
                fontSize = 12.sp,
                color = SubtitleGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.statusText,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = statusColor
            )
        }
    }
}
