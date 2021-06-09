package com.ariefzuhri.myspecificnotification.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ariefzuhri.myspecificnotification.R;
import com.ariefzuhri.myspecificnotification.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

// Authentication sample with Firebase
public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) authWithGoogle(account);
                        ***REMOVED*** catch (ApiException e){
                            Log.w(TAG, "Google sign in failed", e);
                        ***REMOVED***
                    ***REMOVED***
                ***REMOVED***
        );

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btnGoogle.setOnClickListener(view -> {
            Intent intentGoogle = googleSignInClient.getSignInIntent();
            someActivityResultLauncher.launch(intentGoogle);
        ***REMOVED***);
    ***REMOVED***

    private void authWithGoogle(GoogleSignInAccount account){
        Log.d(TAG, "authWithGoogle: " + account.getId());
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authWithGoogle(authCredential);
    ***REMOVED***

    public void authWithGoogle(AuthCredential authCredential){
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "signInWithCredential: success");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            ***REMOVED*** else {
                Log.w(TAG, "signInWithCredential: failure", task.getException());
            ***REMOVED***
        ***REMOVED***);
    ***REMOVED***
***REMOVED***