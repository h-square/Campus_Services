package com.example.campus_services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Thanks extends AppCompatActivity {

    private String OrderNo;
    private TextView OrderNumber;
    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        Intent intent = getIntent();
        OrderNo = intent.getStringExtra("OrderNo");
        //final FirebaseDatabase db = FirebaseDatabase.getInstance();
        //final DatabaseReference database = db.getReference("OrderNumber");
        //database.child("OrdNo").setValue(Integer.toString(Integer.parseInt(OrderNo)+1));
        OrderNumber = (TextView) findViewById(R.id.ViewOrderNumber);
        goBack = (Button) findViewById(R.id.thanksGoBack);
        OrderNumber.setText("OrderNo: "+OrderNo);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Thanks.this,CanteenOrderActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(Thanks.this,CanteenOrderActivity.class));

    }
}
