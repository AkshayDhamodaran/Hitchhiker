package com.akshay.otptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_check extends AppCompatActivity {
    TextView textView;
    FirebaseAuth mFirebaseAuth;
    Button logout;
    String userPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check);

        textView = findViewById(R.id.currentUser);

        mFirebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userPhoneNumber = mFirebaseUser.getPhoneNumber();
        if (mFirebaseUser!=null){
            textView.setText(mFirebaseUser.getPhoneNumber());
        }else {
            Intent intent = new Intent(user_check.this,Landing_screen.class);
            startActivity(intent);
            finish();
        }
    }
}