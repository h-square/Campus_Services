package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SD_Appointment_History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_d__appointment__history);


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
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SD_Appointment_History.this,HomeActivity.class);
        startActivity(intent);
    }
}
