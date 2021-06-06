package com.akshay.otptest;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView imageView;
    RelativeLayout clickable;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.name);
        imageView = itemView.findViewById(R.id.imageViewRecyclerView);
        clickable = itemView.findViewById(R.id.clickable);
    }
}
