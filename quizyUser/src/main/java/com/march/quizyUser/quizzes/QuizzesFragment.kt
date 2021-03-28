package com.march.quizyUser.quizzes

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.march.core.FirebaseQuizzesListener
import com.march.core.QuizSettings
import com.march.quizyHost.R
import com.march.quizyUser.quiz.QuizFragment
import kotlinx.android.synthetic.main.fragment_quizzes.*

class QuizzesFragment : Fragment() {

    private lateinit var viewModel: QuizzesViewModel
    private lateinit var quizzesAdapter: QuizzesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
            QuizzesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_quizzes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initListeners()
        viewModel.getQuizzes()
    }

    private fun onQuizClicked(quizSettings: QuizSettings) {
        showAlertDialog(quizSettings)
    }

    private fun showAlertDialog(quizSettings: QuizSettings) {
        MaterialDialog(context!!).show {
            title(res = R.string.input_name)
            input(inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS, allowEmpty = false)
            positiveButton(R.string.confirm) {
                viewModel.createParticipant(quizSettings, it.getInputField().text.toString())
            }
            negativeButton(R.string.cancel)
        }
    }

    private fun initRecycler() {
        quizzesRV.layoutManager = LinearLayoutManager(context)
        quizzesAdapter = QuizzesAdapter()
        quizzesRV.adapter = quizzesAdapter
        quizzesAdapter.onClickListener = this::onQuizClicked
    }

    private fun initListeners() {
        viewModel.quizzesListener = object: FirebaseQuizzesListener {
            override fun onQuizzesGot(quizzes: List<QuizSettings>) {
                quizzesAdapter.setItems(quizzes)
                hintTV.isVisible = quizzes.isEmpty()
            }

            override fun onFailure() {
                Toast.makeText(context, R.string.firebase_error, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.successListener = { quiz, participant ->
            fragmentManager?.beginTransaction()
                ?.replace(android.R.id.content, QuizFragment.newInstance(quiz, participant))
                ?.commit()
        }

        viewModel.failureListener = {
            Toast.makeText(context, getString(R.string.firebase_error), Toast.LENGTH_LONG).show()
        }
    }
}