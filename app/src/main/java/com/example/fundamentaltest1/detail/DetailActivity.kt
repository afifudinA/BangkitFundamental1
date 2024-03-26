package com.example.fundamentaltest1.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fundamentaltest1.R
import com.example.fundamentaltest1.data.model.ResponseDetailUser
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.databinding.ActivityDetailBinding
import com.example.fundamentaltest1.utils.Result

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