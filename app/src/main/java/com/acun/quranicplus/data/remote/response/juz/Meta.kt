package com.acun.quranicplus.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("hizbQuarter")
    val hizbQuarter: Int,
    @SerializedName("juz")
    val juz: Int,
    @SerializedName("manzil")
    val manzil: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("ruku")
    val ruku: Int,
    @SerializedName("sajda")
    val sajda: Sajda
)