package com.example.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.graphics.withClip
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.example.R
import com.example.data.FirebaseManager
import com.example.data.MemberData
import com.example.model.MemberBottomTab
import com.example.ui.components.MemberBottomNav
import com.example.ui.theme.HindSiliguri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@Composable
fun DigitalIdCardScreen(
    userName: String = "মুহাম্মদ আব্দুল্লাহ",
    member: MemberData? = null,
    onNavigateBack: () -> Unit,
    onTabSelected: (MemberBottomTab) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var memberData by remember(member) {
        mutableStateOf(member ?: FirebaseManager.currentLoggedInMember)
    }

    LaunchedEffect(Unit, member) {
        val fetched = FirebaseManager.getCurrentOrFetchMember(context, userName)
        if (fetched != null) {
            memberData = fetched
        }
    }

    // ডাউনলোড ও শেয়ার প্রসেসিং স্টেট (১ সেকেন্ড লোডিং)
    var isDownloading by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = Modifier.testTag("digital_id_card_screen"),
        containerColor = Color(0xFFF4F7FC),
        bottomBar = {
            MemberBottomNav(
                selectedTab = MemberBottomTab.PROFILE,
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
            // ১. সাদা হেডার: ব্যাক তীর, শিরোনাম "ডিজিটাল আইডি কার্ড" (২২sp Bold)
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
                            .testTag("digital_id_back_button")
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
                        text = "ডিজিটাল আইডি কার্ড",
                        fontFamily = HindSiliguri,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1B2559),
                        modifier = Modifier.testTag("digital_id_title")
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

            // ২. স্ক্রলযোগ্য কনটেন্ট
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // প্রোফাইলের ID কার্ডের মতোই নেভি-নীল গ্রেডিয়েন্ট কার্ড
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("digital_id_card_view"),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1B2559),
                                        Color(0xFF2471A3)
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset.Infinite
                                )
                            )
                    ) {
                        // ডানে নিচে হালকা বৃত্তাকার আভা
                        Canvas(modifier = Modifier.matchParentSize()) {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.08f),
                                radius = size.width * 0.45f,
                                center = Offset(size.width * 0.95f, size.height * 0.95f)
                            )
                            drawCircle(
                                color = Color(0xFF29B6F0).copy(alpha = 0.12f),
                                radius = size.width * 0.30f,
                                center = Offset(size.width * 0.88f, size.height * 0.85f)
                            )
                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            // উপরে সারি: উপরে বাঁয়ে সংঘের আসল লোগো (৪৪dp), উপরে ডানে সাদা bold "ID: HF-2026-042"
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.club_logo),
                                    contentDescription = "সংঘের লোগো",
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Fit
                                )

                                Text(
                                    text = "ID: ${memberData?.memberId ?: "HF-2026-001"}",
                                    fontFamily = HindSiliguri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }

                            // মাঝে সারি: ৯০dp গোল অবতার, নাম, সক্রিয় সদস্য ব্যাজ, রক্তের গ্রুপ
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // ৯০dp গোল অবতার
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE6F5FD))
                                        .border(BorderStroke(2.5.dp, Color.White), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "অবতার",
                                        tint = Color(0xFF1B2559),
                                        modifier = Modifier.size(52.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // অবতারের ডানে তথ্য
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = memberData?.name?.ifBlank { userName } ?: userName.ifBlank { "মুহাম্মদ আব্দুল্লাহ" },
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Color.White,
                                        lineHeight = 26.sp
                                    )

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFDCFCE7))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (memberData?.role == "super_admin") "সুপার অ্যাডমিন" else "সক্রিয় সদস্য",
                                            fontFamily = HindSiliguri,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF166534)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        text = memberData?.bloodGroupDisplay ?: "রক্তের গ্রুপ: যোগ করা হয়নি",
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // কার্ডের নিচে গাঢ় ব্যান্ডে "যোগদান: [তারিখ]" ও "ফোন: [নম্বর]"
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0x59000000))
                                    .padding(horizontal = 16.dp, vertical = 9.dp)
                            ) {
                                val phoneFull = if (!memberData?.mobile.isNullOrBlank()) {
                                    FirebaseManager.toBengaliDigits(memberData!!.mobile)
                                } else {
                                    "০১৭১২৩৪৫৬৭৮"
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "যোগদান: ${memberData?.joinDate ?: "২০২৬"}",
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = Color.White.copy(alpha = 0.95f)
                                    )

                                    Text(
                                        text = "ফোন: $phoneFull",
                                        fontFamily = HindSiliguri,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 13.sp,
                                        color = Color.White.copy(alpha = 0.95f)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // নিচে নেভি "PDF ডাউনলোড করুন" বাটন (সাদা Bold লেখা, ১ সেকেন্ড লোডিং সহ)
                Button(
                    onClick = {
                        if (!isDownloading && !isSharing) {
                            isDownloading = true
                            coroutineScope.launch(Dispatchers.IO) {
                                delay(1000) // ১ সেকেন্ডের ছোট লোডিং
                                val bitmap = generateHighResIdCardBitmap(context, userName, memberData)
                                val pdfBytes = createIdCardPdf(bitmap)
                                val success = savePdfToDownloads(context, pdfBytes)
                                withContext(Dispatchers.Main) {
                                    isDownloading = false
                                    if (success) {
                                        Toast.makeText(
                                            context,
                                            "ডাউনলোড সম্পন্ন: Downloads ফোল্ডারে সংরক্ষিত",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "ডাউনলোড ব্যর্থ হয়েছে",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    },
                    enabled = !isDownloading && !isSharing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("download_pdf_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1B2559),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF1B2559).copy(alpha = 0.85f),
                        disabledContentColor = Color.White
                    )
                ) {
                    if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "PDF তৈরি হচ্ছে...",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "ডাউনলোড",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PDF ডাউনলোড করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                // আউটলাইন "শেয়ার করুন" বাটন (১ সেকেন্ড লোডিং সহ)
                OutlinedButton(
                    onClick = {
                        if (!isDownloading && !isSharing) {
                            isSharing = true
                            coroutineScope.launch(Dispatchers.IO) {
                                delay(1000) // ১ সেকেন্ডের ছোট লোডিং
                                val bitmap = generateHighResIdCardBitmap(context, userName, memberData)
                                withContext(Dispatchers.Main) {
                                    shareIdCardImage(context, bitmap, memberData, userName)
                                    isSharing = false
                                }
                            }
                        }
                    },
                    enabled = !isDownloading && !isSharing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("share_card_button"),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, Color(0xFF1B2559)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1B2559),
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color(0xFF1B2559).copy(alpha = 0.6f)
                    )
                ) {
                    if (isSharing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color(0xFF1B2559),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "ছবি প্রস্তুত হচ্ছে...",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1B2559)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার",
                            tint = Color(0xFF1B2559),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "শেয়ার করুন",
                            fontFamily = HindSiliguri,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1B2559)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/**
 * ১০৮০px উচ্চ রেজোলিউশনের আইডি কার্ড Bitmap প্রস্তুতকরণ ফাংশন
 */
private fun generateHighResIdCardBitmap(context: Context, userName: String, member: MemberData? = null): Bitmap {
    val width = 1080
    val height = 630
    val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // ১. ব্যাকগ্রাউন্ড গ্রেডিয়েন্ট ও কর্নার ক্লিপ (ব্যাসার্ধ = ৪৪px)
    val cardPath = Path().apply {
        addRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            44f,
            44f,
            Path.Direction.CW
        )
    }
    canvas.clipPath(cardPath)

    val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(0xFF1B2559.toInt(), 0xFF2471A3.toInt()),
            null,
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradientPaint)

    // ২. ডানপাশে হালকা বৃত্তাকার আভা
    val glowPaint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x14FFFFFF // White 8%
        style = Paint.Style.FILL
    }
    canvas.drawCircle(width * 0.95f, height * 0.95f, width * 0.45f, glowPaint1)

    val glowPaint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x1F29B6F0.toInt() // Cyan glow 12%
        style = Paint.Style.FILL
    }
    canvas.drawCircle(width * 0.88f, height * 0.85f, width * 0.30f, glowPaint2)

    // ৩. ফন্ট লোড (HindSiliguri)
    val hindSiliguri = try {
        ResourcesCompat.getFont(context, R.font.hind_siliguri)
    } catch (_: Exception) {
        null
    }
    val boldTypeface = if (hindSiliguri != null) Typeface.create(hindSiliguri, Typeface.BOLD) else Typeface.DEFAULT_BOLD
    val mediumTypeface = hindSiliguri ?: Typeface.DEFAULT

    // ৪. সংঘের আসল লোগো (৪৪dp সমতুল্য ১০৮px)
    val logoOriginal = try {
        BitmapFactory.decodeResource(context.resources, R.drawable.club_logo)
    } catch (_: Exception) {
        null
    }
    if (logoOriginal != null) {
        val logoSize = 100
        val scaledLogo = logoOriginal.scale(logoSize, logoSize, true)
        val logoX = 48f
        val logoY = 40f

        val circlePath = Path().apply {
            addCircle(logoX + logoSize / 2f, logoY + logoSize / 2f, logoSize / 2f, Path.Direction.CW)
        }
        canvas.withClip(circlePath) {
            drawBitmap(scaledLogo, logoX, logoY, Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG))
        }
    }

    // ৫. আইডি টেক্সট (ডানে উপরে)
    val idPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textSize = 34f
        typeface = boldTypeface
        textAlign = Paint.Align.RIGHT
    }
    canvas.drawText("ID: ${member?.memberId ?: "HF-2026-001"}", width - 48f, 85f, idPaint)

    // ৬. অবতার (ব্যাসার্ধ = ৯৫px)
    val avatarCenterX = 155f
    val avatarCenterY = 305f
    val avatarRadius = 90f

    // অবতার ব্যাকগ্রাউন্ড (#E6F5FD)
    val avatarBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE6F5FD.toInt()
        style = Paint.Style.FILL
    }
    canvas.drawCircle(avatarCenterX, avatarCenterY, avatarRadius, avatarBgPaint)

    // অবতারের ভেতরে ব্যক্তি আইকন আঁকা
    val avatarClip = Path().apply {
        addCircle(avatarCenterX, avatarCenterY, avatarRadius, Path.Direction.CW)
    }
    canvas.withClip(avatarClip) {
        val avatarIconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF1B2559.toInt()
            style = Paint.Style.FILL
        }
        // মাথা
        drawCircle(avatarCenterX, avatarCenterY - 22f, 32f, avatarIconPaint)
        // বডি / কাঁধ
        drawCircle(avatarCenterX, avatarCenterY + 75f, 65f, avatarIconPaint)
    }

    // অবতার সাদা বর্ডার (৬px)
    val avatarBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }
    canvas.drawCircle(avatarCenterX, avatarCenterY, avatarRadius, avatarBorderPaint)

    // ৭. নাম
    val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textSize = 50f
        typeface = boldTypeface
    }
    val displayName = member?.name?.ifBlank { userName } ?: userName.ifBlank { "মুহাম্মদ আব্দুল্লাহ" }
    canvas.drawText(displayName, 280f, 250f, namePaint)

    // ৮. "সক্রিয় সদস্য" ব্যাজ (#DCFCE7)
    val badgeRect = RectF(280f, 275f, 280f + 185f, 275f + 48f)
    val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFDCFCE7.toInt()
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(badgeRect, 14f, 14f, badgeBgPaint)

    val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF166534.toInt()
        textSize = 26f
        typeface = boldTypeface
        textAlign = Paint.Align.CENTER
    }
    val roleBadge = if (member?.role == "super_admin") "সুপার অ্যাডমিন" else "সক্রিয় সদস্য"
    canvas.drawText(roleBadge, badgeRect.centerX(), badgeRect.centerY() + 9f, badgeTextPaint)

    // ৯. রক্তের গ্রুপ (না থাকলে "রক্তের গ্রুপ: যোগ করা হয়নি")
    val bloodPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textSize = 33f
        typeface = mediumTypeface
    }
    val bloodDisplay = member?.bloodGroupDisplay ?: "রক্তের গ্রুপ: যোগ করা হয়নি"
    canvas.drawText(bloodDisplay, 280f, 375f, bloodPaint)

    // ১০. নিচের গাঢ় ব্যান্ড (#59000000)
    val bottomBandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x59000000
        style = Paint.Style.FILL
    }
    val bandHeight = 90f
    canvas.drawRect(0f, height - bandHeight, width.toFloat(), height.toFloat(), bottomBandPaint)

    val bottomTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xF2FFFFFF.toInt()
        textSize = 30f
        typeface = mediumTypeface
    }
    val joinText = "যোগদান: ${member?.joinDate ?: "২০২৬"}"
    canvas.drawText(joinText, 48f, height - 34f, bottomTextPaint)

    val bottomPhonePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xF2FFFFFF.toInt()
        textSize = 30f
        typeface = mediumTypeface
        textAlign = Paint.Align.RIGHT
    }
    val phoneFull = if (!member?.mobile.isNullOrBlank()) {
        FirebaseManager.toBengaliDigits(member!!.mobile)
    } else {
        "০১৭১২৩৪৫৬৭৮"
    }
    canvas.drawText("ফোন: $phoneFull", width - 48f, height - 34f, bottomPhonePaint)

    return bitmap
}

