package com.march.quizyUser.quiz

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.march.core.*

class QuizViewModel: ViewModel() {

    val quiz: MutableLiveData<QuizSettings> = MutableLiveData()
    lateinit var quizSettings: QuizSettings
    lateinit var participant: Participant
    var answerSuccessListener: (() -> Unit)? = null
    var answerFailureListener: (() -> Unit)? = null
    private val firebaseQuizRepository = FirebaseQuizRepository()

    fun subscribeOnQuiz() {
        firebaseQuizRepository.subscribeOnQuiz(quizSettings.id, object : FirebaseQuizzesListener {

            override fun onQuizzesGot(quizzes: List<QuizSettings>) {
                quiz.postValue(quizzes[0])
            }

            override fun onFailure() {
            }
        })
    }

    fun sendAnswer(answer: String) {
        participant.answer = quizSettings.answers.indexOf(answer)
        Log.e("TAG", "answers: ${quizSettings.answers}")
        Log.e("TAG",  "index: ${quizSettings.answers.indexOf(answer)}")
        Log.e("TAG", "correctAnswer: ${quizSettings.correctAnswer}")
        firebaseQuizRepository.updateParticipant(quizSettings.id, participant, object : FirebaseStatusListener {
            override fun onSuccess() {
                answerSuccessListener?.invoke()
            }

            override fun onFailure() {
                answerFailureListener?.invoke()
            }
        })
    }
}