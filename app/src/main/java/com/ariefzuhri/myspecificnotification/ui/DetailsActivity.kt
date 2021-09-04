package com.ariefzuhri.myspecificnotification.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ariefzuhri.myspecificnotification.databinding.ActivityDetailsBinding
import com.ariefzuhri.myspecificnotification.utils.EXTRA_MESSAGE

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_MESSAGE)) {
            val extraMessage = intent.getStringExtra(EXTRA_MESSAGE)
            binding.textView.text = extraMessage
        }
    }
}