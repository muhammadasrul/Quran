package com.acun.quranicplus.data.remote.response.prayer

import com.acun.muslimplus.data.remote.response.prayer_time.Method
import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("latitudeAdjustmentMethod")
    val latitudeAdjustmentMethod: String,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("method")
    val method: Method,
    @SerializedName("midnightMode")
    val midnightMode: String,
    @SerializedName("offset")
    val offset: Offset,
    @SerializedName("school")
    val school: String,
    @SerializedName("timezone")
    val timezone: String
)