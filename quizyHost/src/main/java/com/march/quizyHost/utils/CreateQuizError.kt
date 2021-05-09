package com.march.quizyHost.utils

import com.march.quizyHost.R

enum class CreateQuizError(val errorResId: Int, var index: Int = -1) {
    QUIZ_ID(R.string.quiz_id_error), QUIZ_TIME(R.string.quiz_time_error), FIREBASE_ERROR(R.string.firebase_error),
    ANSWER_ERROR(R.string.answer_error)
}