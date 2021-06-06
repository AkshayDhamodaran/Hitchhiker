package com.akshay.otptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.akshay.otptest.Common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VehicleInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText VehicleNumber;
    Button Submit;
    String vehicleType, vehicleNumber;
    String userPhoneNumber;
    String dbVehicleType;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);

        VehicleNumber = findViewById(R.id.vehicle_number);
        Submit = findViewById(R.id.submit);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF);
        userPhoneNumber = Common.CurrentUserModel.getPhoneNumber();
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.vehicle_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        setDetails();


        vehicleNumber = VehicleNumber.getText().toString().trim();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(userPhoneNumber).child("vehicleType").setValue(vehicleType);
                reference.child(userPhoneNumber).child("vehicleNumber").setValue(VehicleNumber.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(android.R.id.content), "Vehicle Info uploaded", Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(android.R.id.content), "Vehicle Info upload Failed", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.USER_REF).child(userPhoneNumber);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vehicleNumber = (String) snapshot.child("vehicleNumber").getValue();
                VehicleNumber.setText(vehicleNumber);
                dbVehicleType = (String) snapshot.child("vehicleType").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vehicleType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}