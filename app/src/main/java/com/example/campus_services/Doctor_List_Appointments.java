package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Doctor_List_Appointments extends AppCompatActivity {

    private TextView vCA_Amount_TV;
    private RecyclerView vCA_RecyclerView;

    private ArrayList<Appointment> vCA_List;
    private AppointmentListAdapter vCA_Adapter;
    private DatabaseReference databaseReference;
    private String SLOT = "Slot - ";
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__list__appointments);

        vCA_Amount_TV = (TextView) findViewById(R.id.CA_Amount_TV);
        vCA_RecyclerView = (RecyclerView) findViewById(R.id.CA_RecyclerView);
        vCA_List = new ArrayList<>();
        vCA_Adapter = new AppointmentListAdapter();
        vCA_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vCA_RecyclerView.setAdapter(vCA_Adapter);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child("Doctor").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("rrr", dataSnapshot.toString());
                    SLOT = SLOT + String.valueOf((long)(dataSnapshot.child("slot").getValue()));
                    databaseReference.child("Appointments").child(SLOT).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    Appointment ap = dataSnapshot1.getValue(Appointment.class);
                                    if(ap.getStatus().equals("Confirm")){
                                        vCA_List.add(ap);
                                    }
                                }
                                Collections.sort(vCA_List, new Comparator<Appointment>() {
                                    @Override
                                    public int compare(Appointment o1, Appointment o2) {
                                        return (int)(o1.getTime()-o2.getTime());
                                    }
                                });
                                vCA_Adapter.setAppointmentData(vCA_List);
                                vCA_Amount_TV.setText(String.valueOf(vCA_List.size()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.doctor_list_appointments);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.doctor_pending_appointments:
                        finish();
                        startActivity(new Intent(Doctor_List_Appointments.this,Doctor_Pending_Appointments.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.doctor_list_appointments:
                        return true;

                    case R.id.doctor_profile:
                        finish();
                        startActivity(new Intent(Doctor_List_Appointments.this,Doctor_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
