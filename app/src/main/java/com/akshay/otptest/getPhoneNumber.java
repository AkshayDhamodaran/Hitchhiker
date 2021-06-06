package com.akshay.otptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class getPhoneNumber extends AppCompatActivity {
    String firstName, lastName, dateOfBirth, College,Gender,about;
    String userPhoneNumber, verification_id;
    FirebaseAuth fAuth;
    String OTP;
    double lattitude = 0.0,longitude = 0.0;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseDatabase rootnode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_phone_number);

        firstName = getIntent().getStringExtra("firstname");
        lastName = getIntent().getStringExtra("lastname");
        dateOfBirth = getIntent().getStringExtra("dob");
        College = getIntent().getStringExtra("college");
        Gender = getIntent().getStringExtra("gender");
        about = "about";


        EditText phoneNumber = findViewById(R.id.editTextPhone);
        Button sendOtp = findViewById(R.id.sendOtp);
        Button Verify = findViewById(R.id.verify);
        EditText Otp = findViewById(R.id.Otp);
        TextView number = findViewById(R.id.enterPhoneNumber);
        TextView otpTitle = findViewById(R.id.otpNumber);
        ProgressBar ProgressBar = findViewById(R.id.progressBar);

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
                                Toast.makeText(getPhoneNumber.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                number.setVisibility(View.GONE);
                                phoneNumber.setVisibility(View.GONE);
                                verificatinUser(userPhoneNumber);
                                sendOtp.setVisibility(View.GONE);
                                otpTitle.setVisibility(View.VISIBLE);
                                Otp.setVisibility(View.VISIBLE);
                                Verify.setVisibility(View.VISIBLE);
                                ProgressBar.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getPhoneNumber.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getPhoneNumber.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getPhoneNumber.this, "Verification failed", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getPhoneNumber.this, "Success", Toast.LENGTH_SHORT).show();
                    rootnode = FirebaseDatabase.getInstance();
                    reference = rootnode.getReference("users");

                    HelperClass helperClass = new HelperClass(firstName, lastName, dateOfBirth, College, userPhoneNumber,Gender,lattitude,longitude,about);
                    reference.child(userPhoneNumber).setValue(helperClass);
                    Intent intent = new Intent(getPhoneNumber.this, SelectHomeLocation.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getPhoneNumber.this, "#####", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}