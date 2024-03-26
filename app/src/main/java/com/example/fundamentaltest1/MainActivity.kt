package com.example.fundamentaltest1

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentaltest1.data.model.ResponseUser
import com.example.fundamentaltest1.data.model.items
import com.example.fundamentaltest1.data.remote.ApiClient
import com.example.fundamentaltest1.databinding.ActivityMainBinding
import com.example.fundamentaltest1.databinding.ItemViewBinding
import com.example.fundamentaltest1.detail.DetailActivity
import com.example.fundamentaltest1.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter {user->
            Intent(this, DetailActivity::class.java).apply {
                putExtra("username", user.login)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }

        viewModel.resultUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<items>)
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar2.isVisible = it.isLoading

                }
            }
        }

        viewModel.getUser()

    }
}