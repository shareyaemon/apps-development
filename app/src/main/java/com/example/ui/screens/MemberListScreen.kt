package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FirebaseManager
import com.example.data.MemberData
import com.example.model.MemberBottomTab
import com.example.ui.components.MemberBottomNav
import com.example.ui.theme.HindSiliguri

private data class MemberItemData(
    val id: String,
    val initial: String,
    val name: String,
    val roleAndBlood: String,
    val isActive: Boolean,
    val searchTerms: String
)

@Composable
fun MemberListScreen(
    onNavigateHome: () -> Unit,
    onTabSelected: (MemberBottomTab) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("সব") } // "সব", "সক্রিয়", "নিষ্ক্রিয়"

    BackHandler {
        onNavigateHome()
    }

    val context = LocalContext.current
    var rawMembers by remember { mutableStateOf<List<MemberData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val reg = FirebaseManager.listenToMembers(
            context = context,
            onSuccess = { list ->
                rawMembers = list
                isLoading = false
            },
            onError = {
                rawMembers = emptyList()
                isLoading = false
            }
        )
        onDispose { reg?.remove() }
    }

    val allMembers = remember(rawMembers) {
        rawMembers.map { member ->
            val roleBn = FirebaseManager.getRoleBengaliName(member.role)
            val bloodPart = if (member.bloodGroup.isNotBlank() && member.bloodGroup != "যোগ করা হয়নি") " • ${member.bloodGroup}" else ""
            MemberItemData(
                id = member.docId.ifBlank { member.memberId },
                initial = member.name.trim().take(1).ifBlank { "স" },
                name = member.name,
                roleAndBlood = "$roleBn$bloodPart",
                isActive = member.isActive,
                searchTerms = "${member.name} $roleBn ${member.bloodGroup} ${member.memberId} ${member.mobile}"
            )
        }
    }

    // ফিল্টার ও সার্চ লজিক
    val filteredMembers = remember(searchQuery, selectedFilter, allMembers) {
        allMembers.filter { member ->
            val matchesFilter = when (selectedFilter) {
                "সক্রিয়" -> member.isActive
                "নিষ্ক্রিয়" -> !member.isActive
                else -> true
            }

            val query = searchQuery.trim().lowercase()
            val matchesSearch = query.isEmpty() ||
                    member.name.lowercase().contains(query) ||
                    member.roleAndBlood.lowercase().contains(query) ||
                    member.searchTerms.lowercase().contains(query)

            matchesFilter && matchesSearch
        }
    }

    Scaffold(
        modifier = Modifier.testTag("member_list_screen"),
        containerColor = Color(0xFFF4F7FC),
        bottomBar = {
            MemberBottomNav(
                selectedTab = MemberBottomTab.MEMBERS,
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
            // ১. সাদা হেডার: বাঁয়ে "সদস্য তালিকা" ২২sp Bold নেভি, ডানে "মোট: ৭ জন" ১৪sp ধূসর (ব্যাক তীর নেই)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "সদস্য তালিকা",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1B2559),
                        modifier = Modifier.testTag("member_list_title")
                    )

                    // ডানে "মোট: ৭ জন" ১৪sp ধূসর (বাংলা সংখ্যায়)
                    Text(
                        text = "মোট: ${filteredMembers.size.toBengaliDigits()} জন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))

                    // ২. সার্চ বার: সাদা, উচ্চতা ৫০dp, কর্নার ১৬dp, হালকা বর্ডার, বাঁয়ে সার্চ আইকন, প্লেসহোল্ডার "নাম বা আইডি দিয়ে খুঁজুন" ১৫sp ধূসর
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "সার্চ",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "নাম বা আইডি দিয়ে খুঁজুন",
                                        fontFamily = HindSiliguri,
                                        fontSize = 15.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontFamily = HindSiliguri,
                                        fontSize = 15.sp,
                                        color = Color(0xFF1B2559)
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF1B2559)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("member_search_input")
                                )
                            }

                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "মুছুন",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ৩. ফিল্টার চিপ pill আকৃতির, উচ্চতা ৪০dp, ১৪sp: "সব" (সিলেক্টেড: নেভি ব্যাকগ্রাউন্ড, সাদা Bold), "সক্রিয়" ও "নিষ্ক্রিয়" (সাদা ব্যাকগ্রাউন্ড, নেভি লেখা, হালকা বর্ডার)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("সব", "সক্রিয়", "নিষ্ক্রিয়").forEach { option ->
                            val isSelected = selectedFilter == option
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) Color(0xFF1B2559) else Color.White)
                                    .then(
                                        if (!isSelected) {
                                            Modifier.border(
                                                BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                                RoundedCornerShape(20.dp)
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable { selectedFilter = option }
                                    .padding(horizontal = 22.dp)
                                    .testTag("filter_chip_$option"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option,
                                    fontFamily = HindSiliguri,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else Color(0xFF1B2559)
                                )
                            }
                        }
                    }
                }

                // ৪. একটি সাদা কার্ডে (কর্নার ২০dp) ৭টি সারি, প্রতিটির উচ্চতা ৬৮dp, মাঝে ডিভাইডার
                item {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF1B2559))
                        }
                    } else if (allMembers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "এখনো কোনো সদস্যের তথ্য নেই",
                                fontFamily = HindSiliguri,
                                fontSize = 15.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    } else if (filteredMembers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "কোনো সদস্য পাওয়া যায়নি",
                                fontFamily = HindSiliguri,
                                fontSize = 15.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    } else {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("members_container_card"),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                filteredMembers.forEachIndexed { index, member ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(68.dp)
                                            .padding(horizontal = 16.dp)
                                            .testTag("member_row_${member.id}"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // বাঁয়ে ৪৪dp নেভি গোল আইকনে সাদা Bold আদ্যক্ষর
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF1B2559)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = member.initial,
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color.White
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(14.dp))

                                        // মাঝে নাম ১৭sp SemiBold ও নিচে ১৪sp ধূসর লাইন
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = member.name,
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 17.sp,
                                                color = Color(0xFF1B2559)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = member.roleAndBlood,
                                                fontFamily = HindSiliguri,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }

                                        // ডানে ব্যাজ:
                                        // "সক্রিয়" ব্যাজ: pill, ব্যাকগ্রাউন্ড #DCFCE7, লেখা গাঢ় সবুজ #166534, ১৩sp SemiBold
                                        // "নিষ্ক্রিয়": ব্যাকগ্রাউন্ড #E5E7EB, লেখা গাঢ় ধূসর #374151
                                        if (member.isActive) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFDCFCE7))
                                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "সক্রিয়",
                                                    fontFamily = HindSiliguri,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFF166534)
                                                )
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFE5E7EB))
                                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "নিষ্ক্রিয়",
                                                    fontFamily = HindSiliguri,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFF374151)
                                                )
                                            }
                                        }
                                    }

                                    if (index < filteredMembers.size - 1) {
                                        HorizontalDivider(
                                            color = Color(0xFFE2E8F0),
                                            thickness = 0.8.dp,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private fun Int.toBengaliDigits(): String {
    return this.toString().map { char ->
        when (char) {
            '0' -> '০'
            '1' -> '১'
            '2' -> '২'
            '3' -> '৩'
            '4' -> '৪'
            '5' -> '৫'
            '6' -> '৬'
            '7' -> '৭'
            '8' -> '৮'
            '9' -> '৯'
            else -> char
        }
    }.joinToString("")
}
