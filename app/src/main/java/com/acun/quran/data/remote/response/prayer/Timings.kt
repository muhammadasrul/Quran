package com.acun.quran.data.remote.response.prayer

import com.acun.quran.data.remote.response.prayer.model.Prayer
import com.acun.quran.data.remote.response.prayer.model.hour
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
    return listOf(
        Prayer(name = "Imsak", time = imsak),
        Prayer(name = "Fajr", time = fajr),
        Prayer(name = "Dhuhr", time = dhuhr),
        Prayer(name = "Asr", time = asr),
        Prayer(name = "Maghrib", time = maghrib),
        Prayer(name = "Isha", time = isha))
}

fun Timings.getNearestPrayer(): Prayer? {
    var nearestPrayer: Prayer? = null
    toPrayerList().forEachIndexed { index, prayer ->
        val cal = Calendar.getInstance(Locale.getDefault())

        if (index == toPrayerList().lastIndex) {
            nearestPrayer = toPrayerList()[1]
        }
        if (prayer.hour() >= cal.get(Calendar.HOUR_OF_DAY)) {
            nearestPrayer = prayer
            return@forEachIndexed
        }
    }
    return nearestPrayer
}

fun Timings.getNowPrayer(): Prayer? {
    var nowPrayer: Prayer? = null
    toPrayerList().forEachIndexed { index, prayer ->
        val cal = Calendar.getInstance(Locale.getDefault())

        if (nowPrayer != null) {
            return@forEachIndexed
        }
        if (index == toPrayerList().lastIndex) {
            nowPrayer = toPrayerList()[1]
        }
        if (prayer.hour() >= cal.get(Calendar.HOUR_OF_DAY)) {
            nowPrayer = if (index <= 1) {
                toPrayerList().last()
            } else {
                toPrayerList()[index-1]
            }
            return@forEachIndexed
        }
    }
    return nowPrayer
}
