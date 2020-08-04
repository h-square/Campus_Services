package com.example.campus_services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity {

    private String CanteenName, CanteenAvailability;
    private Item dish;
    private EditText DName;
    private EditText DPrice;
    private Button Submit;
    private ArrayList<String> Instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenAvailability = intent.getStringExtra("CanteenAvailable");

        DName = findViewById(R.id.etItemName);
        DPrice = findViewById(R.id.etItemPrice);
        Submit = findViewById(R.id.btnSubmitItem);

        Instructions = new ArrayList<>();
        Instructions.add("None");

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT);
                    return;
                }
                if (DPrice.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Price",Toast.LENGTH_SHORT);
                    return;
                }
                dish = new Item(DName.getText().toString(),DPrice.getText().toString(),"1",Instructions);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_items = database.getReference("CanteenMenu/" + CanteenName);
                table_items.child(DName.getText().toString()).setValue(dish);
                Intent intent = new Intent(AddItem.this, CanteenManager.class);
                intent.putExtra("CanteenName",CanteenName);
                intent.putExtra("CanteenAvailable", CanteenAvailability);
                finish();
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, CanteenManager.class);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenAvailable", CanteenAvailability);
        finish();
        startActivity(intent);

    }
}
