package com.akshay.otptest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.otptest.Common.Common;
import com.akshay.otptest.LoginCheckerModel;
import com.akshay.otptest.ProfileActivity;
import com.akshay.otptest.R;
import com.akshay.otptest.RiderDetails;
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
import java.util.List;

public class NearUserAdapter extends RecyclerView.Adapter<NearUserAdapter.ViewHolder>{
    private List<LoginCheckerModel> userData;
    private Context context;
//    private ItemClickListener mClickListener;
    ViewGroup parentt;
    private String fname;
    private String lname;
    private String name;
    private String userPhoneNumber;

    public NearUserAdapter(List<LoginCheckerModel> mData, Context context) {
        this.userData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        userPhoneNumber = userData.get(position).getPhoneNumber();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        holder.clickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentUserClick = userData.get(position);
                Intent intent = new Intent(context, RiderDetails.class);
                context.startActivity(intent);
            }
        });
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePics/").child(userPhoneNumber);
        try {
            final File localFile = File.createTempFile("profile",".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Akshay", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        reference.child(userPhoneNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               //String college = (String) snapshot.child("college").getValue();
                fname = (String) snapshot.child("first_name").getValue();
                lname = (String) snapshot.child("last_name").getValue();
                name = fname + " " + lname;
                holder.Fname.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return  userData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView Fname;
        ImageView imageView;
        String phone;
        RelativeLayout clickable;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Fname = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewRecyclerView);
            clickable=(RelativeLayout)itemView.findViewById(R.id.clickable);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public void setData(String name) {
            Fname.setText(name);
        }
    }
}
