package com.march.core

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseQuizRepository {

    companion object {
        private const val QUIZZES_COLLECTION_PATH = "quizzes"
        private const val PARTICIPANTS_COLLECTION_PATH = "participants"
        private const val STATUS_FIELD = "status"
        private const val ID_FIELD = "id"
    }

    private val firestore: FirebaseFirestore = Firebase.firestore

    fun saveQuizSettings(quizSettings: QuizSettings, statusListener: FirebaseStatusListener) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .document(quizSettings.id)
            .set(quizSettings)
            .addOnSuccessListener {
                statusListener.onSuccess()
            }
            .addOnFailureListener { statusListener.onFailure() }
    }

    fun subscribeOnQuizzes(listener: FirebaseQuizzesListener) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .whereEqualTo(STATUS_FIELD, QuizStatus.NEW)
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    listener.onFailure()
                    return@addSnapshotListener
                }

                if (value != null && !value.isEmpty) {
                    val quizzes = value.toObjects(QuizSettings::class.java)
                    listener.onQuizzesGot(quizzes)
                }
            }
    }

    fun subscribeOnQuiz(quizId: String, listener: FirebaseQuizzesListener) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .whereEqualTo(ID_FIELD, quizId)
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    listener.onFailure()
                    return@addSnapshotListener
                }

                if (value != null && !value.isEmpty) {
                    val quizzes = value.toObjects(QuizSettings::class.java)
                    listener.onQuizzesGot(quizzes)
                }

            }
    }

    fun createParticipant(quizId: String, participant: Participant, statusListener: FirebaseStatusListener) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .document(quizId)
            .collection(PARTICIPANTS_COLLECTION_PATH)
            .document(participant.id)
            .set(participant)
            .addOnSuccessListener {
                statusListener.onSuccess()
            }
            .addOnFailureListener { statusListener.onFailure() }
    }

    fun subscribeOnParticipants(quizId: String, listener: FirebaseParticipantsListener) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .document(quizId)
            .collection(PARTICIPANTS_COLLECTION_PATH)
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    listener.onFailure()
                    return@addSnapshotListener
                }

                if (value != null && !value.isEmpty) {
                    val participants = value.toObjects(Participant::class.java)
                    listener.onParticipantsGot(participants)
                }
            }
    }

    fun updateQuiz(quizSettings: QuizSettings) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .document(quizSettings.id)
            .set(quizSettings)
    }

    fun updateParticipant(quizId: String, participant: Participant, statusListener: FirebaseStatusListener) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .document(quizId)
            .collection(PARTICIPANTS_COLLECTION_PATH)
            .document(participant.id)
            .set(participant)
            .addOnSuccessListener {
                statusListener.onSuccess()
            }
            .addOnFailureListener { statusListener.onFailure() }
    }

    fun deleteQuiz(quizId: String) {
        firestore.collection(QUIZZES_COLLECTION_PATH)
            .document(quizId)
            .delete()
    }

}