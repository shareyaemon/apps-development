package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SoftSkyBlue
import com.example.ui.theme.SubtitleGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnlinePaymentScreen(
    onNavigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isProcessing by remember { mutableStateOf(false) }
    var checkApril by remember { mutableStateOf(true) }
    var checkMay by remember { mutableStateOf(true) }
    var checkJune by remember { mutableStateOf(true) }
    var selectedMethod by remember { mutableStateOf("বিকাশ") }

    val totalAmount = (if (checkApril) 200 else 0) +
            (if (checkMay) 200 else 0) +
            (if (checkJune) 100 else 0)

    val paymentMethods = listOf("বিকাশ", "নগদ", "ব্যাংক কার্ড")

    BackHandler {
        onNavigateBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("online_payment_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("online_payment_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NavyPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "অনলাইনে পরিশোধ",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Section 1: "কোন মাসের হাদিয়া দেবেন?"
                Text(
                    text = "কোন মাসের হাদিয়া দেবেন?",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column {
                        // Month 1: এপ্রিল ২০২৬
                        MonthCheckRow(
                            month = "এপ্রিল ২০২৬",
                            amount = "৳ ২০০",
                            isChecked = checkApril,
                            onToggle = { checkApril = !checkApril },
                            tag = "check_april"
                        )

                        HorizontalDivider(color = CardBorder.copy(alpha = 0.6f), thickness = 0.8.dp)

                        // Month 2: মে ২০২৬
                        MonthCheckRow(
                            month = "মে ২০২৬",
                            amount = "৳ ২০০",
                            isChecked = checkMay,
                            onToggle = { checkMay = !checkMay },
                            tag = "check_may"
                        )

                        HorizontalDivider(color = CardBorder.copy(alpha = 0.6f), thickness = 0.8.dp)

                        // Month 3: জুন ২০২৬ (বাকি অংশ)
                        MonthCheckRow(
                            month = "জুন ২০২৬ (বাকি অংশ)",
                            amount = "৳ ১০০",
                            isChecked = checkJune,
                            onToggle = { checkJune = !checkJune },
                            tag = "check_june"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 2: "পেমেন্ট মাধ্যম"
                Text(
                    text = "পেমেন্ট মাধ্যম",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column {
                        paymentMethods.forEachIndexed { index, method ->
                            val isSelected = selectedMethod == method
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) SoftSkyBlue.copy(alpha = 0.6f) else Color.White
                                    )
                                    .clickable { selectedMethod = method }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .testTag("method_$method"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedMethod = method },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = NavyPrimary,
                                        unselectedColor = SubtitleGray
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = method,
                                    fontFamily = HindSiliguri,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 15.sp,
                                    color = NavyPrimary
                                )
                            }

                            if (index < paymentMethods.size - 1) {
                                HorizontalDivider(color = CardBorder.copy(alpha = 0.6f), thickness = 0.8.dp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 3: Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "মোট হাদিয়া",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = SubtitleGray
                            )
                            Text(
                                text = "৳ $totalAmount",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = NavyPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "চার্জ",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = SubtitleGray
                            )
                            Text(
                                text = "৳ ০",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = NavyPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = CardBorder, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "পরিশোধযোগ্য",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NavyPrimary
                            )
                            Text(
                                text = "৳ $totalAmount",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = NavyPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Button: "৳ পরিশোধ করুন" (Shows notice that online payment is coming soon)
            val context = androidx.compose.ui.platform.LocalContext.current
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding()
            ) {
                Button(
                    onClick = {
                        android.widget.Toast.makeText(context, "অনলাইন পেমেন্ট শীঘ্রই চালু হবে", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_payment_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "৳ ${com.example.data.FirebaseManager.toBengaliDigits(totalAmount.toString())} পরিশোধ করুন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MonthCheckRow(
    month: String,
    amount: String,
    isChecked: Boolean,
    onToggle: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Checkbox icon / box
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (isChecked) NavyPrimary else Color.White,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (isChecked) NavyPrimary else SubtitleGray,
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = month,
                fontFamily = HindSiliguri,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = NavyPrimary
            )
        }

        Text(
            text = amount,
            fontFamily = HindSiliguri,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = NavyPrimary
        )
    }
}
