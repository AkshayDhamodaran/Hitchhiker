package com.akshay.otptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshay.otptest.Common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class RequestDetails extends AppCompatActivity {
    TextView textView,About;
    String userPhoneNumber;
    ImageView imageView;
    Button accept,reject;
    DatabaseReference reference;
    StorageReference storageReference;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        textView = findViewById(R.id.requestName);
        About = findViewById(R.id.requestAbout);
        imageView = findViewById(R.id.requestProfilePic);
        accept = findViewById(R.id.accept_req);
        reject = findViewById(R.id.reject_req);
        currentUser = Common.CurrentUserModel.getPhoneNumber();

        userPhoneNumber = getIntent().getStringExtra("phoneNumber");
        reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(userPhoneNumber);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fname = (String) snapshot.child("first_name").getValue();
                String lname = (String) snapshot.child("last_name").getValue();
                String name = fname + " " + lname;
                textView.setText(name);
                String about = (String) snapshot.child("about").getValue();
                About.setText(about);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storageReference =  FirebaseStorage.getInstance().getReference().child("profilePics/").child(userPhoneNumber);
        try {
            final File localFile = File.createTempFile("profile",".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("requests").child(Common.CurrentUserModel.getPhoneNumber()).child(userPhoneNumber);
                reference.removeValue();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("yourRequests").child(userPhoneNumber).child(Common.CurrentUserModel.getPhoneNumber());
                reference1.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(RequestDetails.this,requests.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("yourRequests").child(userPhoneNumber).child(Common.CurrentUserModel.getPhoneNumber()).child("status");
                reference.setValue("accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(RequestDetails.this,requests.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}