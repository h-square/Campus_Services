package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.d("hi","1");
            Intent intent = new Intent(MainActivity.this,Login_Activity.class);
            finish();
            startActivity(intent);
        }
        else{
            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            final String student_id = email.substring(0,9);

            databaseReference.child("Users").child("Canteen").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        // forward to canteen's home activity
                        Intent intent = new Intent(MainActivity.this,CanteenManager.class);
                        finish();
                        startActivity(intent);
                    }
                    else{
                        databaseReference.child("Users").child("Doctor").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    // forward to doctor's home activity
                                }
                                else{
                                    databaseReference.child("Users").child("Supervisor").child(uid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                // forward to supervisor's home activity
                                            }
                                            else{
                                                databaseReference.child("Users").child("Student").child(student_id).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()){
                                                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                        else{
                                                            Log.d("hello! = ", "1" );
                                                            FirebaseAuth.getInstance().signOut();
                                                            Intent intent = new Intent(MainActivity.this,Login_Activity.class);
                                                            finish();
                                                            startActivity(intent);
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


        }

    }

}
