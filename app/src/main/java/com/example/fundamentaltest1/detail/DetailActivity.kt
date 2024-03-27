package com.example.fundamentaltest1.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fundamentaltest1.R
import com.example.fundamentaltest1.data.model.ResponseDetailUser
import com.example.fundamentaltest1.databinding.ActivityDetailBinding
import com.example.fundamentaltest1.detail.follow.FollowsFragment
import com.example.fundamentaltest1.utils.Result
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra("username") ?: ""
        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    binding.image.load(user.avatarUrl) {
                        transformations(CircleCropTransformation())
                    }
                    binding.toolbar.title = "Detail ${user.login}"
                    binding.nama.text = user.name
                    binding.tab.getTabAt(0)?.orCreateBadge?.number = user.followers
                    binding.tab.getTabAt(1)?.orCreateBadge?.number = user.following
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar2.isVisible = it.isLoading

                }
            }
        }

        viewModel.getDetailUser(username)

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
                    viewModel.getFollowers(username)
                } else viewModel.getFollowings(username)
            }

            override fun onTabUnselected(tab: Tab?) {
            }

            override fun onTabReselected(tab: Tab?) {
            }
        })

        viewModel.getFollowers(username)
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