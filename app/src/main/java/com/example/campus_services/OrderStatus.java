package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderStatus extends AppCompatActivity {

    private TextView PendingOrderNumber, OrderStatusType;
    private ListView listView;
    private ArrayList<String> mItemName, OrderDetails, CookingInstruction, paymentMethod;
    private ArrayAdapter<String> arrayAdapter;
    private String customerId;
    private int count=0;
    private String StatusType,table_name;
    private ArrayList<ArrayList<String>> instr;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        Intent intent = getIntent();
        StatusType = intent.getStringExtra("OperationType");
        PendingOrderNumber = findViewById(R.id.tvPendingOrderNumber);
        listView = findViewById(R.id.lvPendingOrder);
        OrderStatusType = findViewById(R.id.OrderStatusType);

        mItemName = new ArrayList<>();
        paymentMethod = new ArrayList<>();
        OrderDetails= new ArrayList<>();
        CookingInstruction = new ArrayList<>();
        instr = new ArrayList<ArrayList<String>>();

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.dish_info,R.id.dishnameid,mItemName);
        if (StatusType.equals("OrderHistory")){
            OrderStatusType.setText("Past Orders: ");
            table_name = "DeliveredOrders";
        }
        else {
            OrderStatusType.setText("Pending Orders: ");
            table_name = "CurrentOrders";
        }

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        for (int i=0; i<email.length();i++){
            if (email.charAt(i)=='@'){
                customerId = email.substring(0,i);
                break;
            }
        }

        final FirebaseDatabase db =FirebaseDatabase.getInstance();
        final DatabaseReference table_curr_orders = db.getReference(table_name);
        final Query query = table_curr_orders.orderByChild("customerId").equalTo(email);
        PendingOrderNumber.setText(Integer.toString(count));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    if(StatusType.equals("OrderHistory")){
                        mItemName.add("Order No: " + order.getOrderNo() + "\nCanteen: " + order.getCanteenName());
                    }
                    else {
                        mItemName.add("Order No: " + order.getOrderNo() + "\nCanteen: " + order.getCanteenName() + "\nStatus: " + order.getStatus());
                    }
                    OrderDetails.add(order.getOrderDetails());
                    CookingInstruction.add(order.getOrderDetails());
                    paymentMethod.add(order.getPaymentMethod());
                    count+=1;
                    PendingOrderNumber.setText(Integer.toString(count));
                    instr.add(order.getCookingInstruction());
                }
                listView.setAdapter(arrayAdapter);
                query.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderStatus.this,OrderSummary.class);
                intent.putExtra("OrderBasic", mItemName.get(position));
                intent.putExtra("OrderDetails",OrderDetails.get(position));
                intent.putExtra("OperationType", StatusType);
                intent.putExtra("CookingInstruction",CookingInstruction.get(position));
                intent.putExtra("PaymentMethod", paymentMethod.get(position));
                intent.putExtra("CookingInstructions", instr.get(position));
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(OrderStatus.this,CanteenOrderActivity.class));

    }
}
