package com.tolgaozgun.sprintplanning.data.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolgaozgun.sprintplanning.data.model.User
import com.tolgaozgun.sprintplanning.databinding.PlayerCardBinding

class UsersViewAdapter(private var context: Context,
                       private var users: List<User>,
                       private val listener: UsersViewClickListener) : RecyclerView.Adapter<UsersViewAdapter.UsersViewHolder>() {

    fun interface UsersViewClickListener {
        fun onItemClick(user: User)
    }

    class UsersViewHolder(private var binding: PlayerCardBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(user: User, position: Int, listener: UsersViewClickListener){
            with(binding){
                txtName.text = user.name
                txtVoteValue.text = ""
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
        holder.bind(users[position], position, listener)
    }

    override fun getItemCount(): Int {
        return users.count()
    }


}