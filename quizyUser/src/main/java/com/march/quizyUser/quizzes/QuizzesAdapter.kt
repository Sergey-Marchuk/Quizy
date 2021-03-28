package com.march.quizyUser.quizzes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.march.core.QuizSettings
import com.march.quizyHost.R
import kotlinx.android.synthetic.main.item_quiz.view.*

class QuizzesAdapter : RecyclerView.Adapter<QuizzesAdapter.QuizViewHolder>() {

    var quizzes = ArrayList<QuizSettings>()
    var onClickListener: ((QuizSettings) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun getItemCount(): Int = quizzes.size

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bindQuiz(quizzes[position])
    }

    fun setItems(quizzes: List<QuizSettings>) {
        this.quizzes.clear()
        this.quizzes.addAll(quizzes)
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindQuiz(quizSettings: QuizSettings) {
            itemView.quizTitleTV.text = quizSettings.quizId
            itemView.setOnClickListener {
                onClickListener?.invoke(quizSettings)
            }
        }
    }
}