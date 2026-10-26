package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("হিলফুল ফুজুল", appName)
  }

  @Test
  fun `launch MainActivity without crash`() {
    val app = org.robolectric.Robolectric.buildActivity(MainActivity::class.java).setup()
  }

  @Test
  fun `check firestore getInstance`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    com.example.data.FirebaseManager.getFirestore(context)
  }

  @Test
  fun `verify blood donation eligibility when no previous donation`() {
    val member = com.example.data.MemberData(
      name = "মুহাম্মদ আব্দুল্লাহ",
      lastDonationDate = null,
      totalDonations = 0
    )
    assertEquals(true, member.isEligibleToDonateNow)
    assertEquals("এখনই উপযুক্ত", member.nextEligibleDonationDateDisplay)
    assertEquals("এখনো রক্তদান করা হয়নি", member.lastDonationDateDisplay)
    assertEquals("০ বার", member.totalDonationsDisplay)
  }

  @Test
  fun `verify 90 day cooldown blocks donation if recent`() {
    val recentDate = java.util.Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000)) // 10 days ago
    val member = com.example.data.MemberData(
      name = "মুহাম্মদ আব্দুল্লাহ",
      lastDonationDate = com.google.firebase.Timestamp(recentDate),
      totalDonations = 2
    )
    assertEquals(false, member.isEligibleToDonateNow)
    assertEquals("২ বার", member.totalDonationsDisplay)
    org.junit.Assert.assertNotEquals("এখনই উপযুক্ত", member.nextEligibleDonationDateDisplay)
  }

  @Test
  fun `verify 90 day cooldown allows donation if older than 90 days`() {
    val oldDate = java.util.Date(System.currentTimeMillis() - (95L * 24 * 60 * 60 * 1000)) // 95 days ago
    val member = com.example.data.MemberData(
      name = "মুহাম্মদ আব্দুল্লাহ",
      lastDonationDate = com.google.firebase.Timestamp(oldDate),
      totalDonations = 5
    )
    assertEquals(true, member.isEligibleToDonateNow)
    assertEquals("৫ বার", member.totalDonationsDisplay)
  }
}

