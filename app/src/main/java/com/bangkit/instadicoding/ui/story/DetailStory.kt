package com.bangkit.instadicoding.ui.story

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.instadicoding.R
import com.bangkit.instadicoding.data.remote.response.ListStoryItem
import com.bangkit.instadicoding.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.detail_story)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.rounded_arrow_back_ios_24)
        }


        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }


        setDetailContent(data)
    }

    private fun setDetailContent(data: ListStoryItem?) {
        data?.let {
            binding.apply {
                tvUsernameDetail.text = it.name
                tvDescDetail.text = it.description
                Glide.with(this@DetailStory)
                    .load(it.photoUrl)
                    .into(ivStoryDetail)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}