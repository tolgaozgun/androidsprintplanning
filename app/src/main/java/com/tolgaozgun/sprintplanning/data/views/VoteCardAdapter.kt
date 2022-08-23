package com.tolgaozgun.sprintplanning.data.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolgaozgun.sprintplanning.databinding.VoteCardBinding
import com.tolgaozgun.sprintplanning.util.Constants

class VoteCardAdapter(private var context: Context,
                      private val listener: VoteCardClickListener) : RecyclerView.Adapter<VoteCardAdapter.VoteCardViewHolder>() {

    fun interface VoteCardClickListener {
        fun onItemClick(item: Int)
    }

    private var values: IntArray = Constants.VOTE_VALUES

    class VoteCardViewHolder(private var binding: VoteCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(value: Int, position: Int, listener: VoteCardClickListener){
            with(binding){
                tvValue.text = value.toString()
                myRectangleView.setOnClickListener {
                    listener.onItemClick(value)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteCardViewHolder {
        val itemBinding = VoteCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return VoteCardViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VoteCardViewHolder, position: Int) {
        holder.bind(values[position], position, listener)
    }

    override fun getItemCount(): Int {
        return values.count()
    }

}