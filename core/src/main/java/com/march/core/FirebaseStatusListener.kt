package com.march.core

interface FirebaseStatusListener {
    fun onSuccess()
    fun onFailure()
}

interface FirebaseQuizzesListener {
    fun onQuizzesGot(quizzes: List<QuizSettings>)
    fun onFailure()
}

interface FirebaseParticipantsListener {
    fun onParticipantsGot(participants: List<Participant>)
    fun onFailure()
}
