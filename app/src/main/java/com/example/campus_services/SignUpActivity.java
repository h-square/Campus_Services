package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName,etEmail,etPassword,etPhoneNumber;
    private Button btnSignup;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    private void Register(){
        String email=etEmail.getText().toString().trim().toLowerCase();
        String password=etPassword.getText().toString().trim();
        String name=etName.getText().toString().trim();
        String phone=etPhoneNumber.getText().toString().trim();

        String t="201701448@daiict.ac.in";

        if(email.length()!=t.length()){
            Toast.makeText(SignUpActivity.this,"Enter valid daiict email address!",Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=0;i<9;i++){
            if(email.charAt(i)<'0' || email.charAt(i)>'9'){
                Toast.makeText(SignUpActivity.this,"Enter valid student id first",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(!email.substring(9).equals(t.substring(9))){
            Toast.makeText(SignUpActivity.this,"Only daiict email address is valid!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(SignUpActivity.this, "Email field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(SignUpActivity.this,"Password field can't be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering " + name);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference table_user = database.getReference("User");

                    User user = new User(etName.getText().toString().trim(),etPhoneNumber.getText().toString().trim() , "0");
                    table_user.child(etEmail.getText().toString().trim().substring(0,9)).setValue(user);
                    Toast.makeText(SignUpActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Could not register! Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        progressDialog = new ProgressDialog(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            }
        });
    }
}
