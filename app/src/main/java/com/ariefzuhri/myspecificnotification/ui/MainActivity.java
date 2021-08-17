package com.ariefzuhri.myspecificnotification.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.ariefzuhri.myspecificnotification.R;
import com.ariefzuhri.myspecificnotification.databinding.ActivityMainBinding;
import com.ariefzuhri.myspecificnotification.model.Data;
import com.ariefzuhri.myspecificnotification.utils.FcmUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.ariefzuhri.myspecificnotification.utils.FcmUtils.sendNotification;

/* App features:
 * - Send push notification to a specific user with token
 * - [Coming soon] Send push notifications to multiple users who are subscribed to a specific topic
 * - Get push notifications sent specifically to a specific user
 * - Get push notifications from subscribed topics */
public class MainActivity extends AppCompatActivity {

    // You can change with your UID here
    private static final String UID_SAMPLE = "I8AyqX33p7b6OG1ltEo5eHd8eXL2";

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) launchLogin();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* Get token as notification receiver when onNewToken can't handle it
         * because it requires an authenticated user when saving to database */
        FcmUtils.registerToken();

        /* You need to subscribe to related topics if you send notifications to a specific
         * topic and this is don't need a token when sending a notification */
        FcmUtils.subscribeToTopic("news");
        FcmUtils.unsubscribeFromTopic("news");

        binding.edtUid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.btnSend.setEnabled(!charSequence.toString().isEmpty());
            }
        });

        binding.btnSend.setEnabled(false);
        binding.btnSend.setOnClickListener(view -> {
            String recipientId = binding.edtUid.getText().toString();
            String notificationTitle = "Hello, World!";
            String notificationMessage = "You get a new notification";
            Data data = new Data(binding.edtExtraData.getText().toString());
            sendNotification(this, recipientId, notificationTitle, notificationMessage, data);
        });

        binding.btnLogout.setOnClickListener(view -> logout());

        binding.edtUid.setText(UID_SAMPLE);
    }

    private void logout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInClient.signOut();
        firebaseAuth.signOut();

        launchLogin();
    }

    private void launchLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}