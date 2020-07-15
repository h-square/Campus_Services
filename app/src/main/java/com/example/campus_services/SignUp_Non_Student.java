package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUp_Non_Student extends AppCompatActivity {

    private EditText vNS_Name,vNS_Email,vNS_PhoneNumber,vNS_Password;
    private Spinner vUser_Type_Spinner;
    private TextView vNS_Login;
    private Button vNS_Signup;

    private ArrayList<String> User_Type_List;
    private ArrayAdapter<String> User_Type_Adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__non__student);

        vNS_Name = (EditText) findViewById(R.id.NS_Name);
        vNS_Email = (EditText) findViewById(R.id.NS_Email);
        vNS_PhoneNumber = (EditText) findViewById(R.id.NS_PhoneNumber);
        vNS_Password = (EditText) findViewById(R.id.NS_Password);
        vNS_Login = (TextView) findViewById(R.id.NS_Login);
        vNS_Signup = (Button) findViewById(R.id.NS_Signup);
        vUser_Type_Spinner = (Spinner) findViewById(R.id.User_Type_Spinner);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        User_Type_List = new ArrayList<>();
        User_Type_Adapter = new ArrayAdapter<String>(SignUp_Non_Student.this,android.R.layout.simple_spinner_dropdown_item,User_Type_List);

        User_Type_Adapter.add("Canteen");
        User_Type_Adapter.add("Doctor");
        User_Type_Adapter.add("Supervisor");

        vUser_Type_Spinner.setAdapter(User_Type_Adapter);

        vNS_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = vNS_Name.getText().toString().trim();
                final String email = vNS_Email.getText().toString().trim();
                final String phoneNumber = vNS_PhoneNumber.getText().toString().trim();
                String password = vNS_Password.getText().toString().trim();
                final String Type = vUser_Type_Spinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(name)){
                    vNS_Name.setError("Name is required");
                    Toast.makeText(SignUp_Non_Student.this,"Name is required",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    vNS_Email.setError("Email is required");
                    Toast.makeText(SignUp_Non_Student.this,"Email is required",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    vNS_PhoneNumber.setError("Phone Number is required");
                    Toast.makeText(SignUp_Non_Student.this,"Phone Number is required",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    vNS_Password.setError("Password is required");
                    Toast.makeText(SignUp_Non_Student.this,"Password is required",Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(Type.equals("Canteen")){
                                Canteen canteen = new Canteen("1", email, name, phoneNumber,"0");
                                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference.child("Users").child("Canteen").child(Uid).setValue(canteen);
                            }
                            else if(Type.equals("Doctor")){

                            }
                            else{

                            }

                            Intent intent = new Intent(SignUp_Non_Student.this,Login_Activity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                });

            }
        });

        vNS_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp_Non_Student.this,Login_Activity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
