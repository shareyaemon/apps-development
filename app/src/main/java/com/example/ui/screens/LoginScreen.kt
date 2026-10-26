package com.example.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.FirebaseManager
import com.example.data.MemberData
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.HindSiliguri
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SubtitleGray
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateHome: () -> Unit,
    onNavigateApplication: () -> Unit,
    onLoginSuccess: (MemberData) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var hasAttemptedLogin by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }

    // First super-admin creation state
    var isMembersEmpty by remember { mutableStateOf(false) }
    var hasAdminCreated by remember { mutableStateOf(false) }
    var showCreateAdminDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val empty = FirebaseManager.isMembersCollectionEmpty(context)
            isMembersEmpty = empty
        } catch (e: Exception) {
            isMembersEmpty = false
        }
    }

    BackHandler {
        onNavigateHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("login_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Navy curved header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = NavyPrimary,
                        shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
                    )
                    .statusBarsPadding()
                    .padding(top = 28.dp, bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.club_logo),
                        contentDescription = "Club Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .testTag("login_header_logo"),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "স্বাগতম",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "আপনার সদস্য অ্যাকাউন্টে লগইন করুন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color(0xFFBAC7E5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Mobile or Member ID Label
                Text(
                    text = "মোবাইল নম্বর বা সদস্য আইডি",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                val cleanUsername = username.trim()
                val mobileErrorMessage = when {
                    cleanUsername.isEmpty() && hasAttemptedLogin -> "মোবাইল নম্বর লিখুন"
                    cleanUsername.isNotEmpty() && (!cleanUsername.startsWith("HF-") && !cleanUsername.matches(Regex("^01[3-9]\\d{8}$"))) -> "সঠিক মোবাইল নম্বর লিখুন"
                    else -> null
                }

                val passwordErrorMessage = when {
                    password.isEmpty() && hasAttemptedLogin -> "পাসওয়ার্ড লিখুন"
                    password.isNotEmpty() && password.length < 6 -> "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে"
                    else -> null
                }

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_username_input"),
                    placeholder = {
                        Text(
                            text = "০১XXXXXXXXX",
                            fontFamily = HindSiliguri,
                            color = Color(0xFFA0AEC0)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = if (mobileErrorMessage != null) Color(0xFFC81E1E) else NavyPrimary,
                        unfocusedBorderColor = if (mobileErrorMessage != null) Color(0xFFF05252) else CardBorder,
                        cursorColor = NavyPrimary
                    )
                )

                if (mobileErrorMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = mobileErrorMessage,
                        fontFamily = HindSiliguri,
                        fontSize = 12.sp,
                        color = Color(0xFFC81E1E),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("login_mobile_error")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Label
                Text(
                    text = "পাসওয়ার্ড",
                    fontFamily = HindSiliguri,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = NavyPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_input"),
                    placeholder = {
                        Text(
                            text = "••••••••",
                            color = Color(0xFFA0AEC0)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.testTag("login_password_toggle")
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "পাসওয়ার্ড লুকান" else "পাসওয়ার্ড দেখুন",
                                tint = Color(0xFF5A6E85)
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = if (passwordErrorMessage != null) Color(0xFFC81E1E) else NavyPrimary,
                        unfocusedBorderColor = if (passwordErrorMessage != null) Color(0xFFF05252) else CardBorder,
                        cursorColor = NavyPrimary
                    )
                )

                if (passwordErrorMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = passwordErrorMessage,
                        fontFamily = HindSiliguri,
                        fontSize = 12.sp,
                        color = Color(0xFFC81E1E),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag("login_password_error")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Forgot Password link
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "পাসওয়ার্ড ভুলে গেছেন?",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = NavyPrimary,
                        modifier = Modifier
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "পাসওয়ার্ড পুনরুদ্ধারে অ্যাডমিনের সাথে যোগাযোগ করুন: ০১৭১২৩৪৫৬৭৮",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            .padding(vertical = 4.dp)
                            .testTag("forgot_password_link")
                    )
                }

                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFDE8E8), RoundedCornerShape(10.dp))
                            .border(1.dp, Color(0xFFF05252), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .testTag("login_error_banner")
                    ) {
                        Text(
                            text = errorMessage,
                            fontFamily = HindSiliguri,
                            fontSize = 13.sp,
                            color = Color(0xFF9B1C1C),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Login Button
                Button(
                    onClick = {
                        hasAttemptedLogin = true
                        val clean = username.trim()
                        when {
                            clean.isEmpty() -> {
                                errorMessage = "মোবাইল নম্বর লিখুন"
                                return@Button
                            }
                            !clean.startsWith("HF-") && !clean.matches(Regex("^01[3-9]\\d{8}$")) -> {
                                errorMessage = "সঠিক মোবাইল নম্বর লিখুন"
                                return@Button
                            }
                            password.isEmpty() -> {
                                errorMessage = "পাসওয়ার্ড লিখুন"
                                return@Button
                            }
                            password.length < 6 -> {
                                errorMessage = "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে"
                                return@Button
                            }
                        }

                        isLoading = true
                        errorMessage = ""
                        scope.launch {
                            try {
                                val result = FirebaseManager.loginWithPhoneOrId(context, clean, password)
                                isLoading = false
                                if (result.isSuccess) {
                                    val member = result.getOrNull() ?: MemberData(
                                        memberId = if (clean.startsWith("HF-")) clean else "HF-2026-042",
                                        name = "মুহাম্মদ আব্দুল্লাহ",
                                        role = "member",
                                        mobile = if (clean.startsWith("01")) clean else "01712345678"
                                    )
                                    Toast.makeText(context, "সফলভাবে লগইন করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(member)
                                } else {
                                    val rawMsg = result.exceptionOrNull()?.message.orEmpty()
                                    val msgLower = (rawMsg + " " + (result.exceptionOrNull()?.javaClass?.simpleName ?: "")).lowercase()
                                    errorMessage = when {
                                        rawMsg == "আপনার অ্যাকাউন্ট নিষ্ক্রিয় আছে, সংঘের সাথে যোগাযোগ করুন" || msgLower.contains("নিষ্ক্রিয়") || msgLower.contains("inactive") ->
                                            "আপনার অ্যাকাউন্ট নিষ্ক্রিয় আছে, সংঘের সাথে যোগাযোগ করুন"
                                        rawMsg == "মোবাইল নম্বর লিখুন" -> "মোবাইল নম্বর লিখুন"
                                        rawMsg == "সঠিক মোবাইল নম্বর লিখুন" -> "সঠিক মোবাইল নম্বর লিখুন"
                                        rawMsg == "পাসওয়ার্ড লিখুন" -> "পাসওয়ার্ড লিখুন"
                                        rawMsg == "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে" -> "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে"
                                        msgLower.contains("network") || msgLower.contains("ইন্টারনেট") ->
                                            "ইন্টারনেট সংযোগ চেক করুন"
                                        msgLower.contains("too-many-requests") || msgLower.contains("অনেকবার ভুল চেষ্টা") ->
                                            "অনেকবার ভুল চেষ্টা হয়েছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                                        msgLower.contains("user-not-found") || msgLower.contains("সদস্য পাওয়া যায়নি") ->
                                            "এই মোবাইল নম্বরে কোনো সদস্য পাওয়া যায়নি"
                                        msgLower.contains("wrong-password") || msgLower.contains("invalid-credential") || msgLower.contains("credential") || msgLower.contains("পাসওয়ার্ড ভুল") ->
                                            "পাসওয়ার্ড ভুল হয়েছে"
                                        else ->
                                            "লগইন সম্ভব হয়নি, আবার চেষ্টা করুন"
                                    }
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                val msgLower = (e.message.orEmpty() + " " + e.javaClass.simpleName).lowercase()
                                errorMessage = when {
                                    e.message == "আপনার অ্যাকাউন্ট নিষ্ক্রিয় আছে, সংঘের সাথে যোগাযোগ করুন" || msgLower.contains("নিষ্ক্রিয়") || msgLower.contains("inactive") ->
                                        "আপনার অ্যাকাউন্ট নিষ্ক্রিয় আছে, সংঘের সাথে যোগাযোগ করুন"
                                    msgLower.contains("network") || msgLower.contains("ইন্টারনেট") ->
                                        "ইন্টারনেট সংযোগ চেক করুন"
                                    msgLower.contains("too-many-requests") || msgLower.contains("অনেকবার") ->
                                        "অনেকবার ভুল চেষ্টা হয়েছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                                    msgLower.contains("user-not-found") || msgLower.contains("সদস্য পাওয়া যায়নি") ->
                                        "এই মোবাইল নম্বরে কোনো সদস্য পাওয়া যায়নি"
                                    msgLower.contains("wrong-password") || msgLower.contains("invalid-credential") || msgLower.contains("credential") || msgLower.contains("পাসওয়ার্ড ভুল") ->
                                        "পাসওয়ার্ড ভুল হয়েছে"
                                    else ->
                                        "লগইন সম্ভব হয়নি, আবার চেষ্টা করুন"
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("login_button"),
                    enabled = !isLoading && !isGoogleLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "লগইন করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Divider with "অথবা"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = CardBorder
                    )
                    Text(
                        text = " অথবা ",
                        fontFamily = HindSiliguri,
                        fontSize = 13.sp,
                        color = SubtitleGray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = CardBorder
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Google Sign In Button
                OutlinedButton(
                    onClick = {
                        val activity = context as? Activity
                        if (activity != null) {
                            isGoogleLoading = true
                            errorMessage = ""
                            scope.launch {
                                val result = FirebaseManager.signInWithGoogle(activity)
                                isGoogleLoading = false
                                if (result.isSuccess) {
                                    val user = result.getOrNull()
                                    val member = FirebaseManager.currentLoggedInMember ?: MemberData(
                                        name = user?.displayName ?: "মুহাম্মদ আব্দুল্লাহ",
                                        email = user?.email ?: "",
                                        role = "member"
                                    )
                                    Toast.makeText(context, "গুগল সাইন-ইন সফল হয়েছে!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(member)
                                } else {
                                    val ex = result.exceptionOrNull()
                                    if (ex !is androidx.credentials.exceptions.GetCredentialCancellationException) {
                                        errorMessage = "গুগল সাইন-ইন ব্যর্থ হয়েছে: ${ex?.message ?: "পুনরায় চেষ্টা করুন"}"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("google_signin_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = NavyPrimary
                    ),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    if (isGoogleLoading) {
                        CircularProgressIndicator(
                            color = NavyPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Google দিয়ে সাইন ইন করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NavyPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Apply button: "নতুন সদস্য? আবেদন করুন"
                OutlinedButton(
                    onClick = onNavigateApplication,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("new_member_apply_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = NavyPrimary
                    ),
                    border = BorderStroke(1.2.dp, NavyPrimary)
                ) {
                    Text(
                        text = "নতুন সদস্য? আবেদন করুন",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = NavyPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Go to home without login: "লগইন ছাড়াই হোমে যান"
                TextButton(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("guest_home_link")
                ) {
                    Text(
                        text = "লগইন ছাড়াই হোমে যান",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = NavyPrimary
                    )
                }

                // First super admin creation link if members collection is empty and not created yet
                if (isMembersEmpty && !hasAdminCreated) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "প্রথম অ্যাডমিন তৈরি করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = Color(0xFFD97706),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .clickable { showCreateAdminDialog = true }
                                .padding(vertical = 6.dp, horizontal = 12.dp)
                                .testTag("create_first_admin_link")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Modal Dialog to create the first Super Admin
    if (showCreateAdminDialog) {
        var adminName by remember { mutableStateOf("") }
        var adminMobile by remember { mutableStateOf("") }
        var selectedBloodGroup by remember { mutableStateOf("A+") }
        var adminPassword by remember { mutableStateOf("") }
        var adminPasswordVisible by remember { mutableStateOf(false) }
        var adminDialogError by remember { mutableStateOf("") }
        var isAdminCreating by remember { mutableStateOf(false) }

        Dialog(
            onDismissRequest = {
                if (!isAdminCreating) showCreateAdminDialog = false
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(22.dp)
                    .testTag("create_admin_dialog")
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "প্রথম সুপার অ্যাডমিন তৈরি",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "অ্যাপের প্রথম সুপার অ্যাডমিন অ্যাকাউন্ট তৈরি করুন। এটি একবারই তৈরি করা যাবে।",
                        fontFamily = HindSiliguri,
                        fontSize = 12.sp,
                        color = SubtitleGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Name Field
                    Text(
                        text = "পূর্ণ নাম",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = adminName,
                        onValueChange = {
                            adminName = it
                            adminDialogError = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_name_input"),
                        placeholder = {
                            Text(
                                text = "মুহাম্মদ আব্দুল্লাহ",
                                fontFamily = HindSiliguri,
                                fontSize = 13.sp,
                                color = Color(0xFFA0AEC0)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mobile Field (11 digits, starts with 01)
                    Text(
                        text = "মোবাইল নম্বর (১১ ডিজিট, 01 দিয়ে শুরু)",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = adminMobile,
                        onValueChange = {
                            adminMobile = it
                            adminDialogError = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_mobile_input"),
                        placeholder = {
                            Text(
                                text = "01XXXXXXXXX",
                                fontFamily = HindSiliguri,
                                fontSize = 13.sp,
                                color = Color(0xFFA0AEC0)
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Blood Group Selection (A+ to AB-)
                    Text(
                        text = "রক্তের গ্রুপ",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("A+", "A-", "B+", "B-").forEach { bg ->
                            val isSelected = selectedBloodGroup == bg
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NavyPrimary else Color.White)
                                    .border(1.dp, if (isSelected) NavyPrimary else CardBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedBloodGroup = bg }
                                    .testTag("admin_blood_group_$bg"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = bg,
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else NavyPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("O+", "O-", "AB+", "AB-").forEach { bg ->
                            val isSelected = selectedBloodGroup == bg
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NavyPrimary else Color.White)
                                    .border(1.dp, if (isSelected) NavyPrimary else CardBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedBloodGroup = bg }
                                    .testTag("admin_blood_group_$bg"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = bg,
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else NavyPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Password Field (min 6 chars)
                    Text(
                        text = "পাসওয়ার্ড (কমপক্ষে ৬ অক্ষর)",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = adminPassword,
                        onValueChange = {
                            adminPassword = it
                            adminDialogError = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_password_input"),
                        placeholder = {
                            Text(
                                text = "••••••••",
                                fontSize = 13.sp,
                                color = Color(0xFFA0AEC0)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { adminPasswordVisible = !adminPasswordVisible },
                                modifier = Modifier.testTag("admin_password_toggle")
                            ) {
                                Icon(
                                    imageVector = if (adminPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (adminPasswordVisible) "পাসওয়ার্ড লুকান" else "পাসওয়ার্ড দেখুন",
                                    tint = Color(0xFF5A6E85)
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (adminPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = NavyPrimary,
                            unfocusedBorderColor = CardBorder
                        )
                    )

                    if (adminDialogError.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = adminDialogError,
                            fontFamily = HindSiliguri,
                            fontSize = 12.sp,
                            color = Color(0xFFC81E1E),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { showCreateAdminDialog = false },
                            enabled = !isAdminCreating
                        ) {
                            Text(
                                text = "বাতিল",
                                fontFamily = HindSiliguri,
                                fontWeight = FontWeight.SemiBold,
                                color = SubtitleGray
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val cleanN = adminName.trim()
                                val cleanM = adminMobile.trim()
                                if (cleanN.isBlank()) {
                                    adminDialogError = "নাম প্রদান করুন"
                                    return@Button
                                }
                                if (!cleanM.matches(Regex("^01[3-9]\\d{8}$"))) {
                                    adminDialogError = "১১ ডিজিটের সঠিক মোবাইল নম্বর দিন (01 দিয়ে শুরু)"
                                    return@Button
                                }
                                if (adminPassword.length < 6) {
                                    adminDialogError = "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে"
                                    return@Button
                                }

                                isAdminCreating = true
                                adminDialogError = ""
                                scope.launch {
                                    val res = FirebaseManager.createFirstSuperAdmin(
                                        context,
                                        cleanN,
                                        cleanM,
                                        adminPassword,
                                        selectedBloodGroup
                                    )
                                    isAdminCreating = false
                                    if (res.isSuccess) {
                                        val member = res.getOrNull() ?: MemberData(
                                            memberId = "HF-2026-001",
                                            name = cleanN,
                                            role = "super_admin",
                                            designation = "সুপার অ্যাডমিন",
                                            mobile = cleanM,
                                            bloodGroup = selectedBloodGroup
                                        )
                                        hasAdminCreated = true
                                        isMembersEmpty = false
                                        showCreateAdminDialog = false
                                        Toast.makeText(
                                            context,
                                            "সুপার অ্যাডমিন অ্যাকাউন্ট তৈরি সফল হয়েছে!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        onLoginSuccess(member)
                                    } else {
                                        adminDialogError = res.exceptionOrNull()?.message
                                            ?: "অ্যাকাউন্ট তৈরিতে ব্যর্থ হয়েছে"
                                    }
                                }
                            },
                            enabled = !isAdminCreating,
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("submit_create_admin_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NavyPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            if (isAdminCreating) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "অ্যাকাউন্ট তৈরি করুন",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
