package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactFeedback extends AppCompatActivity {

    private String CanteenName, OrderString;
    private EditText message;
    private TextView contactInfo, canteenNameInfo;
    private Button submit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_feedback);

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        OrderString = intent.getStringExtra("OrderString");

        message = findViewById(R.id.etFeedback);
        contactInfo = findViewById(R.id.tvContactInfo);
        submit = findViewById(R.id.btnFeedbackSubmit);
        canteenNameInfo = findViewById(R.id.tvCanteenNameInfo);
        progressDialog = new ProgressDialog(this);

        canteenNameInfo.setText(CanteenName);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_canteenInfo = db.getReference("Users/Canteen");
        table_canteenInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String contactNumber="1";
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Canteen item = ds.getValue(Canteen.class);
                    if(item.getName().equals(CanteenName)){
                        contactNumber = "" + item.getContact();
                        break;
                    }
                }
                //String contactNumber = dataSnapshot.child("Canteen" + CanteenNumber).getValue(String.class);

                contactInfo.setText(contactNumber);
                table_canteenInfo.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Feedback", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    progressDialog.setMessage("Sending Feedback...");
                    progressDialog.show();
                    final FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                    final DatabaseReference table_feedbackNumber = db2.getReference("CanteenFeedbackCount");
                    table_feedbackNumber.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String feedbackNumber = dataSnapshot.getValue(String.class);
                            Feedback feedback = new Feedback(CanteenName, message.getText().toString());
                            final DatabaseReference table_feedback = db2.getReference("CanteenFeedback");
                            table_feedback.child(feedbackNumber).setValue(feedback);
                            table_feedbackNumber.removeEventListener(this);
                            final DatabaseReference table_feedbackNumber1 = db2.getReference("CanteenFeedbackCount");
                            table_feedbackNumber1.child("Count").setValue(Integer.toString(Integer.parseInt(feedbackNumber) + 1));
                            progressDialog.dismiss();
                            message.setText("");
                            Toast.makeText(getApplicationContext(), "Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent1 = new Intent(ContactFeedback.this, CanteenMenu.class);
        intent1.putExtra("OrderString", OrderString);
        intent1.putExtra("CanteenName",CanteenName);
        finish();
        startActivity(intent1);

    }
}
