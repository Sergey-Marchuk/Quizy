package com.march.quizyUser.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.march.core.Participant
import com.march.core.QuizSettings
import com.march.core.QuizStatus
import com.march.quizyHost.R
import com.march.quizyUser.quizzes.QuizzesFragment
import kotlinx.android.synthetic.main.fragment_quiz.*

class QuizFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!).create(
            QuizViewModel::class.java)
        restoreInstanceState()
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeOnQuiz()
        initListeners()
        initQuestion()
        viewModel.subscribeOnQuiz()
    }

    private fun subscribeOnQuiz() {
        viewModel.quiz.observe(this, {
            if (it.status == QuizStatus.STARTED && viewModel.participant.answer == -1) {
                titleTV.text = resources.getString(R.string.choose_answer, it.currentTime / 1000)
            } else if (it.status == QuizStatus.COMPLETED) {
                finishQuiz()
            }

            answersRV.isVisible = it.status == QuizStatus.STARTED && viewModel.participant.answer == -1
        })
    }

    private fun initRecyclerView() {
        answersRV.layoutManager = GridLayoutManager(context, 2)
        answersRV.adapter = AnswersAdapter(this::onAnswerClicked, viewModel.quizSettings.answersCount)
    }

    private fun restoreInstanceState() {
        arguments?.apply {
            viewModel.quizSettings = getParcelable(QUIZ_KEY) ?: return@apply
            viewModel.participant = getParcelable(PARTICIPANT_KEY) ?: return@apply
        }
    }

    private fun onAnswerClicked(answer: Int) {
        viewModel.sendAnswer(answer)
    }

    private fun initListeners() {
        viewModel.answerSuccessListener = {
            titleTV.text = getString(R.string.answer_confirmed)
            answersRV.isVisible = false
        }

        viewModel.answerFailureListener = {
            Toast.makeText(context, R.string.firebase_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun initQuestion() {
        val question = viewModel.quizSettings.question
        if (question.isNotEmpty()) {
            questionTV.text = getString(R.string.question, question)
        }
    }

    private fun finishQuiz() {
        fragmentManager?.beginTransaction()
            ?.replace(android.R.id.content, QuizzesFragment())
            ?.commit()
    }

    companion object {
        private const val QUIZ_KEY = "quiz_key"
        private const val PARTICIPANT_KEY = "participant_key"

        fun newInstance(quizSettings: QuizSettings, participant: Participant): QuizFragment {
            val args = Bundle()
            args.putParcelable(QUIZ_KEY, quizSettings)
            args.putParcelable(PARTICIPANT_KEY, participant)

            val fragment = QuizFragment()
            fragment.arguments = args

            return fragment
        }
    }
}