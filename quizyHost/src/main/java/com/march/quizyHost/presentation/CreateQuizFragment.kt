package com.march.quizyHost.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.march.quizyHost.QuizParticipantsFragment
import com.march.quizyHost.R
import com.march.quizyHost.utils.CreateQuizError
import kotlinx.android.synthetic.main.fragment_create_quiz.*

class CreateQuizFragment : Fragment() {

    private lateinit var viewModel: CreateQuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(CreateQuizViewModel::class.java)
        return inflater.inflate(R.layout.fragment_create_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initAnswersDropdown()
        invalidateCorrectAnswerDropdown()
    }

    private fun initListeners() {
        quizIdET.onTextChanged { viewModel.setQuizId(it) }
        questionET.onTextChanged { viewModel.setQuizQuestion(it) }
        timeET.onTextChanged { viewModel.setQuizTime(it) }
        correctAnswerATV.onItemSelected { viewModel.setCorrectAnswer(it) }
        createQuizMB.setOnClickListener { viewModel.createQuiz() }
        answersCountATV.onItemSelected{
            viewModel.setAnswersCount(it)
            invalidateCorrectAnswerDropdown()
        }
        viewModel.onValidationError = {
            when(it) {
                CreateQuizError.QUIZ_TIME -> timeET.error = resources.getString(it.errorResId)
                CreateQuizError.QUIZ_ID -> quizIdET.error = resources.getString(it.errorResId)
                else -> Toast.makeText(context, resources.getString(it.errorResId), Toast.LENGTH_LONG).show()
            }
        }
        viewModel.onNavigateNext = {
            fragmentManager?.beginTransaction()
                ?.replace(android.R.id.content, QuizParticipantsFragment.newInstance(it))
                ?.commit()
        }
    }

    private fun initAnswersDropdown() {
        val answers = resources.getStringArray(R.array.answers_numbers).drop(1)
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, answers)
        answersCountATV.setAdapter(adapter)
        answersCountATV.setText(answers[0], false)
    }

    private fun invalidateCorrectAnswerDropdown() {
        val answersCount = answersCountATV.text.toString().toInt()
        var answers = resources.getStringArray(R.array.answers_numbers)
        answers = answers.dropLast(answers.size - answersCount).toTypedArray()
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, answers)
        correctAnswerATV.setAdapter(adapter)
        correctAnswerATV.setText(answers[0], false)
    }

    private fun TextInputEditText.onTextChanged(listener: (String) -> Unit) {
        doAfterTextChanged { text ->
            text?.toString()?.let { listener.invoke(it) }
        }
    }

    private fun AutoCompleteTextView.onItemSelected(callback: (String) -> Unit) {
        setOnItemClickListener { _, view, _, _ ->
            val selectedValue = (view as TextView).text.toString()
            callback.invoke(selectedValue)
        }
    }

}
