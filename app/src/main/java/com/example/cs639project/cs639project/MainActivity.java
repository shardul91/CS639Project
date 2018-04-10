package com.example.cs639project.cs639project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get firebase instance
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser== null){
            startActivity(new Intent(MainActivity.this, Login_Register_Activity.class));
            finish();
        }else return;
    }

    //inflate the menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //two options in the menu.
        // 1. logout and 2. settings
        switch (item.getItemId()){
            case R.id.logout_button:
                logout();
                return true;
            case R.id.settings_button:
                setting();
                return true;
            default:    return super.onOptionsItemSelected(item);
        }
    }

    private void setting() {
        //settings activity
    }

    private void logout() {
        //logout the current user.
        try {
            mAuth.signOut();
            Toast.makeText(this, "User Logged out", Toast.LENGTH_SHORT).show();
            recreate();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while Logging out", Toast.LENGTH_SHORT).show();
        }
    }
}
