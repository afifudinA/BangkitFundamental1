package com.example.fundamentaltest1.detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fundamentaltest1.R
import com.example.fundamentaltest1.data.local.DbModule
import com.example.fundamentaltest1.data.model.ResponseDetailUser
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.databinding.ActivityDetailBinding
import com.example.fundamentaltest1.detail.follow.FollowsFragment
import com.example.fundamentaltest1.utils.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>(){
        DetailViewModel.Factory(DbModule(this))
    }



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<items>("items")
        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.image.load(user.avatarUrl) {
                        transformations(CircleCropTransformation())
                    }
                    binding.toolbar.title = "Detail ${user.login}"
                    binding.nama.text = user.name
                    binding.followerCount.text = "Followers ${user.followers}"
                    binding.followingCount.text = "Following ${user.following}"
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar2.isVisible = it.isLoading

                }
            }
        }

        viewModel.getDetailUser(item?.login)

        val fragment = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING)
        )
        val titleFragment = mutableListOf(
            getString(R.string.followers), getString(R.string.followings)
        )

        val adapter = DetailAdapter(this, fragment)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posisi ->
            tab.text = titleFragment[posisi]
        }.attach()


        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(item?.login)
                } else viewModel.getFollowings(item?.login)
            }

            override fun onTabUnselected(tab: Tab?) {
            }

            override fun onTabReselected(tab: Tab?) {
            }
        })

        viewModel.getFollowers(item?.login)

        viewModel.resultSuccessFavorite.observe(this){
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultDeleteFavorite.observe(this){
            binding.btnFavorite.changeIconColor(R.color.white)
        }

        binding.btnFavorite.setOnClickListener{
            viewModel.setFavorite(item)
        }
        viewModel.findFavorite(item?.id ?: 0){
            binding.btnFavorite.changeIconColor(R.color.red)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press here
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

fun FloatingActionButton.changeIconColor(@ColorRes color: Int){
    val color = ContextCompat.getColor(this.context, color)
    imageTintList = ColorStateList.valueOf(color)
}