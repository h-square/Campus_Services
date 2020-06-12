package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Login_Activity extends AppCompatActivity {

    private EditText vEmail,vPassword;
    private Button vbtnLogin;
    private TextView vSignup,vForgotPassword;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        vEmail = (EditText) findViewById(R.id.Email);
        vPassword = (EditText) findViewById(R.id.Password);
        vbtnLogin = (Button) findViewById(R.id.btnLogin);
        vSignup = (TextView) findViewById(R.id.Signup);
        vForgotPassword = (TextView) findViewById(R.id.ForgotPassword);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        vbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = vEmail.getText().toString().trim();
                String password = vPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    vEmail.setError("Please Enter Email");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    vPassword.setError("Password is required");
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            final String student_id = email.substring(0,9);

                            databaseReference.child("Users").child("Canteen").child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        // forward to canteen's home activity
                                        Intent intent = new Intent(Login_Activity.this,CanteenManager.class);
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
                                                                            Intent intent = new Intent(Login_Activity.this,HomeActivity.class);
                                                                            finish();
                                                                            startActivity(intent);
                                                                        }
                                                                        else{
                                                                            Log.d("hello! = ", "1" );
                                                                            FirebaseAuth.getInstance().signOut();
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
                        else{
                            Toast.makeText(Login_Activity.this,"Please SignUp first",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        vSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this,SignUpActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
