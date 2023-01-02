package com.acun.quran.data.remote.response.prayer

import com.acun.quran.data.remote.response.prayer.model.Prayer
import com.acun.quran.data.remote.response.prayer.model.hour
import com.acun.quran.data.remote.response.prayer.model.minute
import com.google.gson.annotations.SerializedName
import java.util.*

data class Timings(
    @SerializedName("Asr")
    val asr: String,
    @SerializedName("Dhuhr")
    val dhuhr: String,
    @SerializedName("Fajr")
    val fajr: String,
    @SerializedName("Imsak")
    val imsak: String,
    @SerializedName("Isha")
    val isha: String,
    @SerializedName("Maghrib")
    val maghrib: String,
    @SerializedName("Midnight")
    val midnight: String,
    @SerializedName("Sunrise")
    val sunrise: String,
    @SerializedName("Sunset")
    val sunset: String
)

fun Timings.toPrayerList(): List<Prayer> {
    val prayerList = listOf(
        Prayer(name = "Fajr", time = fajr, isNowPrayer = false, isNearestPrayer = false),
        Prayer(name = "Dhuhr", time = dhuhr, isNowPrayer = false, isNearestPrayer = false),
        Prayer(name = "Asr", time = asr, isNowPrayer = false, isNearestPrayer = false),
        Prayer(name = "Maghrib", time = maghrib, isNowPrayer = false, isNearestPrayer = false),
        Prayer(name = "Isha", time = isha, isNowPrayer = false, isNearestPrayer = false))

    val cal = Calendar.getInstance()
    val nowPrayer = prayerList.lastOrNull { cal.get(Calendar.HOUR_OF_DAY) >= it.hour() && cal.get(Calendar.MINUTE) >= it.minute()}
    if (nowPrayer != null) {
        prayerList[prayerList.indexOf(nowPrayer)].isNowPrayer = true

        if (prayerList.indexOf(nowPrayer) != prayerList.lastIndex) {
            prayerList[prayerList.indexOf(nowPrayer)+1].isNearestPrayer = true
        } else {
            prayerList[0].isNearestPrayer = true
        }
    } else {
        prayerList.last().isNowPrayer = true
        prayerList[0].isNearestPrayer = true
    }
    return prayerList
}

fun Timings.getNowPrayer(): Prayer? {
    return toPrayerList().firstOrNull { it.isNowPrayer }
}

fun Timings.getNearestPrayer(): Prayer? {
    return toPrayerList().firstOrNull { it.isNearestPrayer }
}