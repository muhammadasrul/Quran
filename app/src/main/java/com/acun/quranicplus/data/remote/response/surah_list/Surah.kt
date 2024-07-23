package com.acun.quranicplus.data.remote.response.surah_list


import android.os.Parcelable
import com.acun.quranicplus.data.local.room.entity.SurahEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Surah(
    @SerializedName("name")
    val name: Name,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfVerses")
    val numberOfVerses: Int,
    @SerializedName("revelation")
    val revelation: Revelation,
    @SerializedName("sequence")
    val sequence: Int,
    @SerializedName("tafsir")
    val tafsir: Tafsir
): Parcelable {
    fun mapToEntity() = SurahEntity(
        name = name.short,
        number = number,
        translationEn = name.translation.en,
        translationId = name.translation.id,
        transliterationEn = name.transliteration.en,
        transliterationId = name.transliteration.id,
        numberOfVerses = numberOfVerses
    )
}