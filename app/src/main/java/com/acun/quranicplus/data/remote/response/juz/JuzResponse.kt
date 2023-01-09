package com.acun.quranicplus.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class JuzResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: JuzDetail,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)