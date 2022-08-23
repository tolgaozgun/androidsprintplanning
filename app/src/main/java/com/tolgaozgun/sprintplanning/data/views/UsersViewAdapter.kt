package com.tolgaozgun.sprintplanning.data.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolgaozgun.sprintplanning.databinding.VoteCardBinding
import com.tolgaozgun.sprintplanning.util.Constants

class UsersViewAdapter(private var context: Context,
                      private val listener: UsersViewClickListener) : RecyclerView.Adapter<UsersViewAdapter.UsersViewHolder>() {

    fun interface UsersViewClickListener {
        fun onItemClick(item: Int)
    }

    private var values: IntArray = Constants.VOTE_VALUES

    class UsersViewHolder(private var binding: VoteCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(value: Int, position: Int, listener: UsersViewClickListener){
            with(binding){
                tvValue.text = value.toString()
                myRectangleView.setOnClickListener {
                    listener.onItemClick(value)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val itemBinding = VoteCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return UsersViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(values[position], position, listener)
    }

    override fun getItemCount(): Int {
        return values.count()
    }


}