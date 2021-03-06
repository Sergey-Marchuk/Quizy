package com.march.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class QuizSettings(
    val id: String = UUID.randomUUID().toString(),
    var quizId: String = "",
    var question: String = "",
    var time: Long = 10000,
    var answersCount: Int = 4,
    var correctAnswer: Int = 0,
    var status: QuizStatus = QuizStatus.NEW,
    var currentTime: Long = time): Parcelable

enum class QuizStatus {
    NEW, STARTED, COMPLETED
}