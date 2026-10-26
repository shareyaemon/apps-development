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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FirebaseManager
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SoftSkyBlue
import com.example.ui.theme.SubtitleGray
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.launch

@Composable
fun MemberApplicationScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var fullName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var hasPhoto by remember { mutableStateOf(false) }

    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    BackHandler {
        onNavigateBack()
    }

    val bloodGroupsRow1 = listOf("A+", "A-", "B+", "B-")
    val bloodGroupsRow2 = listOf("O+", "O-", "AB+", "AB-")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("member_application_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                        modifier = Modifier.testTag("application_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NavyPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "সদস্য আবেদন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

            // Scrollable Form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                // Info text
                Text(
                    text = "ফর্মটি পূরণ করুন। অ্যাডমিন যাচাই করে অনুমোদন দিলে আপনি সদস্য হবেন।",
                    fontFamily = HindSiliguri,
                    fontSize = 13.sp,
                    color = SubtitleGray,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Profile Photo Upload Section
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dashed circular photo frame
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .testTag("photo_upload_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = Stroke(
                                width = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                            )
                            drawCircle(
                                color = Color(0xFFA0AEC0),
                                radius = size.minDimension / 2 - 2f,
                                style = stroke
                            )
                        }

                        if (hasPhoto) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(SoftSkyBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Photo Added",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Add Photo",
                                tint = Color(0xFFA0AEC0),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    // Button: "ছবি যোগ করুন"
                    Button(
                        onClick = {
                            hasPhoto = !hasPhoto
                            Toast.makeText(
                                context,
                                if (hasPhoto) "ছবি সফলভাবে নির্বাচন করা হয়েছে!" else "ছবি সরানো হয়েছে",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftSkyBlue,
                            contentColor = NavyPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("add_photo_button")
                    ) {
                        Text(
                            text = if (hasPhoto) "ছবি পরিবর্তন করুন" else "ছবি যোগ করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Field 1: পূর্ণ নাম
                FormField(
                    label = "পূর্ণ নাম",
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "আপনার পূর্ণ নাম লিখুন",
                    tag = "input_full_name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Field 2: পিতার নাম
                FormField(
                    label = "পিতার নাম",
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    placeholder = "পিতার নাম লিখুন",
                    tag = "input_father_name"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Field 3: মোবাইল নম্বর
                FormField(
                    label = "মোবাইল নম্বর",
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    placeholder = "০১XXXXXXXXX",
                    keyboardType = KeyboardType.Phone,
                    tag = "input_mobile"
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Field 4: রক্তের গ্রুপ (Grid of 8 options)
                Text(
                    text = "রক্তের গ্রুপ",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Row 1 of Blood Groups: A+, A-, B+, B-
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bloodGroupsRow1.forEach { bg ->
                        BloodGroupChip(
                            group = bg,
                            isSelected = selectedBloodGroup == bg,
                            onSelect = { selectedBloodGroup = bg },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Row 2 of Blood Groups: O+, O-, AB+, AB-
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bloodGroupsRow2.forEach { bg ->
                        BloodGroupChip(
                            group = bg,
                            isSelected = selectedBloodGroup == bg,
                            onSelect = { selectedBloodGroup = bg },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Field 5: ঠিকানা
                Text(
                    text = "ঠিকানা",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("input_address"),
                    placeholder = {
                        Text(
                            text = "গ্রাম/মহল্লা, ডাকঘর, থানা, জেলা",
                            fontFamily = HindSiliguri,
                            fontSize = 13.sp,
                            color = Color(0xFFA0AEC0)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = NavyPrimary,
                        unfocusedBorderColor = CardBorder,
                        cursorColor = NavyPrimary
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Submit Button: "আবেদন জমা দিন"
                Button(
                    onClick = {
                        if (fullName.isBlank()) {
                            Toast.makeText(context, "অনুগ্রহ করে আপনার পূর্ণ নাম লিখুন", Toast.LENGTH_SHORT).show()
                        } else if (mobileNumber.isBlank()) {
                            Toast.makeText(context, "অনুগ্রহ করে মোবাইল নম্বর লিখুন", Toast.LENGTH_SHORT).show()
                        } else {
                            isSubmitting = true
                            scope.launch {
                                val result = FirebaseManager.submitApplication(
                                    context = context,
                                    fullName = fullName,
                                    fatherName = fatherName,
                                    mobile = mobileNumber,
                                    bloodGroup = selectedBloodGroup,
                                    address = address
                                )
                                isSubmitting = false
                                if (result.isSuccess) {
                                    showSuccessDialog = true
                                } else {
                                    Toast.makeText(context, "আবেদন জমা দিতে সমস্যা হয়েছে, অনুগ্রহ করে আবার চেষ্টা করুন", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_application_button"),
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "আবেদন জমা দিন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Success Confirmation Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateBack()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "আবেদন সফল হয়েছে!",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NavyPrimary
                )
            },
            text = {
                Text(
                    text = "ধন্যবাদ $fullName। আপনার সদস্য আবেদনটি সফলভাবে জমা হয়েছে। অ্যাডমিন কর্তৃপক্ষ যাচাই-বাছাই শেষে আপনার মোবাইল নম্বর ($mobileNumber)-এ নিশ্চিতকরণ বার্তা পাঠাবে।",
                    fontFamily = HindSiliguri,
                    fontSize = 14.sp,
                    color = SubtitleGray,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "ঠিক আছে",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        )
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    tag: String
) {
    Column {
        Text(
            text = label,
            fontFamily = HindSiliguri,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = NavyPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag),
            placeholder = {
                Text(
                    text = placeholder,
                    fontFamily = HindSiliguri,
                    fontSize = 13.sp,
                    color = Color(0xFFA0AEC0)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = NavyPrimary,
                unfocusedBorderColor = CardBorder,
                cursorColor = NavyPrimary
            )
        )
    }
}

@Composable
fun BloodGroupChip(
    group: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(
                color = if (isSelected) SoftSkyBlue else Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 1.8.dp else 1.dp,
                color = if (isSelected) NavyPrimary else CardBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect)
            .testTag("blood_group_$group"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = group,
            fontFamily = HindSiliguri,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 15.sp,
            color = NavyPrimary
        )
    }
}
