package com.acun.quran.data.remote.response.surah


import com.google.gson.annotations.SerializedName

data class SurahDetail(
    @SerializedName("name")
    val name: Name,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfVerses")
    val numberOfVerses: Int,
    @SerializedName("preBismillah")
    val preBismillah: Any,
    @SerializedName("revelation")
    val revelation: Revelation,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("tafsir")
    val tafsir: Tafsir,
    @SerializedName("verses")
    val verses: List<Verse>
)