package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SD_Appointment_History extends AppCompatActivity {

    private RecyclerView vAH_RecyclerView;
    private ArrayList<Appointment> vAH_ArrayList;
    private AppointmentListAdapter vAH_Adapter;

    private DatabaseReference databaseReference;
    private String student_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_d__appointment__history);

        vAH_RecyclerView = (RecyclerView) findViewById(R.id.AH_RecyclerView);
        vAH_ArrayList = new ArrayList<>();
        vAH_Adapter = new AppointmentListAdapter();
        vAH_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vAH_RecyclerView.setAdapter(vAH_Adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        student_Id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        student_Id = student_Id.substring(0,9);

        databaseReference.child("Appointment_History").child(student_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Appointment ap = dataSnapshot1.getValue(Appointment.class);
                        vAH_ArrayList.add(ap);
                    }
                    vAH_Adapter.setAppointmentData(vAH_ArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.sd_appointment_history);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.sd_register_appointment:
                        finish();
                        startActivity(new Intent(SD_Appointment_History.this,SD_Register_Appointment.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.sd_appointments:
                        finish();
                        startActivity(new Intent(SD_Appointment_History.this,SD_Appointments.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.sd_appointment_history:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appointment_history,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.clear_AH:{
                AlertDialog.Builder builder = new AlertDialog.Builder(SD_Appointment_History.this);
                builder.setTitle("Clear Appointments");
                builder.setMessage("All the history of appointments will be cleared.");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("Appointment_History").child(student_Id).removeValue();
                        Toast.makeText(SD_Appointment_History.this,"Appointments Cleared",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SD_Appointment_History.this,HomeActivity.class);
        startActivity(intent);
    }
}
