package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenOrderStatus extends AppCompatActivity {

    private TextView PendingOrderNumber, StatusType;
    private ListView listView;
    private ArrayList<String> mItemName, OrderDetails,saveOrderNumber, CookingInstruction, paymentMethod, OrderNo, Customers;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<ArrayList<String>> instr;
    private String CanteenName, OperationType,table_name,CanteenAvailable;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_order_status);

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        OperationType = intent.getStringExtra("OperationType");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");

        PendingOrderNumber = (TextView) findViewById(R.id.tvPendingOrderCanteen);
        StatusType = (TextView) findViewById(R.id.tvCanteenOrderStatusType);
        listView = (ListView) findViewById(R.id.lvPendingOrderCanteen);
        mItemName= new ArrayList<>();
        OrderNo = new ArrayList<>();
        Customers = new ArrayList<>();
        CookingInstruction = new ArrayList<>();
        saveOrderNumber = new ArrayList<>();
        OrderDetails= new ArrayList<>();
        paymentMethod = new ArrayList<>();
        instr = new ArrayList<ArrayList<String>>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.dish_info,R.id.dishnameid,mItemName);
        PendingOrderNumber.setText(Integer.toString(count));

//        if (OperationType.equals("OrderHistory")){
//
//        }
//        else {
//            StatusType.setText("Pending Orders: ");
//            table_name = "CurrentOrders";
//        }

        StatusType.setText("Past Orders: ");
        table_name = "DeliveredOrders";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_curr_orders = database.getReference(table_name);
        final Query query = table_curr_orders.orderByChild("canteenName").equalTo(CanteenName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    mItemName.add("Order No: " + order.getOrderNo() + "\n" + order.getCustomerId());
                    OrderNo.add(order.getOrderNo());
                    Customers.add(order.getCustomerId());
                    OrderDetails.add(order.getOrderDetails());
                    saveOrderNumber.add(order.getOrderNo());
                    CookingInstruction.add(order.getOrderDetails());
                    paymentMethod.add(order.getPaymentMethod());
                    instr.add(order.getCookingInstruction());
                    count+=1;
                    PendingOrderNumber.setText(Integer.toString(count));
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
                Intent intent = new Intent(CanteenOrderStatus.this,CanteenOrderDeliver.class);
                intent.putExtra("CanteenName",CanteenName);
                intent.putExtra("OrderBasic", mItemName.get(position));
                intent.putExtra("OrderNo",OrderNo.get(position));
                intent.putExtra("CustomerID",Customers.get(position));
                intent.putExtra("OrderDetails",OrderDetails.get(position));
                intent.putExtra("OrderNumber",saveOrderNumber.get(position));
                intent.putExtra("OperationType",OperationType);
                intent.putExtra("CookingInstruction",CookingInstruction.get(position));
                intent.putExtra("CanteenAvailable", CanteenAvailable);
                intent.putExtra("PaymentMethod",paymentMethod.get(position));
                intent.putExtra("CookingInstructions",instr.get(position));
                CanteenOrderStatus.this.finish();
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CanteenOrderStatus.this,CanteenManager.class);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        CanteenOrderStatus.this.finish();
        startActivity(intent);

    }
}
