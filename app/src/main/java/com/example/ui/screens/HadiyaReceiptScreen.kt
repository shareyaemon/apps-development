package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary

@Composable
fun HadiyaReceiptScreen(
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onNavigateHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("hadiya_receipt_screen")
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
                        onClick = onNavigateHome,
                        modifier = Modifier.testTag("receipt_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NavyPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "হাদিয়া রসিদ",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Scrollable Body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge Circle & Text
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(Color(0xFFDCFCE7), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color(0xFF166534),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "পেমেন্ট সফল হয়েছে",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF166534)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Receipt Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("receipt_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header with Authentic Club Logo and Name
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.club_logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "হিলফুল ফুজুল স্বর্ণটিলা যুব সংঘ",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = "হাদিয়া জমার রসিদ",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 13.sp,
                                    color = Color(0xFF475569)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.height(14.dp))

                        // Receipt metadata with dark gray labels (#475569) and navy semi-bold values (#1B2559)
                        ReceiptRow(label = "রসিদ নং", value = "HF-REC-2026-1042")
                        ReceiptRow(label = "তারিখ", value = "১৬ অক্টোবর ২০২৬")
                        ReceiptRow(label = "সদস্য", value = "মুহাম্মদ আব্দুল্লাহ")
                        ReceiptRow(label = "সদস্য আইডি", value = "HF-2026-042")
                        ReceiptRow(label = "মাধ্যম", value = "বিকাশ")
                        ReceiptRow(label = "লেনদেন আইডি", value = "TRX-89427501")

                        Spacer(modifier = Modifier.height(14.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.height(14.dp))

                        // Breakdown of months
                        ReceiptRow(label = "এপ্রিল ২০২৬", value = "৳ ২০০")
                        ReceiptRow(label = "মে ২০২৬", value = "৳ ২০০")
                        ReceiptRow(label = "জুন ২০২৬ (বাকি অংশ)", value = "৳ ১০০")

                        Spacer(modifier = Modifier.height(14.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.height(14.dp))

                        // Total amount (Bold and Larger)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "মোট পরিশোধ",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = NavyPrimary
                            )
                            Text(
                                text = "৳ ৫০০",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = NavyPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Bottom Two Action Buttons: "PDF ডাউনলোড করুন" & "হোমে ফিরুন"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "রসিদ PDF ডাউনলোড সম্পন্ন হয়েছে!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("download_pdf_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PDF ডাউনলোড করুন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFFFFFFFF)
                    )
                }

                OutlinedButton(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("return_home_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = NavyPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, NavyPrimary)
                ) {
                    Text(
                        text = "হোমে ফিরুন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NavyPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = HindSiliguri,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color(0xFF475569)
        )
        Text(
            text = value,
            fontFamily = HindSiliguri,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = NavyPrimary
        )
    }
}

@Composable
fun DashedDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.5.dp)
    ) {
        val strokeWidthPx = 1.5.dp.toPx()
        drawLine(
            color = Color(0xFFCBD5E1),
            start = androidx.compose.ui.geometry.Offset(0f, strokeWidthPx / 2),
            end = androidx.compose.ui.geometry.Offset(size.width, strokeWidthPx / 2),
            strokeWidth = strokeWidthPx,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
        )
    }
}
