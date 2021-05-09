package com.march.quizyHost

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.march.core.Participant
import kotlinx.android.synthetic.main.item_participant.view.*

class ParticipantsAdapter(private val correctAnswer: Int): RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder>() {

    private val participants = ArrayList<Participant>()
    private val answers = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false)
        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bindParticipant(participants[position])
    }

    override fun getItemCount(): Int = participants.size

    fun updateParticipants(participants: List<Participant>) {
        this.participants.clear()
        this.participants.addAll(participants)
        notifyDataSetChanged()
    }

    fun setAnswers(answers: List<String>) {
        this.answers.addAll(answers)
    }

    inner class ParticipantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindParticipant(participant: Participant) {
            itemView.nameTV.text = participant.name
            val answer = participant.answer
            if (answer >= 0) {
                itemView.answerTV.text = answers[answer]
            }

            itemView.answerTV.visibility = if (answer >= 0) View.VISIBLE else View.INVISIBLE
            val answerColor = getAnswerColor(itemView.context, answer)
            itemView.answerTV.setTextColor(answerColor)
            val drawableRes = if (answer == correctAnswer) R.drawable.ic_correct else R.drawable.ic_wrong
            itemView.answerIV.setImageDrawable(itemView.context.getDrawable(drawableRes))
            itemView.answerIV.imageTintList = ColorStateList.valueOf(answerColor)
            itemView.answerIV.isVisible = answer >= 0
        }

        private fun getAnswerColor(context: Context, answer: Int): Int {
            return ResourcesCompat.getColor(context.resources, if (answer == correctAnswer) {
                R.color.colorGreen
            } else {
                R.color.colorRed
            }, context.theme)
        }
    }
}