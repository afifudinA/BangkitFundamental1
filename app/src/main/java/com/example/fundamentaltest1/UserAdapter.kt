package com.example.fundamentaltest1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.databinding.ItemViewBinding

class UserAdapter(
    private var items: MutableList<items> = mutableListOf(), private val listener: (items) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class UserViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: items) {
            // Bind data to views here
            binding.image.load(item.avatarUrl)
            binding.username.text = item.login
            // Bind other data as needed
        }
    }

    fun setData(newData: MutableList<items>?) {
        newData?.let {
            items.clear()
            items.addAll(it)
            notifyDataSetChanged()
        }
    }
}
