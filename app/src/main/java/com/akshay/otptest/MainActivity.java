package com.akshay.otptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    String userPhoneNumber, verification_id;
    FirebaseAuth fAuth;
    String OTP;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        EditText phoneNumber = findViewById(R.id.editTextPhone);
        Button sendOtp = findViewById(R.id.sendOtp);
        Button Verify = findViewById(R.id.verify);
        EditText Otp = findViewById(R.id.Otp);
        TextView number = findViewById(R.id.enterPhoneNumber);
        TextView otpTitle = findViewById(R.id.otpNumber);
        ProgressBar ProgressBar = findViewById(R.id.progressBar);

        //reSend.setEnabled(false);
        fAuth = FirebaseAuth.getInstance();


        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneNumber.getText().toString().isEmpty() && phoneNumber.getText().toString().length() == 10) {
                    userPhoneNumber = "+91" + phoneNumber.getText().toString().trim();

                    Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("phoneNumber").equalTo(userPhoneNumber);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                number.setVisibility(View.GONE);
                                phoneNumber.setVisibility(View.GONE);
                                verificatinUser(userPhoneNumber);
                                sendOtp.setVisibility(View.GONE);
                                otpTitle.setVisibility(View.VISIBLE);
                                Otp.setVisibility(View.VISIBLE);
                                Verify.setVisibility(View.VISIBLE);
                                ProgressBar.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(MainActivity.this, "Phone number does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Otp.getText().toString().isEmpty()) {
                    Otp.setError("Enter OTP");
                } else {
                    OTP = Otp.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, OTP);
                    authenticateUser(credential);
                }
            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                authenticateUser(phoneAuthCredential);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                token = forceResendingToken;
                verification_id = s;
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
            }
        };

    }

    public void verificatinUser(String userPhoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(fAuth)
                        .setPhoneNumber(userPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void authenticateUser(PhoneAuthCredential credential) {

        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomePageMaps.class);
                    intent.putExtra("phoneNumber",userPhoneNumber);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "#####", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}