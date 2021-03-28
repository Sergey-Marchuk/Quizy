package com.march.quizyHost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.march.core.*

class QuizParticipantsViewModel: ViewModel() {

    val participants = MutableLiveData<List<Participant>>()
    lateinit var quizSettings: QuizSettings
    private val firebaseQuizRepository = FirebaseQuizRepository()

    fun subscribeOnParticipants() {
        firebaseQuizRepository.subscribeOnParticipants(quizSettings.id, object: FirebaseParticipantsListener {
            override fun onParticipantsGot(participants: List<Participant>) {
                this@QuizParticipantsViewModel.participants.postValue(participants)
            }

            override fun onFailure() {
            }
        })
    }

    fun updateCurrentTime(time: Long) {
        quizSettings.currentTime = time
        firebaseQuizRepository.updateQuiz(quizSettings)
    }

    fun updateQuizStatus(status: QuizStatus) {
        quizSettings.status = status
        firebaseQuizRepository.updateQuiz(quizSettings)
    }

    fun deleteQuiz() {
        firebaseQuizRepository.deleteQuiz(quizSettings.id)
    }

}