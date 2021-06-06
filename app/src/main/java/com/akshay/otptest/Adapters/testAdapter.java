package com.akshay.otptest.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.otptest.LoginCheckerModel;
import com.akshay.otptest.R;

import java.util.List;

public class testAdapter extends RecyclerView.Adapter<testAdapter.ViewHolderTest> {

    private List<LoginCheckerModel> users;

    public testAdapter(List<LoginCheckerModel> users){
        this.users = users;
    }
    @NonNull
    @Override
    public testAdapter.ViewHolderTest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_recycler,parent,false);
        return new ViewHolderTest(view);
    }

    @Override
    public void onBindViewHolder(@NonNull testAdapter.ViewHolderTest holder, int position) {
        String resource = users.get(position).getFirstname();


        holder.setData(resource);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolderTest extends RecyclerView.ViewHolder {

        public ViewHolderTest(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(String resource) {

        }
    }
}
