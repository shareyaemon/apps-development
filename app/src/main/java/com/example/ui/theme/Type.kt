package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val HindSiliguri = FontFamily(
    Font(R.font.hind_siliguri, FontWeight.Normal),
    Font(R.font.hind_siliguri, FontWeight.Medium),
    Font(R.font.hind_siliguri, FontWeight.SemiBold),
    Font(R.font.hind_siliguri, FontWeight.Bold)
)

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        color = NavyPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = NavyPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = NavyPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = NavyPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = NavyPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = SubtitleGray
    ),
    bodySmall = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = SubtitleGray
    ),
    labelLarge = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = HindSiliguri,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp
    )
)
