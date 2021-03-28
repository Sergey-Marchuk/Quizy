package com.march.quizyUser.quiz

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AnswersAdapter(private val clickListener: (Int) -> Unit,
                     private val answersCount: Int): RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = Button(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bindButton((position + 1).toString())
    }

    override fun getItemCount(): Int = answersCount

    inner class AnswerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val text = (this.itemView as Button).text.toString()
                clickListener.invoke(text.toInt()) }
        }

        fun bindButton(text: String) {
            (itemView as Button).text = text
        }
    }
}