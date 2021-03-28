package com.march.quizyHost

import android.content.Context
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
        Log.e("TAG", "updateParticipants")
        notifyDataSetChanged()
    }

    inner class ParticipantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindParticipant(participant: Participant) {
            itemView.nameTV.text = participant.name
            val answer = participant.answer
            Log.e("TAG", answer.toString())
            itemView.answerTV.text = answer.toString()
            itemView.answerTV.isVisible = answer > 0
            itemView.answerTV.setTextColor(getAnswerColor(itemView.context, answer))
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