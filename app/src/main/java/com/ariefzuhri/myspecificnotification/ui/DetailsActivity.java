package com.ariefzuhri.myspecificnotification.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ariefzuhri.myspecificnotification.databinding.ActivityDetailsBinding;

import static com.ariefzuhri.myspecificnotification.utils.Constants.EXTRA_MESSAGE;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MESSAGE)) {
            String extraMessage = intent.getStringExtra(EXTRA_MESSAGE);
            binding.textView.setText(extraMessage);
        }
    }
}