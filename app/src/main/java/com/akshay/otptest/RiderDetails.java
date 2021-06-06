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
import android.widget.Toast;

import com.akshay.otptest.Common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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

public class RiderDetails extends AppCompatActivity {

 TextView textView,about;
 ImageView imageView;
 Button button;
 String userPhoneNumber;
 String name;
 String About;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_details);


        textView = findViewById(R.id.riderName);
        imageView = findViewById(R.id.riderProfilePic);
        about = findViewById(R.id.riderAbout);
        button = findViewById(R.id.ride_request);

        userPhoneNumber = Common.currentUserClick.getPhoneNumber();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        reference.child(userPhoneNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fname = (String) snapshot.child("first_name").getValue();
                String lname = (String) snapshot.child("last_name").getValue();
                name = fname + " " + lname;
                About = (String) snapshot.child("about").getValue();
                textView.setText(name);
                about.setText(About);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePics/").child(userPhoneNumber);
        try {
            final File localFile = File.createTempFile("profile", ".jpg");
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("requests");
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("yourRequests");
                reference.child(userPhoneNumber).child(Common.CurrentUserModel.getPhoneNumber()).child("phoneNumber").setValue(Common.CurrentUserModel.getPhoneNumber());
                reference.child(userPhoneNumber).child(Common.CurrentUserModel.getPhoneNumber()).child("name").setValue(name);
                reference.child(userPhoneNumber).child(Common.CurrentUserModel.getPhoneNumber()).child("about").setValue(About).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(android.R.id.content),"Request sent",Snackbar.LENGTH_SHORT).show();
                    }
                });
                reference1.child(Common.CurrentUserModel.getPhoneNumber()).child(userPhoneNumber).child("status").setValue("pending");
                reference1.child(Common.CurrentUserModel.getPhoneNumber()).child(userPhoneNumber).child("phoneNumber").setValue(userPhoneNumber);
            }
        });
    }
}