package com.ariefzuhri.myspecificnotification.ui

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ariefzuhri.myspecificnotification.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthResult
import android.util.Log
import androidx.activity.result.ActivityResult
import com.ariefzuhri.myspecificnotification.databinding.ActivityLoginBinding
import com.ariefzuhri.myspecificnotification.util.TAG
import com.google.android.gms.tasks.Task

// Simple authentication using Firebase
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAuth()
    }

    private fun initAuth() {
        val someActivityResultLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.let { authWithGoogle(it) }
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            someActivityResultLauncher.launch(intent)
        }
    }

    private fun authWithGoogle(account: GoogleSignInAccount) {
        Log.d(TAG, "authWithGoogle: ${account.id}")
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        authWithGoogle(authCredential)
    }

    private fun authWithGoogle(authCredential: AuthCredential) {
        firebaseAuth.signInWithCredential(authCredential)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential: success")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(TAG, "signInWithCredential: failure", task.exception)
                }
            }
    }
}