package com.acun.quranicplus.data.remote.response.prayer


import com.google.gson.annotations.SerializedName

data class Designation(
    @SerializedName("abbreviated")
    val abbreviated: String,
    @SerializedName("expanded")
    val expanded: String
)