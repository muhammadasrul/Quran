package com.acun.quranicplus.data.remote.response.juz_list


import com.google.gson.annotations.SerializedName

data class JuzListResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Juz>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)