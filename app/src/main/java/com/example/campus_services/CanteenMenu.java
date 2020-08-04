package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenMenu extends AppCompatActivity {

    private TextView tvDisplayCanteenName;
    private Button btnPlaceOrder;
    private ListView lvCanteenMenu;
    private String CanteenName, OrderString, InstructionString;

    private DatabaseReference mDatabase;
    private ArrayList<String> mItemName,mItem, availability,instr;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<ArrayList<String>> instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_menu);

        tvDisplayCanteenName = findViewById(R.id.tvDisplayCanteenName);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        lvCanteenMenu = findViewById(R.id.lvCanteenMenu);

        final Intent intent = getIntent();
        tvDisplayCanteenName.setText(intent.getStringExtra("CanteenName"));
        CanteenName = intent.getStringExtra("CanteenName");
        OrderString = intent.getStringExtra("OrderString");
        InstructionString = intent.getStringExtra("InstructionString");
        instr = intent.getStringArrayListExtra("CookI");

        mDatabase = FirebaseDatabase.getInstance().getReference("CanteenMenu/"+CanteenName);
        mItemName = new ArrayList<>();
        mItem = new ArrayList<>();
        availability = new ArrayList<>();
        instructions = new ArrayList<ArrayList<String>>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.dish_info,R.id.dishnameid,mItem);
        lvCanteenMenu.setAdapter(arrayAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Item item = ds.getValue(Item.class);
                    mItemName.add(item.getName()+ "\nPrice: " + item.getPrice());
                    availability.add(item.getAvailability());
                    if (item.getAvailability().equals("1")){
                        mItem.add(item.getName()+ "\nPrice: " + item.getPrice() + "\nAvailable");
                    }
                    else {
                        mItem.add(item.getName()+ "\nPrice: " + item.getPrice() + "\nUnavailable");
                    }
                    instructions.add(item.getInstructions());
                }
                lvCanteenMenu.setAdapter(arrayAdapter);
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvCanteenMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (availability.get(position).equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Item is unavailable currently", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1 = new Intent(getApplicationContext(), ItemQuantity.class);
                intent1.putExtra("CurrentActivity","CanteenMenu");
                intent1.putExtra("CurrentDish", mItemName.get(position));
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("CanteenName",CanteenName);
                intent1.putExtra("CookingInstructions",instructions.get(position));
                intent1.putExtra("InstructionString",InstructionString);
                intent1.putExtra("CookI",instr);
                finish();
                startActivity(intent1);
            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderString.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please add Items before Proceeding",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1 = new Intent(getApplicationContext(), PlaceOrder.class);
                intent1.putExtra("CanteenName",CanteenName);
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("InstructionString",InstructionString);
                intent1.putExtra("CookI",instr);
                finish();
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, CanteenOrderActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_canteen_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ContactFeedback:
                Intent intent1 = new Intent(getApplicationContext(), ContactFeedback.class);
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("CanteenName",CanteenName);
                intent1.putExtra("InstructionString",InstructionString);
                intent1.putExtra("CookI",instr);
                finish();
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
