package com.akshay.otptest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class reqViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView imageView;
    ImageButton call;
    RelativeLayout relativeLayout;
    public reqViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.ReqName);
        imageView = itemView.findViewById(R.id.imageViewReqItem);
        call = itemView.findViewById(R.id.call_button);
        relativeLayout = itemView.findViewById(R.id.reqItem);
    }
}
