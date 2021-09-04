package com.ariefzuhri.myspecificnotification.ui

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ariefzuhri.myspecificnotification.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.Intent
import com.ariefzuhri.myspecificnotification.databinding.ActivityMainBinding
import com.ariefzuhri.myspecificnotification.model.Data
import com.ariefzuhri.myspecificnotification.utils.registerToken
import com.ariefzuhri.myspecificnotification.utils.sendNotification
import com.ariefzuhri.myspecificnotification.utils.subscribeToTopic
import com.ariefzuhri.myspecificnotification.utils.unsubscribeFromTopic

/**
 * App features:
 * - Send push notification to a specific user with token
 * - [Coming soon] Send push notifications to multiple users who are subscribed to a specific topic
 * - Get push notifications sent specifically to a specific user
 * - Get push notifications from subscribed topics
 */

// You can change with your UID here
private const val UID_SAMPLE = "I8AyqX33p7b6OG1ltEo5eHd8eXL2"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) launchLogin()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Get token as notification receiver when onNewToken can't handle it
         * because it requires an authenticated user when saving to database
         */
        registerToken()

        /**
         * You need to subscribe to related topics if you send notifications to a specific
         * topic and this is don't need a token when sending a notification
         */
        subscribeToTopic("news")
        unsubscribeFromTopic("news")

        binding.edtUid.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.btnSend.isEnabled = charSequence.toString().isNotEmpty()
            }
        })
        binding.btnSend.isEnabled = false
        binding.btnSend.setOnClickListener {
            val recipientId = binding.edtUid.text.toString()
            val notificationTitle = "Hello, World!"
            val notificationMessage = "You get a new notification"
            val data = Data(binding.edtExtraData.text.toString())
            sendNotification(this, recipientId, notificationTitle, notificationMessage, data)
        }
        binding.btnLogout.setOnClickListener { logout() }
        binding.edtUid.setText(UID_SAMPLE)
    }

    private fun logout() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
        firebaseAuth.signOut()
        launchLogin()
    }

    private fun launchLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}