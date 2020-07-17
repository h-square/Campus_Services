package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenFeedback extends AppCompatActivity {

    private String CanteenName, OperationType,CanteenAvailable;
    private ListView listView;
    private ArrayList<String> mItemName;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_feedback);

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        listView = (ListView) findViewById(R.id.lvCanteenFeedback);

        mItemName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.dish_info,R.id.dishnameid,mItemName);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_Feedback = db.getReference("CanteenFeedback");
        final Query query = table_Feedback.orderByChild("canteenName").equalTo(CanteenName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Feedback feedback = ds.getValue(Feedback.class);
                    mItemName.add(feedback.getMessage());
                }
                listView.setAdapter(arrayAdapter);
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CanteenFeedback.this,CanteenManager.class);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(intent);

    }
}
