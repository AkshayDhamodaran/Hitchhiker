package com.akshay.otptest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akshay.otptest.Common.Common;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePic;
    TextView name,ProfPhoneNumber;
    EditText About;
    String userPhoneNumber;
    String dbFname,dbLname,dbPhone,dbAbout;
    Button update;
    String about;
    Uri imageUri;
    String Name;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = (ImageView) findViewById(R.id.profilePic);
        name = findViewById(R.id.profileName);
        ProfPhoneNumber = findViewById(R.id.phoneProfile);
        About = findViewById(R.id.about);
        update = findViewById(R.id.UpdateAbout);

        user = FirebaseAuth.getInstance().getCurrentUser();

        userPhoneNumber = Common.CurrentUserModel.getPhoneNumber();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getDetails();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about = About.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
                reference.child(dbPhone).child("about").setValue(about).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(android.R.id.content),"About updated",Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "About update failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void getDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        reference.child(userPhoneNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbFname = (String) snapshot.child("first_name").getValue();
                dbLname = (String) snapshot.child("last_name").getValue();
                Name = dbFname + " " + dbLname;
                dbAbout = (String) snapshot.child("about").getValue();
                dbPhone = (String) snapshot.child("phoneNumber").getValue();
                setDetails();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setDetails() {

        ProfPhoneNumber.setText(dbPhone);
        About.setText(dbAbout);
        name.setText(Name);

        StorageReference downloadProfilePic = storageReference.child("profilePics/").child(userPhoneNumber);

        try {
            final File localFile = File.createTempFile("profile",".jpg");
            downloadProfilePic.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profilePic.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
           imageUri = data.getData();
           profilePic.setImageURI(imageUri);
           uploadPicture();
        }
    }

    private void uploadPicture() {
        StorageReference profilePicref = storageReference.child("profilePics/").child(userPhoneNumber);
        profilePicref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Snackbar.make(findViewById(android.R.id.content),"Image uploaded",Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content),"Image upload failed",Snackbar.LENGTH_LONG).show();
            }
        });
    }

}