package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Doctor_Pending_Appointments extends AppCompatActivity {

    private RecyclerView vPending_Appointments_RecyclerView;
    private AppointmentListAdapter vPending_Appointments_Adapter;
    private TextView vPending_Appointments_Amount;

    private DatabaseReference databaseReference;
    private ArrayList<Appointment> vPending_Appointments_List;

    private String uid;
    private int slot = 0;
    private String SLOT = "Slot - ";
    public static int CONFIRM_APPOINTMENTS = 0;
    public static int MAX_CONFIRM_APPOINTMENTS = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__pending__appointments);

        vPending_Appointments_Amount = (TextView) findViewById(R.id.Pending_Appointments_Amount);

        vPending_Appointments_RecyclerView = (RecyclerView) findViewById(R.id.Pending_Appointments_RecyclerView);
        vPending_Appointments_List  = new ArrayList<>();
        vPending_Appointments_Adapter = new AppointmentListAdapter();
        vPending_Appointments_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vPending_Appointments_RecyclerView.setAdapter(vPending_Appointments_Adapter);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child("Doctor").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    slot = (int)(long)(dataSnapshot.child("slot").getValue());
                    SLOT = SLOT + String.valueOf(slot);
                    databaseReference.child("Appointments").child(SLOT).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                vPending_Appointments_List.clear();
                                int x = 0;
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    Appointment ap = dataSnapshot1.getValue(Appointment.class);
                                    x++;
                                    if(ap.getStatus().equals("Pending")) {
                                        vPending_Appointments_List.add(ap);
                                    }
                                }
                                CONFIRM_APPOINTMENTS = x - vPending_Appointments_List.size();
                                vPending_Appointments_Amount.setText(String.valueOf(vPending_Appointments_List.size()));
                                Collections.sort(vPending_Appointments_List, new Comparator<Appointment>() {
                                    @Override
                                    public int compare(Appointment o1, Appointment o2) {
                                        return (int)(o1.getTime() - o2.getTime());
                                    }
                                });
                                vPending_Appointments_Adapter.setAppointmentData(vPending_Appointments_List);
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

        bottomNavigationView.setSelectedItemId(R.id.doctor_pending_appointments);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.doctor_pending_appointments:
                        return true;

                    case R.id.doctor_list_appointments:
                        finish();
                        startActivity(new Intent(Doctor_Pending_Appointments.this,Doctor_List_Appointments.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.doctor_profile:
                        finish();
                        startActivity(new Intent(Doctor_Pending_Appointments.this,Doctor_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_pa_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.batch_confirm_pa:{
                AlertDialog.Builder dialog = new AlertDialog.Builder(Doctor_Pending_Appointments.this);
                View view = getLayoutInflater().inflate(R.layout.batch_confirm_pa_layout,null);
                dialog.setTitle("Batch Operation");

                TextView vCA_Amount = (TextView) view.findViewById(R.id.CA_Amount);
                final Spinner vCA_Spinner = (Spinner) view.findViewById(R.id.CA_Spinner);
                ArrayList<String> vCA_ArrayList;
                ArrayAdapter<String> vCA_Adapter;

                vCA_ArrayList = new ArrayList<>();
                vCA_Adapter = new ArrayAdapter<>(Doctor_Pending_Appointments.this,android.R.layout.simple_spinner_dropdown_item,vCA_ArrayList);
                vCA_Spinner.setAdapter(vCA_Adapter);

                vCA_ArrayList.add("Select Amount of Appointments");
                final int pool = MAX_CONFIRM_APPOINTMENTS - CONFIRM_APPOINTMENTS;
                if(pool >= 5){
                    vCA_ArrayList.add("<= 5 appointments");
                }
                if(pool >= 10){
                    vCA_ArrayList.add("<= 10 appointments");
                }
                if(pool >= 15){
                    vCA_ArrayList.add("<= 15 appointments");
                }
                if(pool >= 20){
                    vCA_ArrayList.add("<= 20 appointments");
                }
                if(pool >= 25){
                    vCA_ArrayList.add("<= 25 appointments");
                }
                vCA_Adapter.notifyDataSetChanged();
                vCA_Amount.setText(String.valueOf(CONFIRM_APPOINTMENTS));

                long arrivalTime = -1;
                if(isSlotOne()){
                    arrivalTime = 12*3600 + 25*60 + CONFIRM_APPOINTMENTS*3*60;
                }
                if(isSlotTwo()){
                    arrivalTime = 16*3600 + 25*60 + CONFIRM_APPOINTMENTS*3*60;
                }
                final long finalArrivalTime = arrivalTime;
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = vCA_Spinner.getSelectedItemPosition();
                        if(position == 0){
                            return;
                        }
                        int allowed_amount = position*5;
                        for(int i=0;i < vPending_Appointments_List.size() && i <= allowed_amount;i++){
                            String studentId = vPending_Appointments_List.get(i).getStudent_Id();
                            databaseReference.child("Appointments").child(SLOT).child(studentId).child("status").setValue("Confirm");
                            databaseReference.child("Appointments").child(SLOT).child(studentId).child("arrival_Time").setValue(finalArrivalTime + i*180);
                        }
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setView(view);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                break;
            }
            case R.id.clear_all_pa:{
                AlertDialog.Builder dialog = new AlertDialog.Builder(Doctor_Pending_Appointments.this);
                dialog.setTitle("Cancel Appointments");
                dialog.setMessage("All the remaining pending appointments for the selected slot will be canceled.");

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("Appointments").child(SLOT).removeValue();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isSlotOne(){ return SLOT.equals("Slot - 1"); }

    private boolean isSlotTwo(){ return SLOT.equals("Slot - 2"); }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
