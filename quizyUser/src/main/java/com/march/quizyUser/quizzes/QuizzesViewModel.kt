package com.march.quizyUser.quizzes

import androidx.lifecycle.ViewModel
import com.march.core.*

class QuizzesViewModel : ViewModel() {

    private val firebaseQuizRepository = FirebaseQuizRepository()
    var quizzesListener: FirebaseQuizzesListener? = null
    var successListener: ((QuizSettings, Participant) -> Unit)? = null
    var failureListener: (() -> Unit)? = null

    fun getQuizzes() {
        quizzesListener?.let {
            firebaseQuizRepository.subscribeOnQuizzes(it)
        }
    }

    fun createParticipant(quiz: QuizSettings, name: String) {
        val participant = Participant(name = name)
        firebaseQuizRepository.createParticipant(quiz.id, participant, object : FirebaseStatusListener {
            override fun onSuccess() {
                successListener?.invoke(quiz, participant)
            }

            override fun onFailure() {
                failureListener?.invoke()
            }

        })
    }
}