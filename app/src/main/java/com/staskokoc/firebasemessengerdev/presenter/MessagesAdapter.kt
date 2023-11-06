package com.staskokoc.firebasemessengerdev.presenter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.staskokoc.firebasemessengerdev.databinding.ListMessagesBinding
import com.staskokoc.firebasemessengerdev.domain.models.User

class MessagesAdapter : ListAdapter<User, MessagesAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(
        private val binding: ListMessagesBinding,
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(user: User) {
            binding.message.text = "${user.name}: ${user.message}"
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(ListMessagesBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(user = getItem(position))
    }
}