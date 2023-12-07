package com.bangkit.instadicoding.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.data.remote.response.ListStoryItem
import com.bangkit.instadicoding.databinding.ActivityMainBinding
import com.bangkit.instadicoding.ui.adapter.StoryListAdapter
import com.bangkit.instadicoding.ui.story.DetailStory
import com.bangkit.instadicoding.ui.story.PostStory
import com.bangkit.instadicoding.ui.welcome.OnBoardActivity
import com.bangkit.instadicoding.utils.ViewModelFactory

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.layoutManager = LinearLayoutManager(this)

        getSession()
        getStories()
        onFabClick()

    }

    private fun getSession() {
        viewModel.getSession().observe(this) { loginResult ->
            if (loginResult.token == "") {
                val intentWelcome = Intent(this, OnBoardActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentWelcome)
                finish()
            }
        }
    }

    private fun getStories() {
        val adapter = StoryListAdapter()
        binding.rvStories.adapter = adapter
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickCallback(object: StoryListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@MainActivity, DetailStory::class.java)
                intent.putExtra(DetailStory.EXTRA_DATA, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.action_map -> {
//                Toast.makeText(this, "Action Map", Toast.LENGTH_SHORT).show()
//            }

            R.id.action_logout -> {
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show()
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onFabClick() {
        binding.postStory.setOnClickListener {
            val intent = Intent(this, PostStory::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getStories()
    }

}