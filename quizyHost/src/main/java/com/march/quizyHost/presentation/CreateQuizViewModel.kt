package com.march.quizyHost.presentation

import androidx.lifecycle.ViewModel
import com.march.core.FirebaseQuizRepository
import com.march.core.FirebaseStatusListener
import com.march.core.QuizSettings
import com.march.quizyHost.utils.CreateQuizError

class CreateQuizViewModel: ViewModel() {

    private val quizSettings = QuizSettings()
    private val firebaseQuizRepository = FirebaseQuizRepository()

    var onValidationError: ((CreateQuizError) -> Unit)? = null
    var onNavigateNext: ((QuizSettings) -> Unit)? = null


    fun setQuizId(quizId: String) {
        quizSettings.quizId = quizId
    }

    fun setQuizQuestion(question: String) {
        quizSettings.question = question
    }

    fun setQuizTime(quizTime: String) {
        quizSettings.time = (quizTime.toLongOrNull() ?: 0) * 1000
    }

    fun setAnswersCount(answersCount: String) {
        quizSettings.answersCount = answersCount.toInt()
    }

    fun setCorrectAnswer(correctAnswer: String) {
        quizSettings.correctAnswer = correctAnswer.toInt()
    }

    fun createQuiz() {
        if (validateSettings()) {
            firebaseQuizRepository.saveQuizSettings(quizSettings, object :
                FirebaseStatusListener {
                override fun onSuccess() {
                    onNavigateNext?.invoke(quizSettings)
                }

                override fun onFailure() {
                    onValidationError?.invoke(CreateQuizError.FIREBASE_ERROR)
                }
            })
        }
    }

    private fun validateSettings(): Boolean {
        var isValidationCorrect = true
        if (quizSettings.quizId.isEmpty()) {
            onValidationError?.invoke(CreateQuizError.QUIZ_ID)
            isValidationCorrect = false
        }

        if (quizSettings.time < 5) {
            onValidationError?.invoke(CreateQuizError.QUIZ_TIME)
            isValidationCorrect = false
        }

        return isValidationCorrect
    }
}