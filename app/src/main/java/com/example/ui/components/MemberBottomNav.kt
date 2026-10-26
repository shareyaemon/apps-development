package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MemberBottomTab
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SoftSkyBlue
import com.example.ui.theme.SubtitleGray

@Composable
fun MemberBottomNav(
    selectedTab: MemberBottomTab,
    onTabSelected: (MemberBottomTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(width = 0.8.dp, color = CardBorder)
            .testTag("member_bottom_nav")
    ) {
        // 1. হোম
        NavigationBarItem(
            selected = selectedTab == MemberBottomTab.HOME,
            onClick = { onTabSelected(MemberBottomTab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "হোম",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "হোম",
                    fontFamily = HindSiliguri,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == MemberBottomTab.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = SoftSkyBlue,
                unselectedIconColor = SubtitleGray,
                unselectedTextColor = SubtitleGray
            )
        )

        // 2. নোটিশ
        NavigationBarItem(
            selected = selectedTab == MemberBottomTab.NOTICE,
            onClick = { onTabSelected(MemberBottomTab.NOTICE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "নোটিশ",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "নোটিশ",
                    fontFamily = HindSiliguri,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == MemberBottomTab.NOTICE) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = SoftSkyBlue,
                unselectedIconColor = SubtitleGray,
                unselectedTextColor = SubtitleGray
            )
        )

        // 3. হাদিয়া
        NavigationBarItem(
            selected = selectedTab == MemberBottomTab.HADIYA,
            onClick = { onTabSelected(MemberBottomTab.HADIYA) },
            icon = {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = "হাদিয়া",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "হাদিয়া",
                    fontFamily = HindSiliguri,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == MemberBottomTab.HADIYA) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = SoftSkyBlue,
                unselectedIconColor = SubtitleGray,
                unselectedTextColor = SubtitleGray
            )
        )

        // 4. সদস্য
        NavigationBarItem(
            selected = selectedTab == MemberBottomTab.MEMBERS,
            onClick = { onTabSelected(MemberBottomTab.MEMBERS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "সদস্য",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "সদস্য",
                    fontFamily = HindSiliguri,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == MemberBottomTab.MEMBERS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = SoftSkyBlue,
                unselectedIconColor = SubtitleGray,
                unselectedTextColor = SubtitleGray
            )
        )

        // 5. প্রোফাইল
        NavigationBarItem(
            selected = selectedTab == MemberBottomTab.PROFILE,
            onClick = { onTabSelected(MemberBottomTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "প্রোফাইল",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "প্রোফাইল",
                    fontFamily = HindSiliguri,
                    fontSize = 12.sp,
                    fontWeight = if (selectedTab == MemberBottomTab.PROFILE) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = NavyPrimary,
                selectedTextColor = NavyPrimary,
                indicatorColor = SoftSkyBlue,
                unselectedIconColor = SubtitleGray,
                unselectedTextColor = SubtitleGray
            )
        )
    }
}
