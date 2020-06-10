package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModifyCanteen extends AppCompatActivity {

    private EditText etCanteenName,etCanteenEmail,etCanteenPassword,etCanteenContact;
    private Button btnCanteenAdd;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_canteen);

        mAuth = FirebaseAuth.getInstance();
        etCanteenName = findViewById(R.id.etCanteenName);
        etCanteenEmail = findViewById(R.id.etCanteenEmail);
        etCanteenPassword = findViewById(R.id.etCanteenPassword);
        etCanteenContact = findViewById(R.id.etCanteenContact);
        btnCanteenAdd = findViewById(R.id.btnCanteenAdd);

        btnCanteenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add();
            }
        });
    }

    private void Add(){
        String name = etCanteenName.getText().toString().trim();
        String email = etCanteenEmail.getText().toString().trim().toLowerCase();
        String phone = etCanteenContact.getText().toString().trim();
        String password = etCanteenPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference table_user = database.getReference("Canteen");
                    // Create a new user with a first and last name
                    Canteen canteen = new Canteen("1",etCanteenEmail.getText().toString().trim(), etCanteenName.getText().toString().trim(), "0");
                    table_user.child(mAuth.getUid()).setValue(canteen);
                    Toast.makeText(ModifyCanteen.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Toast.makeText(ModifyCanteen.this,"Could not register! Try again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
