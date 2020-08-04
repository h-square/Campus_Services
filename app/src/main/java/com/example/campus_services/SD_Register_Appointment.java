package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SD_Register_Appointment extends AppCompatActivity {

    private Spinner vSlot_Spinner;
    private CircleImageView vSD_Doctor_Profile_Image;
    private TextView vSD_Doctor_Name,vAlready_registered_TV;
    private Button vSD_Register_Appointment_btn;
    private DatabaseReference databaseReference;

    private ArrayList<String> vSlotArrayList;
    private ArrayAdapter<String> vSlotArrayAdapter;

    private String studentId;
    private String SLOT1 = "Slot - 1";
    private String SLOT2 = "Slot - 2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_d__register__appointment);

        vSlot_Spinner = (Spinner) findViewById(R.id.Slot_Spinner);
        vSD_Doctor_Profile_Image = (CircleImageView) findViewById(R.id.SD_Doctor_Profile_Image);
        vSD_Doctor_Name = (TextView) findViewById(R.id.SD_Doctor_Name);
        vSD_Register_Appointment_btn = (Button) findViewById(R.id.SD_Register_Appointment_btn);
        vAlready_registered_TV = (TextView) findViewById(R.id.Already_registered_TV);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        studentId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        studentId = studentId.substring(0,9);
        vSlotArrayList = new ArrayList<>();

        vSlotArrayAdapter = new ArrayAdapter<>(SD_Register_Appointment.this,android.R.layout.simple_spinner_dropdown_item,vSlotArrayList);

        vSlot_Spinner.setAdapter(vSlotArrayAdapter);

        vSlotArrayList.add("none");
        vSlotArrayList.add("Slot-1 (12:30 to 13:30)");
        vSlotArrayList.add("Slot-2 (16:30 to 17:30)");
        vSlotArrayAdapter.notifyDataSetChanged();

        moveDataFromAppointmentToAppointmentHistory();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.sd_register_appointment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.sd_register_appointment:
                        return true;

                    case R.id.sd_appointments:
                        finish();
                        startActivity(new Intent(SD_Register_Appointment.this,SD_Appointments.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.sd_appointment_history:
                        finish();
                        startActivity(new Intent(SD_Register_Appointment.this,SD_Appointment_History.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        vSlot_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(position == 0){
                    showRegisterBtn();
                    vSD_Doctor_Profile_Image.setImageResource(R.drawable.default_profile_image);
                    vSD_Doctor_Name.setText("Doctor's Name");
                }else {
                    databaseReference.child("Users").child("Doctor").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String name = "#";
                                String url = "";
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    long slot = ((long) dataSnapshot1.child("slot").getValue());
                                    if ((int) slot == position) {
                                        name = dataSnapshot1.child("name").getValue().toString();
                                        url = dataSnapshot1.child("image_Url").getValue().toString();
                                    }
                                }
                                if (name.equals("#")) {
                                    showRegisterBtn();
                                    vSD_Doctor_Profile_Image.setImageResource(R.drawable.default_profile_image);
                                    vSD_Doctor_Name.setText("Doctor's Name");
                                    Toast.makeText(SD_Register_Appointment.this, "No Doctor is Available for this Slot", Toast.LENGTH_SHORT).show();
                                } else {
                                    vSD_Doctor_Name.setText(name);
                                    if (!url.equals("")) {
                                        Picasso.get().load(url).into(vSD_Doctor_Profile_Image);
                                    }
                                    final String slot = "Slot - " + String.valueOf(position);
                                    String tomorrow = CalendarUtils.getTomorrowsDate();
                                    long time = CalendarUtils.getCurrentTime();
                                    databaseReference.child("Appointments").child(slot).child(studentId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                hideRegisterBtn();
                                            }else{
                                                showRegisterBtn();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vSD_Register_Appointment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = vSlot_Spinner.getSelectedItemPosition();
                if(pos == 0 || vSD_Doctor_Name.getText().toString().equals("Doctor's Name")){
                    Toast.makeText(SD_Register_Appointment.this,"Please select valid option from List",Toast.LENGTH_SHORT).show();
                    return;
                }
                final String slot = "Slot - " + String.valueOf(pos);
                String date = CalendarUtils.getDate(pos);
                long time = CalendarUtils.getCurrentTime();
                Appointment ap = new Appointment(studentId,"Pending",date,time,pos);
                databaseReference.child("Appointments").child(slot).child(studentId).setValue(ap);
                Toast.makeText(SD_Register_Appointment.this,"Appointment Registered",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void moveDataFromAppointmentToAppointmentHistory() {
        databaseReference.child("Appointments").child(SLOT1).child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Appointment ap = dataSnapshot.getValue(Appointment.class);
                    if(CalendarUtils.isPastAppointment(ap.getDate(),ap.getSlot())){
                        String key = databaseReference.child("Appointment_History").child(studentId).push().getKey();
                        databaseReference.child("Appointment_History").child(studentId).child(key).setValue(ap);
                        databaseReference.child("Appointments").child(SLOT1).child(studentId).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Appointments").child(SLOT2).child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Appointment ap = dataSnapshot.getValue(Appointment.class);
                    String date = ap.getDate();
                    if(!(CalendarUtils.isTomorrow(date))){
                        String key = databaseReference.child("Appointment_History").child(studentId).push().getKey();
                        databaseReference.child("Appointment_History").child(studentId).child(key).setValue(ap);
                        databaseReference.child("Appointments").child(SLOT2).child(studentId).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showRegisterBtn(){
        vSD_Register_Appointment_btn.setVisibility(View.VISIBLE);
        vAlready_registered_TV.setVisibility(View.GONE);
    }

    public void hideRegisterBtn(){
        vSD_Register_Appointment_btn.setVisibility(View.GONE);
        vAlready_registered_TV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SD_Register_Appointment.this,HomeActivity.class);
        startActivity(intent);
    }
}
