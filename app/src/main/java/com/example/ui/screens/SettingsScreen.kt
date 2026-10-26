package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HindSiliguri

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onNavigateBack()
    }

    // পাসওয়ার্ড ফিল্ডের স্টেট
    var currentPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // টগল ও অগ্রাধিকার স্টেট
    var isNotificationEnabled by remember { mutableStateOf(true) }
    var isShowInBloodDonorList by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.testTag("settings_screen"),
        containerColor = Color(0xFFF4F7FC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FC))
                .padding(innerPadding)
        ) {
            // ১. সাদা হেডার: ব্যাক তীর ও শিরোনাম "সেটিংস" (২২sp Bold নেভি)
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
                            .testTag("settings_back_button")
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
                        text = "সেটিংস",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1B2559),
                        modifier = Modifier.testTag("settings_title")
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            // ২. স্ক্রলযোগ্য কনটেন্ট (ছবির মতো হুবহু কাঠামো)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                // "পাসওয়ার্ড পরিবর্তন" ১৮sp Bold শিরোনাম (কার্ডের বাইরে)
                Text(
                    text = "পাসওয়ার্ড পরিবর্তন",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B2559),
                    modifier = Modifier.padding(bottom = 12.dp, start = 2.dp)
                )

                // সাদা কার্ড (কর্নার ২০dp, হালকা বর্ডার)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("change_password_card"),
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
                        // লেবেল ১: "বর্তমান পাসওয়ার্ড" (১৫sp Medium নেভি)
                        Text(
                            text = "বর্তমান পাসওয়ার্ড",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color(0xFF1B2559)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // খালি ইনপুট ফিল্ড (উচ্চতা ৫০dp, কর্নার ১৬dp, হালকা বর্ডার, চোখ আইকন সহ)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(16.dp))
                                .padding(start = 16.dp, end = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = currentPassword,
                                    onValueChange = { currentPassword = it },
                                    singleLine = true,
                                    visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    textStyle = TextStyle(
                                        fontFamily = HindSiliguri,
                                        fontSize = 15.sp,
                                        color = Color(0xFF1B2559)
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF1B2559)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("current_password_input")
                                )
                                IconButton(
                                    onClick = { currentPasswordVisible = !currentPasswordVisible },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .testTag("current_password_toggle")
                                ) {
                                    Icon(
                                        imageVector = if (currentPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (currentPasswordVisible) "পাসওয়ার্ড লুকান" else "পাসওয়ার্ড দেখুন",
                                        tint = Color(0xFF5A6E85)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // লেবেল ২: "নতুন পাসওয়ার্ড" (১৫sp Medium নেভি)
                        Text(
                            text = "নতুন পাসওয়ার্ড",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color(0xFF1B2559)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // খালি ইনপুট ফিল্ড (উচ্চতা ৫০dp, কর্নার ১৬dp, হালকা বর্ডার, চোখ আইকন সহ)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(16.dp))
                                .padding(start = 16.dp, end = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = newPassword,
                                    onValueChange = { newPassword = it },
                                    singleLine = true,
                                    visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    textStyle = TextStyle(
                                        fontFamily = HindSiliguri,
                                        fontSize = 15.sp,
                                        color = Color(0xFF1B2559)
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF1B2559)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("new_password_input")
                                )
                                IconButton(
                                    onClick = { newPasswordVisible = !newPasswordVisible },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .testTag("new_password_toggle")
                                ) {
                                    Icon(
                                        imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (newPasswordVisible) "পাসওয়ার্ড লুকান" else "পাসওয়ার্ড দেখুন",
                                        tint = Color(0xFF5A6E85)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // লেবেল ৩: "নতুন পাসওয়ার্ড আবার দিন" (১৫sp Medium নেভি)
                        Text(
                            text = "নতুন পাসওয়ার্ড আবার দিন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color(0xFF1B2559)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // খালি ইনপুট ফিল্ড (উচ্চতা ৫০dp, কর্নার ১৬dp, হালকা বর্ডার, চোখ আইকন সহ)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(16.dp))
                                .padding(start = 16.dp, end = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = confirmPassword,
                                    onValueChange = { confirmPassword = it },
                                    singleLine = true,
                                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    textStyle = TextStyle(
                                        fontFamily = HindSiliguri,
                                        fontSize = 15.sp,
                                        color = Color(0xFF1B2559)
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF1B2559)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("confirm_password_input")
                                )
                                IconButton(
                                    onClick = { confirmPasswordVisible = !confirmPasswordVisible },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .testTag("confirm_password_toggle")
                                ) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (confirmPasswordVisible) "পাসওয়ার্ড লুকান" else "পাসওয়ার্ড দেখুন",
                                        tint = Color(0xFF5A6E85)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // নিচে নেভি "পাসওয়ার্ড বদলান" বাটন (সাদা Bold লেখা, উচ্চতা ৪৮dp, কর্নার ১৬dp)
                        Button(
                            onClick = {
                                when {
                                    currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() -> {
                                        Toast.makeText(context, "সবগুলো ঘর পূরণ করুন", Toast.LENGTH_SHORT).show()
                                    }
                                    newPassword != confirmPassword -> {
                                        Toast.makeText(context, "নতুন পাসওয়ার্ড মিলছে না", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        currentPassword = ""
                                        currentPasswordVisible = false
                                        newPassword = ""
                                        newPasswordVisible = false
                                        confirmPassword = ""
                                        confirmPasswordVisible = false
                                        Toast.makeText(context, "পাসওয়ার্ড সফলভাবে পরিবর্তন করা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("change_password_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1B2559),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "পাসওয়ার্ড বদলান",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // "পছন্দসমূহ" ১৮sp Bold শিরোনাম (কার্ডের বাইরে)
                Text(
                    text = "পছন্দসমূহ",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B2559),
                    modifier = Modifier.padding(bottom = 12.dp, start = 2.dp)
                )

                // সাদা কার্ডে ৩টি সারি (ডিভাইডারসহ)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("preferences_card"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // সারি ১: "নোটিফিকেশন পান" (নেভি টগল চালু)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "নোটিফিকেশন পান",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559)
                            )

                            Switch(
                                checked = isNotificationEnabled,
                                onCheckedChange = { isNotificationEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF1B2559),
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color(0xFFCBD5E1)
                                ),
                                modifier = Modifier.testTag("toggle_notification")
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFE2E8F0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )

                        // সারি ২: "রক্তদাতা তালিকায় আমার নাম দেখান" (নেভি টগল চালু)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "রক্তদাতা তালিকায় আমার নাম দেখান",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559)
                            )

                            Switch(
                                checked = isShowInBloodDonorList,
                                onCheckedChange = { isShowInBloodDonorList = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF1B2559),
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color(0xFFCBD5E1)
                                ),
                                modifier = Modifier.testTag("toggle_blood_donor_visibility")
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFE2E8F0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(horizontal = 18.dp)
                        )

                        // সারি ৩: "ভাষা" বাঁয়ে ও ডানে Bold নেভি "বাংলা"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "ভাষা",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559)
                            )

                            Text(
                                text = "বাংলা",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1B2559),
                                modifier = Modifier.testTag("language_text_bangla")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
