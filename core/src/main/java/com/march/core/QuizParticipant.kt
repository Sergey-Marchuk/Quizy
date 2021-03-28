package com.march.core

import java.util.*

data class QuizParticipant(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var answer: Int? = null
)