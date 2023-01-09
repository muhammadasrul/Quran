package com.acun.quranicplus.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Translation(
    @SerializedName("en")
    val en: String,
    @SerializedName("id")
    val id: String
)