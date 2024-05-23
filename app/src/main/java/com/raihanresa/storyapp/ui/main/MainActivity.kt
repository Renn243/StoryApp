package com.raihanresa.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.raihanresa.storyapp.data.ResultState
import com.raihanresa.storyapp.data.local.UserPreference
import com.raihanresa.storyapp.databinding.ActivityMainBinding
import com.raihanresa.storyapp.ui.ViewModelFactory
import com.raihanresa.storyapp.ui.add.AddStoryActivity
import com.raihanresa.storyapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: ItemStoryAdapter

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObserver()

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = ItemStoryAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun setupObserver() {
        mainViewModel.getStories().observe(this, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    storyAdapter.submitList(result.data.listStory)
                }
                is ResultState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, result.error ?: "Unknown error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                performLogout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun performLogout() {
        val userPreference = UserPreference.getInstance(this)
        lifecycleScope.launch {
            userPreference.clear()
            val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getStories()
    }
}