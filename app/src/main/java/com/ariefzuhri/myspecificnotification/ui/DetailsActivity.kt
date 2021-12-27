package com.ariefzuhri.myspecificnotification.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ariefzuhri.myspecificnotification.databinding.ActivityDetailsBinding
import com.ariefzuhri.myspecificnotification.util.EXTRA_MESSAGE

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showExtraMessage()
    }

    private fun showExtraMessage() {
        if (intent.hasExtra(EXTRA_MESSAGE)) {
            val extraMessage = intent.getStringExtra(EXTRA_MESSAGE)
            binding.textView.text = extraMessage
        }
    }
}