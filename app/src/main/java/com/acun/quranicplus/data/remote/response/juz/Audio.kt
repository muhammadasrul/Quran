package com.acun.quranicplus.data.remote.response.juz


import com.google.gson.annotations.SerializedName

data class Audio(
    @SerializedName("primary")
    val primary: String,
    @SerializedName("secondary")
    val secondary: List<String>
)