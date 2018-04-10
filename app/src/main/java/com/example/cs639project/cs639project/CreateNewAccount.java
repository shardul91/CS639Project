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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateNewAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText mSignUpEmail;
    EditText mSignUpPassword;
    Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        mAuth = FirebaseAuth.getInstance();
        mSignUpEmail= findViewById(R.id.signUp_email_holder);
        mSignUpPassword= findViewById(R.id.signUp_password_holder);
        mSignUpButton= findViewById(R.id.signUp_submit);
    }

    public void SignUp(View view) {
        String email= mSignUpEmail.getText().toString().trim();
        String password= mSignUpPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public static final String TAG = "";
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(CreateNewAccount.this, MainActivity.class));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(CreateNewAccount.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Please Enter all the details", Toast.LENGTH_SHORT).show();
        }
    }
}