/**
 * Bitmap থেকে A6 সাইজের PDF ডকুমেন্ট তৈরি
 */
private fun createIdCardPdf(bitmap: Bitmap): ByteArray {
    val document = PdfDocument()
    // A6 landscape মাপ পয়েন্টে: ৪২০ x ২৯৮ pt
    val pageWidth = 420
    val pageHeight = 298
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = document.startPage(pageInfo)
    val canvas = page.canvas

    val margin = 20f
    val destWidth = pageWidth - (margin * 2)
    val destHeight = destWidth * (bitmap.height.toFloat() / bitmap.width.toFloat())
    val topOffset = (pageHeight - destHeight) / 2f
    val destRect = RectF(margin, topOffset, margin + destWidth, topOffset + destHeight)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    canvas.drawBitmap(bitmap, null, destRect, paint)

    document.finishPage(page)

    val outputStream = ByteArrayOutputStream()
    document.writeTo(outputStream)
    document.close()
    return outputStream.toByteArray()
}

/**
 * PDF ফাইল ফোনের Downloads ফোল্ডারে সেভ (Android 10+ এ MediaStore)
 */
private fun savePdfToDownloads(context: Context, pdfBytes: ByteArray): Boolean {
    val fileName = "ID_Card_HF-2026-042.pdf"
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { os ->
                    os.write(pdfBytes)
                    os.flush()
                }
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
                true
            } else {
                false
            }
        } else {
            @Suppress("DEPRECATION")
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            val file = File(downloadsDir, fileName)
            FileOutputStream(file).use { os ->
                os.write(pdfBytes)
                os.flush()
            }
            true
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * সিস্টেম শেয়ার শিট দিয়ে PNG ছবি শেয়ার
 */
private fun shareIdCardImage(context: Context, bitmap: Bitmap, member: MemberData?, userName: String) {
    try {
        val cacheFolder = File(context.cacheDir, "shared_images").apply { mkdirs() }
        val memberId = member?.memberId ?: "HF-2026-001"
        val imageFile = File(cacheFolder, "ID_Card_${memberId}.png")
        FileOutputStream(imageFile).use { os ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
        }

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )

        val bloodDisplay = member?.bloodGroupDisplay ?: "রক্তের গ্রুপ: যোগ করা হয়নি"
        val memberName = member?.name?.ifBlank { userName } ?: userName
        val shareText = "হিলফুল ফুজুল স্বর্ণটিলা যুব সংঘ — ডিজিটাল আইডি কার্ড\nনাম: $memberName\nআইডি: $memberId\n$bloodDisplay"

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "হিলফুল ফুজুল সদস্য আইডি কার্ড").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "শেয়ার করতে সমস্যা হয়েছে", Toast.LENGTH_SHORT).show()
    }
}
