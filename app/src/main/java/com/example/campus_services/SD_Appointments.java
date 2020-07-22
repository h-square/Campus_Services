package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SD_Appointments extends AppCompatActivity {

    private LinearLayout vSlot_One_Appointment,vAT_LinearLayout_1;
    private CircleImageView vProfile_Image_1;
    private TextView vUser_Name_1,vRegistration_Date_1,vRegistration_Time_1,vAppointment_Status_1,vAppointment_Time_1;
    private Button vCancel_Appointment_btn_1;

    private LinearLayout vSlot_Two_Appointment,vAT_LinearLayout_2;
    private CircleImageView vProfile_Image_2;
    private TextView vUser_Name_2,vRegistration_Date_2,vRegistration_Time_2,vAppointment_Status_2,vAppointment_Time_2;
    private Button vCancel_Appointment_btn_2;

    private String studentId;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_d__appointments);

        vSlot_One_Appointment = (LinearLayout) findViewById(R.id.Slot_One_Appointment);
        vAT_LinearLayout_1 = (LinearLayout) findViewById(R.id.AT_LinearLayout_1);
        vProfile_Image_1 = (CircleImageView) findViewById(R.id.Profile_Image_1);
        vUser_Name_1 = (TextView) findViewById(R.id.User_Name_1);
        vRegistration_Date_1 = (TextView )findViewById(R.id.Registration_Date_1);
        vRegistration_Time_1 = (TextView) findViewById(R.id.Registration_Time_1);
        vAppointment_Status_1 = (TextView) findViewById(R.id.Appointment_Status_1);
        vAppointment_Time_1 = (TextView) findViewById(R.id.Appointment_Time_1);
        vCancel_Appointment_btn_1 = (Button) findViewById(R.id.Cancel_Appointment_btn_1);

        vSlot_Two_Appointment = (LinearLayout) findViewById(R.id.Slot_Two_Appointment);
        vAT_LinearLayout_2 = (LinearLayout) findViewById(R.id.AT_LinearLayout_2);
        vProfile_Image_2 = (CircleImageView) findViewById(R.id.Profile_Image_2);
        vUser_Name_2 = (TextView) findViewById(R.id.User_Name_2);
        vRegistration_Date_2 = (TextView )findViewById(R.id.Registration_Date_2);
        vRegistration_Time_2 = (TextView) findViewById(R.id.Registration_Time_2);
        vAppointment_Status_2 = (TextView) findViewById(R.id.Appointment_Status_2);
        vAppointment_Time_2 = (TextView) findViewById(R.id.Appointment_Time_2);
        vCancel_Appointment_btn_2 = (Button) findViewById(R.id.Cancel_Appointment_btn_2);

        studentId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        studentId = studentId.substring(0,9);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").child("Student").child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    vUser_Name_1.setText(dataSnapshot.child("name").getValue().toString());
                    vUser_Name_2.setText(dataSnapshot.child("name").getValue().toString());
                    String url = dataSnapshot.child("image_Url").getValue().toString();
                    if(!url.equals("")){
                        Picasso.get().load(url).into(vProfile_Image_1);
                        Picasso.get().load(url).into(vProfile_Image_2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Appointments").child("Slot - 1").child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    showSlotOne();
                    Appointment ap =  (dataSnapshot.getValue(Appointment.class));
                    vAppointment_Status_1.setText(ap.getStatus());
                    vRegistration_Time_1.setText(CalendarUtils.convertTimeFromSectoHHMM(ap.getTime()));
                    vRegistration_Date_1.setText(ap.getDate());
                    if(ap.getArrival_Time() == -1){
                        hideArrivalTimeSlotOne();
                    }else{
                        showArrivalTimeSlotOne();
                        vAppointment_Time_1.setText(CalendarUtils.convertTimeFromSectoHHMM(ap.getArrival_Time()));
                    }
                }else{
                    hideSlotOne();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Appointments").child("Slot - 2").child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    showSlotTwo();
                    Appointment ap =  (dataSnapshot.getValue(Appointment.class));
                    vAppointment_Status_2.setText(ap.getStatus());
                    vRegistration_Time_2.setText(CalendarUtils.convertTimeFromSectoHHMM(ap.getTime()));
                    vRegistration_Date_2.setText(ap.getDate());
                    if(ap.getArrival_Time() == -1){
                        hideArrivalTimeSlotTwo();
                    }else{
                        showArrivalTimeSlotTwo();
                        vAppointment_Time_2.setText(CalendarUtils.convertTimeFromSectoHHMM(ap.getArrival_Time()));
                    }
                }else{
                    hideSlotTwo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vCancel_Appointment_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SD_Appointments.this);
                builder.setTitle("Cancel Appointment");
                builder.setMessage("To cancel Appointment click on Ok");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.child("Appointments").child("Slot - 1").child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    final long tm = (long)(dataSnapshot.child("arrival_Time").getValue());
                                    if(tm == -1) {
                                        databaseReference.child("Appointments").child("Slot - 1").child(studentId).removeValue();
                                        return;
                                    }
                                    databaseReference.child("Appointments").child("Slot - 1").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    long arrtime = (long)(dataSnapshot1.child("arrival_Time").getValue());
                                                    String id = dataSnapshot1.child("student_Id").getValue().toString();
                                                    if(arrtime > tm){
                                                        databaseReference.child("Appointments").child("Slot - 1").child(id).child("arrival_Time").setValue(arrtime-180);
                                                    }
                                                }
                                            }
                                            databaseReference.child("Appointments").child("Slot - 1").child(studentId).removeValue();
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

                        Toast.makeText(SD_Appointments.this,"Appointment Canceled",Toast.LENGTH_LONG).show();
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
            }
        });

        vCancel_Appointment_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SD_Appointments.this);
                builder.setTitle("Cancel Appointment");
                builder.setMessage("To cancel Appointment click on Ok");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("Appointments").child("Slot - 2").child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    final long tm = (long)(dataSnapshot.child("arrival_Time").getValue());
                                    if(tm == -1) {
                                        databaseReference.child("Appointments").child("Slot - 2").child(studentId).removeValue();
                                        return;
                                    }
                                    databaseReference.child("Appointments").child("Slot - 2").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                    long arrtime = (long)(dataSnapshot1.child("arrival_Time").getValue());
                                                    String id = dataSnapshot1.child("student_Id").getValue().toString();
                                                    if(arrtime > tm){
                                                        databaseReference.child("Appointments").child("Slot - 2").child(id).child("arrival_Time").setValue(arrtime-180);
                                                    }
                                                }
                                            }
                                            databaseReference.child("Appointments").child("Slot - 2").child(studentId).removeValue();
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

                        Toast.makeText(SD_Appointments.this,"Appointment Canceled",Toast.LENGTH_LONG).show();
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
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.sd_appointments);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.sd_register_appointment:
                        finish();
                        startActivity(new Intent(SD_Appointments.this,SD_Register_Appointment.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.sd_appointments:
                        return  true;

                    case R.id.sd_appointment_history:
                        finish();
                        startActivity(new Intent(SD_Appointments.this,SD_Appointment_History.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void showArrivalTimeSlotOne() { vAT_LinearLayout_1.setVisibility(View.VISIBLE); }

    private void hideArrivalTimeSlotOne() { vAT_LinearLayout_1.setVisibility(View.GONE); }

    private void showArrivalTimeSlotTwo() { vAT_LinearLayout_2.setVisibility(View.VISIBLE); }

    private void hideArrivalTimeSlotTwo() { vAT_LinearLayout_2.setVisibility(View.GONE); }

    private void showSlotTwo() { vSlot_Two_Appointment.setVisibility(View.VISIBLE);}

    private void hideSlotTwo() { vSlot_Two_Appointment.setVisibility(View.GONE); }

    private void showSlotOne(){ vSlot_One_Appointment.setVisibility(View.VISIBLE); }

    private void hideSlotOne(){ vSlot_One_Appointment.setVisibility(View.GONE); }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SD_Appointments.this,HomeActivity.class);
        startActivity(intent);
    }
}
