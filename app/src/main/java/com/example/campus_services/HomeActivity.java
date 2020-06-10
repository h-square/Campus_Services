package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private TextView tvDisplayName;
    private Button btnCanteenOrder,btnComplaints,btnAppointments;

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference table_user;
    private ValueEventListener listener1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getEmail();
        int i=0;
        while(userID.charAt(i) != '@')
            i++;
        userID = userID.substring(0,i);
        tvDisplayName = findViewById(R.id.tvDisplayName);

        btnCanteenOrder = findViewById(R.id.btnCanteenOrder);
        btnAppointments = findViewById(R.id.btnAppointments);
        btnComplaints = findViewById(R.id.btnComplaints);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_user = db.getReference("User");
        listener1 = table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(userID).getValue(User.class);
                tvDisplayName.setText("Welcome: " + user.getName().toUpperCase());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnCanteenOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CanteenOrderActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_page,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                table_user.removeEventListener(listener1);
                mAuth.signOut();
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
