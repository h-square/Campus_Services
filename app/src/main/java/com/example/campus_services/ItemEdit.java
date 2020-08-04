package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemEdit extends AppCompatActivity {

    private String CanteenName, available, CanteenAvailable;
    private String ItemString;
    private String name,price;
    private EditText dishPrice, etAddCookingInstruction;
    private TextView dishName,availability;
    private ListView listView;
    private Button submit, delete,ChangeAvailability, btnAddCookingInstruction;
    private ArrayList<String> instructions;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        ItemString = intent.getStringExtra("ItemString");
        available = intent.getStringExtra("Availability");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        instructions = intent.getStringArrayListExtra("CookingInstructions");

        int flag = 0;
        for(int i=0;i<ItemString.length();i++){
            if (flag==0 && ItemString.charAt(i)=='\n'){
                name = ItemString.substring(0,i);
                flag=1;
            }
            if(flag==1 && ItemString.charAt(i)>='0' && ItemString.charAt(i)<='9'){
                price = ItemString.substring(i,ItemString.length());
                break;
            }
        }

        dishName = findViewById(R.id.editItemName);
        dishPrice = findViewById(R.id.editItemPrice);
        availability = findViewById(R.id.ItemAvailability);
        submit = findViewById(R.id.editSubmitItem);
        delete = findViewById(R.id.editDeleteItem);
        ChangeAvailability = findViewById(R.id.ItemChangeAvailability);
        etAddCookingInstruction = findViewById(R.id.etAddCookingInstruction);
        btnAddCookingInstruction = findViewById(R.id.btnAddCookingInstruction);
        listView = findViewById(R.id.lvCookingInstructions);

        dishName.setText(name);
        dishPrice.setText(price);

        if (available.equals("1")){
            availability.setText("Available");
        }
        else {
            availability.setText("Unavailable");
        }

        ChangeAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (available.equals("1")){
                    available="0";
                    availability.setText("Unavailable");
                }
                else {
                    available="1";
                    availability.setText("Available");
                }
            }
        });

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dish_info,R.id.dishnameid,instructions);
        listView.setAdapter(arrayAdapter);

        btnAddCookingInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StInput= etAddCookingInstruction.getText().toString();
                if(StInput != null && StInput.length() > 0){
                    instructions.add(StInput);
                    arrayAdapter.notifyDataSetChanged();
                    etAddCookingInstruction.setText("");
                }else{
                    //EditText is blank
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dishName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT);
                    return;
                }
                if (dishPrice.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Enter Valid Price!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Item dish = new Item(dishName.getText().toString(),dishPrice.getText().toString(),available,instructions);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_items = database.getReference("CanteenMenu/"+CanteenName);
                table_items.child(dishName.getText().toString()).setValue(dish);

                Intent intent = new Intent(ItemEdit.this, CanteenManager.class);
                intent.putExtra("CanteenAvailable", CanteenAvailable);
                intent.putExtra("CanteenName",CanteenName);
                finish();
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference table_items = database.getReference("CanteenMenu/"+CanteenName);
                table_items.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.child(name).getRef().removeValue();
                        Intent intent = new Intent(ItemEdit.this, CanteenManager.class);
                        intent.putExtra("CanteenName",CanteenName);
                        intent.putExtra("CanteenAvailable", CanteenAvailable);
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this,CanteenManager.class);
        i.putExtra("CanteenName", CanteenName);
        i.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(i);

    }
}
