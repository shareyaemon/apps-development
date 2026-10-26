package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BloodDonor
import com.example.model.EventItem
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
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticesBottomSheet(
    notices: List<NoticeItem> = emptyList(),
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundLight,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "সকল নোটিশ",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NavyPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NavyPrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (notices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "এখনো কোনো নোটিশ নেই",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = SubtitleGray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    items(notices) { notice ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (notice.date.isNotBlank()) {
                                    Text(
                                        text = notice.date,
                                        fontFamily = HindSiliguri,
                                        fontSize = 12.sp,
                                        color = SubtitleGray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text(
                                    text = notice.title,
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NavyPrimary
                                )
                                if (notice.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = notice.description,
                                        fontFamily = HindSiliguri,
                                        fontSize = 13.sp,
                                        color = SubtitleGray,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val activities = remember {
        listOf(
            EventItem(
                id = "1",
                title = "বার্ষিক কুরআন মাহফিল ও পুরস্কার বিতরণী",
                category = "আসন্ন কার্যক্রম",
                date = "২৫ নভেম্বর ২০২৬",
                location = "স্বর্ণটিলা ঈদগাহ মাঠ প্রাঙ্গণ",
                description = "বিশিষ্ট আন্তর্জাতিক খ্যাতিসম্পন্ন মুফাসসিরগণ তাশরীফ আনবেন। সকল ধর্মপ্রাণ মুসলিম ভাইকে উপস্থিত থাকার জন্য দাওয়াত।"
            ),
            EventItem(
                id = "2",
                title = "শীতবস্ত্র বিতরণ ও চিকিৎসা ক্যাম্প",
                category = "সামাজিক কার্যক্রম",
                date = "১৫ ডিসেম্বর ২০২৬",
                location = "স্বর্ণটিলা ও আশপাশের এলাকা",
                description = "অসহায় ও শীতার্ত মানুষের মাঝে কম্বল ও গরম কাপড় বিতরণ কর্মসূচি।"
            ),
            EventItem(
                id = "3",
                title = "রমজান খাদ্য সহায়তা ও ইফতার মাহফিল",
                category = "বার্ষিক সেবা",
                date = "রমজান ১৪৪৮ হিজরি",
                location = "স্বর্ণটিলা জামে মসজিদ",
                description = "দরিদ্র পরিবারের মাঝে পুরো মাসের খাদ্য সামগ্রী ও নিত্যপ্রয়োজনীয় উপহার সামগ্রী বিতরণ।"
            )
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundLight,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "সংঘের কার্যক্রমসমূহ",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = NavyPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NavyPrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                items(activities) { event ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(SoftSkyBlue, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = event.category,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        color = NavyPrimary
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = SubtitleGray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = event.date,
                                        fontFamily = HindSiliguri,
                                        fontSize = 12.sp,
                                        color = SubtitleGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = event.title,
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NavyPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = SubtitleGray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = event.location,
                                    fontFamily = HindSiliguri,
                                    fontSize = 12.sp,
                                    color = SubtitleGray
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = event.description,
                                fontFamily = HindSiliguri,
                                fontSize = 13.sp,
                                color = SubtitleGray,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodDonorsBottomSheet(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedGroup by remember { mutableStateOf("সকল") }

    val bloodGroups = listOf("সকল", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

    val allDonors = remember {
        listOf(
            BloodDonor("1", "মুহাম্মদ আব্দুল্লাহ", "O+", "01711000001", "স্বর্ণটিলা", "৩ মাস পূর্বে"),
            BloodDonor("2", "তানভীর আহমেদ", "A+", "01811000002", "স্বর্ণটিলা বাজার", "১ মাস পূর্বে"),
            BloodDonor("3", "জুবায়ের হোসেন", "B+", "01911000003", "উত্তর স্বর্ণটিলা", "৪ মাস পূর্বে"),
            BloodDonor("4", "সাকিব আল হাসান", "O-", "01611000004", "দক্ষিণ পাড়া", "২ মাস পূর্বে"),
            BloodDonor("5", "রাকিবুল ইসলাম", "AB+", "01722000005", "মধ্যপাড়া", "৫ মাস পূর্বে"),
            BloodDonor("6", "মাহমুদুল হাসান", "B-", "01833000006", "স্বর্ণটিলা রোড", "৩ মাস পূর্বে")
        )
    }

    val filteredDonors = if (selectedGroup == "সকল") allDonors else allDonors.filter { it.bloodGroup == selectedGroup }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundLight,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(SoftRedBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = BloodRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "রক্তদাতা ডিরেক্টরি",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = NavyPrimary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = NavyPrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Blood group filter chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(bloodGroups) { group ->
                    val isSelected = selectedGroup == group
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) BloodRed else Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) BloodRed else CardBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedGroup = group }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = group,
                            fontFamily = HindSiliguri,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (isSelected) Color.White else NavyPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                items(filteredDonors) { donor ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(SoftRedBg, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = donor.bloodGroup,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = BloodRed
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = donor.name,
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = NavyPrimary
                                    )
                                    Text(
                                        text = "${donor.area} • শেষ রক্তদান: ${donor.lastDonation}",
                                        fontFamily = HindSiliguri,
                                        fontSize = 12.sp,
                                        color = SubtitleGray
                                    )
                                }
                            }

                            // Call button
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(SoftSkyBlue, CircleShape)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_DIAL, "tel:${donor.phone}".toUri())
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "ফোন নম্বর: ${donor.phone}", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call Donor",
                                    tint = NavyPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
