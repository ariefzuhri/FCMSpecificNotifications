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
import com.ariefzuhri.myspecificnotification.data.model.Data
import com.ariefzuhri.myspecificnotification.util.registerToken
import com.ariefzuhri.myspecificnotification.util.sendNotification
import com.ariefzuhri.myspecificnotification.util.subscribeToTopic
import com.ariefzuhri.myspecificnotification.util.unsubscribeFromTopic

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

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLoggedInUser()

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

        initViews()
    }

    private fun checkLoggedInUser() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) launchLogin()
    }

    private fun initViews() {
        binding.apply {
            edtUid.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence, i: Int, i1: Int, i2: Int,
                ) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    btnSend.isEnabled = charSequence.toString().isNotEmpty()
                }

                override fun afterTextChanged(editable: Editable) {

                }
            })

            btnSend.isEnabled = false
            btnSend.setOnClickListener {
                val recipientId = edtUid.text.toString()
                val notificationTitle = "Hello, World!"
                val notificationMessage = "You get a new notification"
                val data = Data(edtExtraData.text.toString())

                sendNotification(
                    this@MainActivity,
                    recipientId,
                    notificationTitle, notificationMessage,
                    data
                )
            }

            btnLogout.setOnClickListener { logout() }

            edtUid.setText(UID_SAMPLE)
        }
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