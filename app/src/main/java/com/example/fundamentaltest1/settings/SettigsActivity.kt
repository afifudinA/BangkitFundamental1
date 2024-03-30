package com.example.fundamentaltest1.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fundamentaltest1.R
import com.example.fundamentaltest1.databinding.ActivitySettigsBinding
import com.mbahgojol.exprojectgithub.data.local.SettingPreferences

class SettigsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettigsBinding
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory(SettingPreferences(this))
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySettigsBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getheme().observe(this){
            if (it){
                binding.switchMaterial.text = "Dark Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else{
                binding.switchMaterial.text = "Light Theme"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.switchMaterial.isChecked = it
        }

        binding.switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveTheme(isChecked)
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