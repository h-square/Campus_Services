package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CanteenManager extends AppCompatActivity {

    private TextView tvDisplayCanteenName, tvCanteenAvailability, tvCanteenBalance;
    private ListView lvCanteenItems;
    private Button btnAddItem,btnChangeAvailability;
    private String CanteenName, CanteenAvailability;
    private String UID;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, table_user;
    private ValueEventListener listener;
    private ArrayList<String> mItem, availability, mItemName;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_manager);

        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getUid();

        Intent data = getIntent();
        CanteenName = data.getStringExtra("CanteenName");
        CanteenAvailability = data.getStringExtra("CanteenAvailable");

        tvDisplayCanteenName = findViewById(R.id.tvDisplayCanteenName);
        tvCanteenAvailability = findViewById(R.id.tvCanteenAvailability);
        tvCanteenBalance = findViewById(R.id.tvCanteenBalance);
        lvCanteenItems = findViewById(R.id.lvCanteenItems);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnChangeAvailability = findViewById(R.id.btnChangeAvailability);

        tvDisplayCanteenName.setText(CanteenName);
        tvCanteenAvailability.setText(CanteenAvailability);


        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_user = db.getReference("Users/Canteen");
        listener = table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Canteen canteen = dataSnapshot.child(UID).getValue(Canteen.class);
                tvCanteenBalance.setText("₹ " + canteen.getVirtual_Money());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("CanteenMenu/" + CanteenName);
        mItem = new ArrayList<>();
        mItemName = new ArrayList<>();
        availability = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.dish_info,R.id.dishnameid,mItem);
        lvCanteenItems.setAdapter(arrayAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Item item = ds.getValue(Item.class);
                    mItemName.add(item.getName() + "\nPrice: " + item.getPrice());
                    if(item.getAvailability().equals("1")){
                        mItem.add(item.getName() + "\nPrice: " + item.getPrice() + "\nAvailable");
                    }
                    else{
                        mItem.add(item.getName() + "\nPrice: " + item.getPrice() + "\nUnavailable");
                    }
                    availability.add(item.getAvailability());
                }
                lvCanteenItems.setAdapter(arrayAdapter);
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvCanteenItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                table_user.removeEventListener(listener);
                Intent intent1 = new Intent(getApplicationContext(), ItemEdit.class);
                intent1.putExtra("ItemString", mItemName.get(position));
                intent1.putExtra("CanteenName",CanteenName);
                intent1.putExtra("Availability",availability.get(position));
                intent1.putExtra("CanteenAvailable", CanteenAvailability);
                CanteenManager.this.finish();
                startActivity(intent1);
            }
        });

        btnChangeAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CanteenAvailability.equals("1")){
                    CanteenAvailability = "0";
                }
                else {
                    CanteenAvailability = "1";
                }
                final FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference table_canteen = db.getReference().child("Users").child("Canteen");
                table_canteen.child(UID).child("available").setValue(CanteenAvailability);

            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_user.removeEventListener(listener);
                Intent intent = new Intent(getApplicationContext(), AddItem.class);
                intent.putExtra("CanteenAvailable", CanteenAvailability);
                intent.putExtra("CanteenName",CanteenName);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_canteen_manager,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogoutCanteen:
                table_user.removeEventListener(listener);
                mAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.menuCanteenOrderHistory:
                table_user.removeEventListener(listener);
                /*Intent intent2 = new Intent(this,CanteenOrderStatus.class);
                intent2.putExtra("OperationType","OrderHistory");
                intent2.putExtra("CanteenName",CanteenName);
                intent2.putExtra("CanteenAvailable", CanteenAvailability);
                startActivity(intent2);
                finish();*/
                return true;
            case R.id.menuCanteenStatus:
                /*Intent intent3 = new Intent(this,CanteenOrderStatus.class);
                intent3.putExtra("OperationType","OrderHistory");
                intent3.putExtra("CanteenName",CanteenName);
                intent3.putExtra("CanteenAvailable", CanteenAvailability);
                startActivity(intent3);
                finish();*/
                return true;
            case R.id.menuContactFeedbackCanteen:
                /*Intent intent1 = new Intent(this,Canteenfeedback.class);
                intent1.putExtra("CanteenName",CanteenName);
                intent1.putExtra("CanteenAvailable", CanteenAvailability);
                startActivity(intent1);
                finish();*/
                return true;
            case R.id.menuScanQR:
                /*Intent intent = new Intent(this,ReadQR.class);
                intent.putExtra("CanteenName",CanteenName);
                intent.putExtra("OperationType","OrderStatus");
                intent.putExtra("CanteenAvailable", CanteenAvailability);
                startActivity(intent);
                finish();*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
