package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.core.net.toUri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SoftSkyBlue
import com.example.ui.theme.SubtitleGray

@Composable
fun AboutOrganizationScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onNavigateBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("about_organization_screen")
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
                        modifier = Modifier.testTag("about_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NavyPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "সংগঠন পরিচিতি",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Card: লোগো, নাম, "প্রতিষ্ঠা: ২০১৭"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.club_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .testTag("about_logo"),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "হিলফুল ফুজুল স্বর্ণটিলা যুব সংঘ",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = NavyPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "প্রতিষ্ঠা: ২০১৭",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SkyBlueAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "আমাদের কার্যক্রম" Card (১-৬ নম্বর তালিকা)
                InfoSectionCard(
                    title = "আমাদের কার্যক্রম",
                    icon = Icons.Default.Info
                ) {
                    val activitiesList = listOf(
                        "১. স্বেচ্ছায় রক্তদান কর্মসূচি ও রক্তদাতা ডাটাবেজ পরিচালনা",
                        "২. প্রতি বছর ঐতিহাসিক তাফসীরুল কুরআন ও কিরাত মাহফিল",
                        "৩. পবিত্র রমজান মাসে খাদ্য সহায়তা ও ইফতার সামগ্রী বিতরণ",
                        "৪. তীব্র শীতে অসহায় শীতার্ত মানুষের মাঝে কম্বল ও গরম কাপড় বিতরণ",
                        "৫. গরিব শিক্ষার্থীদের আর্থিক সহায়তা ও শিক্ষা উপকরণ বিতরণ",
                        "৬. জরুরি চিকিৎসা সহায়তা ও সামাজিক কল্যাণমূলক কাজ"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        activitiesList.forEach { activity ->
                            Text(
                                text = activity,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = SubtitleGray,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "যোগাযোগ" Card (ফোন ও ঠিকানা প্লেসহোল্ডার)
                InfoSectionCard(
                    title = "যোগাযোগ",
                    icon = Icons.Default.Call
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_DIAL, "tel:01700000000".toUri())
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "ফোন: ০১৭XXXXXXXX", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = SkyBlueAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "মোবাইল: ০১৭XXXXXXXX",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = NavyPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SkyBlueAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "ঠিকানা: স্বর্ণটিলা, পোস্ট ও উপজেলা",
                                fontFamily = HindSiliguri,
                                fontSize = 14.sp,
                                color = SubtitleGray,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun InfoSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(SoftSkyBlue, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = title,
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = NavyPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            content()
        }
    }
}
