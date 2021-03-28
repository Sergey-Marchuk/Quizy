package com.march.quizyHost

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.march.core.QuizSettings
import com.march.core.QuizStatus
import com.march.quizyHost.presentation.CreateQuizFragment
import kotlinx.android.synthetic.main.fragment_quiz_participants.*

class QuizParticipantsFragment : Fragment() {

    private lateinit var viewModel: QuizParticipantsViewModel
    private lateinit var adapter: ParticipantsAdapter
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application!!).create(QuizParticipantsViewModel::class.java)
        restoreArguments()
        return inflater.inflate(R.layout.fragment_quiz_participants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initCountDownTimer()
        initListeners()
        subscribeOnParticipants()
        viewModel.subscribeOnParticipants()
    }

    private fun initRecyclerView() {
        adapter = ParticipantsAdapter(viewModel.quizSettings.correctAnswer)
        participantsRV.layoutManager = LinearLayoutManager(context)
        participantsRV.adapter = adapter
    }

    private fun restoreArguments() {
        arguments?.apply {
            viewModel.quizSettings = getParcelable(QUIZ_KEY) ?: return@apply
        }
    }

    private fun subscribeOnParticipants() {
        viewModel.participants.observe(this) {
            adapter.updateParticipants(it)
        }
    }

    private fun initListeners() {
        startQuizMB.setOnClickListener {
            if (viewModel.quizSettings.status == QuizStatus.COMPLETED) {
                viewModel.deleteQuiz()
                finishQuiz()
            } else {
                viewModel.updateQuizStatus(QuizStatus.STARTED)
                countDownTimer.start()
                startQuizMB.isVisible = false
            }
        }
    }

    private fun finishQuiz() {
        fragmentManager?.beginTransaction()
            ?.replace(android.R.id.content, CreateQuizFragment())
            ?.commit()
    }

    private fun initCountDownTimer() {
        countDownTimer = object: CountDownTimer(viewModel.quizSettings.time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.updateCurrentTime(millisUntilFinished)
                titleTV.text = resources.getString(R.string.time_left, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                viewModel.updateQuizStatus(QuizStatus.COMPLETED)
                startQuizMB.text = getText(R.string.finish_quiz)
                startQuizMB.isVisible = true
            }

        }
    }

    companion object {
        private const val QUIZ_KEY = "quiz_key"

        fun newInstance(quizSettings: QuizSettings): QuizParticipantsFragment {
            val args = Bundle()
            args.putParcelable(QUIZ_KEY, quizSettings)

            val fragment = QuizParticipantsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}