package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName,etEmail,etPassword,etPhoneNumber;
    private Button btnSignup;
    private TextView tvLogin,vNon_Student_SignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    public static final String USER_TYPE_KEY = "type";

    private void Register(){
        String email=etEmail.getText().toString().trim().toLowerCase();
        String password=etPassword.getText().toString().trim();
        String name=etName.getText().toString().trim();
        String phone=etPhoneNumber.getText().toString().trim();

        String t="201701448@daiict.ac.in";

        int i=0;
        while(i<email.length() && email.charAt(i)!='@')
            i++;
        if(i==email.length()){
            Toast.makeText(SignUpActivity.this,"Enter valid daiict email address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!email.substring(i).equals(t.substring(9))){
            Toast.makeText(SignUpActivity.this,"Only daiict email address is valid!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(SignUpActivity.this, "Email field can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(SignUpActivity.this,"Password field can't be empty!",Toast.LENGTH_SHORT).show();
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
                    final DatabaseReference table_user = database.getReference("Users").child("Student");
                    // Create a new user with a first and last name
                    User user = new User(etName.getText().toString().trim(), etPhoneNumber.getText().toString().trim(), "0");
                    table_user.child(etEmail.getText().toString().trim().substring(0,9)).setValue(user);
                    Toast.makeText(SignUpActivity.this,"Registered Sucessfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),Login_Activity.class));
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Could not register! Try again", Toast.LENGTH_SHORT).show();
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
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);
        vNon_Student_SignUp = findViewById(R.id.Non_Student_SignUp);

        databaseReference = FirebaseDatabase.getInstance().getReference();

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

        vNon_Student_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(SignUpActivity.this);
                View mview = getLayoutInflater().inflate(R.layout.student_profile_edit_text_layout,null);
                mbuilder.setTitle("Enter Authentication Key");

                final EditText vStudent_Profile_Edit_Text = mview.findViewById(R.id.Student_Profile_Edit_Text);

                mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = vStudent_Profile_Edit_Text.getText().toString().trim();
                        databaseReference.child("Admin_Key").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Intent intent = new Intent(SignUpActivity.this,SignUp_Non_Student.class);
                                    intent.putExtra(USER_TYPE_KEY,dataSnapshot.getValue().toString());
                                    finish();
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this,"Please Enter Valid Key",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mbuilder.setView(mview);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });
    }
}
