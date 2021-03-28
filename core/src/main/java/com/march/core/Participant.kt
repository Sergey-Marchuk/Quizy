package com.march.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Participant(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var answer: Int = -1): Parcelable