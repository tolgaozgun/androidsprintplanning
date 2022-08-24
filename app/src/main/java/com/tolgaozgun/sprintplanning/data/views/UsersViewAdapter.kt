package com.tolgaozgun.sprintplanning.data.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.databinding.PlayerCardBinding

class UsersViewAdapter(private var context: Context,
                       private var users: List<User>,
                       private var showResults: Boolean,
                       private val listener: UsersViewClickListener) : RecyclerView.Adapter<UsersViewAdapter.UsersViewHolder>() {

    fun interface UsersViewClickListener {
        fun onItemClick(user: User)
    }

    class UsersViewHolder(private var binding: PlayerCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(user: User, position: Int, showResults: Boolean, listener: UsersViewClickListener){
            with(binding){
                txtName.text = user.name
                if(showResults && user.hasVoted){
                    txtVoteValue.text = user.vote.toString()
                }else{
                    txtVoteValue.text = ""
                }

                constraintLayoutPlayerCard.setOnClickListener {
                    listener.onItemClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val itemBinding = PlayerCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return UsersViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position], position, showResults, listener)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    fun updateList(list: List<User>, showResults: Boolean){
        this.users = list
        this.showResults = showResults
        notifyDataSetChanged()
    }


}