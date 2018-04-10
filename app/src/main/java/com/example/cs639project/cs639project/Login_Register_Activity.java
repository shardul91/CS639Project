package com.example.cs639project.cs639project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Register_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText mSignIn_email;
    EditText mSignIn_password;
    Button mSignIn_button;
    SignInButton mGoogle_button;
    TextView mForgot_password;
    TextView mCreate_new_account;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__register_);
        //references to the views i the layout
        mAuth = FirebaseAuth.getInstance();
        mSignIn_email= findViewById(R.id.signIn_email_holder);
        mSignIn_password= findViewById(R.id.signIn_password_holder);
        mSignIn_button= findViewById(R.id.signIn_submit);
        mGoogle_button= findViewById(R.id.google_signIn_button);
        mForgot_password= findViewById(R.id.forgot_password);
        mCreate_new_account= findViewById(R.id.create_new_account);
        progressBar= findViewById(R.id.progress_bar);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

    }

    public void singInMethod(View view) {
        String email= mSignIn_email.getText().toString().trim();
        String password= mSignIn_password.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public static final String TAG = "";
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(Login_Register_Activity.this, MainActivity.class));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login_Register_Activity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "Please enter valid email and password", Toast.LENGTH_SHORT).show();
        }

    }

    public void SignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Failed to Log-In", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login_Register_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(Login_Register_Activity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login_Register_Activity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    public void createNewAccountMethod(View view) {
        startActivity(new Intent(Login_Register_Activity.this, CreateNewAccount.class));
    }

    public void forgotPasswordMethod(View view) {
    }
}
