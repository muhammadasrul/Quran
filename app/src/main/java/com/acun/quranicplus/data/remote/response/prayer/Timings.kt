package com.acun.quranicplus.data.remote.response.prayer

import com.acun.quranicplus.data.remote.response.prayer.model.Prayer
import com.acun.quranicplus.data.remote.response.prayer.model.hour
import com.acun.quranicplus.data.remote.response.prayer.model.minute
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
        Prayer(id = 1, name = "Fajr", time = fajr, isNowPrayer = false, isNearestPrayer = false),
        Prayer(id = 2, name = "Dhuhr", time = dhuhr, isNowPrayer = false, isNearestPrayer = false),
        Prayer(id = 3, name = "Asr", time = asr, isNowPrayer = false, isNearestPrayer = false),
        Prayer(id = 4, name = "Maghrib", time = maghrib, isNowPrayer = false, isNearestPrayer = false),
        Prayer(id = 5, name = "Isha", time = isha, isNowPrayer = false, isNearestPrayer = false))

    val cal = Calendar.getInstance()
    run breaking@ {
        prayerList.forEachIndexed { index, prayer ->
            val prayerDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, prayer.hour())
                set(Calendar.MINUTE, prayer.minute())
            }

            if (prayerDate.after(cal)) {
                prayer.isNearestPrayer = true

                if (index == 0) prayerList[prayerList.lastIndex].isNowPrayer = true
                else prayerList[index-1].isNowPrayer = true

                return@breaking
            }

            if (index == prayerList.lastIndex) {
                prayer.isNowPrayer = true
                prayerList.first().isNearestPrayer = true
            }
        }
    }
    return prayerList
}