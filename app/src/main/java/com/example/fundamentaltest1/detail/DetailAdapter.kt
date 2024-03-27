package com.example.fundamentaltest1.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailAdapter(Fa: FragmentActivity, private val fragment: MutableList<Fragment>) :
    FragmentStateAdapter(Fa) {
    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]
}