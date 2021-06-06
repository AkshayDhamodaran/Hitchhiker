package com.akshay.otptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Screen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText fname, lname;
    TextView firstname, lastname, college_name, date_of_birth, dob, genderTv;
    Button ok_button, register;
    DatePicker datePicker;
    Spinner spinner;
    RadioGroup gender;
    String DoB, college, First_name, Last_name, Gender;
    RadioButton male, female, other;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register__screen);

        firstname = findViewById(R.id.textView3);
        lastname = findViewById(R.id.textView4);
        date_of_birth = findViewById(R.id.textView5);
        college_name = findViewById(R.id.textView6);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        dob = findViewById(R.id.DoB);
        ok_button = findViewById(R.id.setDate);
        register = findViewById(R.id.Register_button);
        datePicker = findViewById(R.id.datePicker);
        spinner = findViewById(R.id.spinner);
        gender = findViewById(R.id.gender);
        genderTv = findViewById(R.id.genderTV);
        male = findViewById(R.id.Male);
        female = findViewById(R.id.Female);
        other = findViewById(R.id.Other);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colleges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Male:
                        Gender = "Male";
                        break;
                    case R.id.Female:
                        Gender = "Female";
                        break;
                    case R.id.Other:
                        Gender = "Other";
                        break;
                }
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                ok_button.setVisibility(View.VISIBLE);
                genderTv.setVisibility(View.GONE);
                male.setVisibility(View.GONE);
                female.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
                register.setVisibility(View.GONE);
                fname.setVisibility(View.GONE);
                lname.setVisibility(View.GONE);
                firstname.setVisibility(View.GONE);
                lastname.setVisibility(View.GONE);
                college_name.setVisibility(View.GONE);
                date_of_birth.setVisibility(View.GONE);
                dob.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);

            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = (datePicker.getMonth() + 1);
                int year = datePicker.getYear();

                datePicker.setVisibility(View.GONE);
                ok_button.setVisibility(View.GONE);

                register.setVisibility(View.VISIBLE);
                genderTv.setVisibility(View.VISIBLE);
                male.setVisibility(View.VISIBLE);
                female.setVisibility(View.VISIBLE);
                other.setVisibility(View.VISIBLE);
                fname.setVisibility(View.VISIBLE);
                lname.setVisibility(View.VISIBLE);
                firstname.setVisibility(View.VISIBLE);
                lastname.setVisibility(View.VISIBLE);
                college_name.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                date_of_birth.setVisibility(View.VISIBLE);
                dob.setVisibility(View.VISIBLE);

                dob.setText(day + "/" + month + "/" + year);
                DoB = day + "/" + month + "/" + year;
                Toast.makeText(Register_Screen.this, DoB, Toast.LENGTH_SHORT).show();

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (college.equals("None")) {
                    Toast.makeText(Register_Screen.this, "Enter College", Toast.LENGTH_SHORT).show();
                } else {

                    First_name = fname.getText().toString();
                    Last_name = lname.getText().toString();

                    Intent intent = new Intent(Register_Screen.this, getPhoneNumber.class);
                    intent.putExtra("firstname", First_name);
                    intent.putExtra("lastname", Last_name);
                    intent.putExtra("dob", DoB);
                    intent.putExtra("college", college);
                    intent.putExtra("gender", Gender);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        college = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}