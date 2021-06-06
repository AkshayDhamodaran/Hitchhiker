package com.akshay.otptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.akshay.otptest.Adapters.NearUserAdapter;
import com.akshay.otptest.Common.Common;
import com.akshay.otptest.Common.LocationScanner;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class geofireTest extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser user;
    String userPhoneNumber;
    double lattitude,longitude;
    GeoFire geoFire;
    int radius = 3;
    String college;
    String currentCollege;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    NearUserAdapter adapter;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofire_test);
        recyclerView=findViewById(R.id.recycler_users);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userPhoneNumber = (String) user.getPhoneNumber();

//        textView = findViewById(R.id.usersList);
        lattitude = Common.CurrentUserModel.getLattitude();
        longitude = Common.CurrentUserModel.getLongitude();

        college = Common.CurrentUserModel.getCollege();


        reference = FirebaseDatabase.getInstance().getReference("geolocations");

        findDriver();


    }


    private void findDriver() {

        geoFire = new GeoFire(reference);
        List<String> keys=new ArrayList<>();
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(lattitude,longitude),radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!Common.CurrentUserModel.getPhoneNumber().equals(key)){
                    keys.add(key);
                }
            }
            @Override
            public void onKeyExited(String key) {

            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        loadKeyUsers(keys);
    }

    private void loadKeyUsers(List<String> keys) {
        List<LoginCheckerModel> loginCheckerModels=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.USER_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int i=1;
                            for (DataSnapshot Itemsnapshot : snapshot.getChildren()) {
                                LoginCheckerModel loginCheckerModel = Itemsnapshot.getValue(LoginCheckerModel.class);
                                if(keys.contains(Itemsnapshot.getKey())) {
                                    if(loginCheckerModel.getCollege().equals(Common.CurrentUserModel.getCollege())) {
                                        loginCheckerModels.add(loginCheckerModel);
                                    }
                                }
                                if(snapshot.getChildrenCount()==i){
                                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    adapter = new NearUserAdapter(loginCheckerModels, geofireTest.this);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                                i++;

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}