package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenOrderDeliver extends AppCompatActivity {

    private TextView OrderDetailsNo, OrderDetailsName, TotalAmount, displayPaymentMethod;
    private Button deliverOrder;
    private ListView listView;
    private ArrayList<String> mItemName,instr;
    private ArrayAdapter<String> arrayAdapter;
    private String orderBasics, orderNo, orderName, orderDetails, CanteenName, OrderNumber,OperationType,CanteenAvailable, CookingInstruction, PaymentMethod, OrderStatus;
    private Order order;
    int flag=0, amount=0;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_order_deliver);

        final Intent intent = getIntent();
        orderBasics = intent.getStringExtra("OrderBasic");
        orderNo = intent.getStringExtra("OrderNo");
        orderName = intent.getStringExtra("CustomerID");
        orderDetails = intent.getStringExtra("OrderDetails");
        CanteenName = intent.getStringExtra("CanteenName");
        OrderNumber = intent.getStringExtra("OrderNumber");
        OperationType=intent.getStringExtra("OperationType");
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");
        CookingInstruction = intent.getStringExtra("CookingInstruction");
        PaymentMethod = intent.getStringExtra("PaymentMethod");
        instr = intent.getStringArrayListExtra("CookingInstructions");

        OrderDetailsNo = (TextView) findViewById(R.id.tvOrderDeliverNo);
        OrderDetailsName = findViewById(R.id.tvOrderDeliverID);
        TotalAmount = (TextView)findViewById(R.id.OrderDeliverDetailsTotalAmount);
        //displayCookingInstruction = (TextView) findViewById(R.id.OrderDeliveryCookingInstruction);
        displayPaymentMethod = (TextView) findViewById(R.id.canteenDisplayPaymentMethod);
        deliverOrder = (Button) findViewById(R.id.DeliverOrder);
        listView = (ListView) findViewById(R.id.lvOrderDeliver);
        //displayCookingInstruction.setText(CookingInstruction);
        mItemName= new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.dish_info,R.id.dishnameid,instr);

        if (OperationType.equals("OrderStatus")){
            if (PaymentMethod.equals("1")){
                displayPaymentMethod.setText("PAID");
            }
            else {
                displayPaymentMethod.setText("NOT PAID");
            }
        }
        else {
            if (PaymentMethod.equals("1")){
                displayPaymentMethod.setText("ONLINE");
            }
            else {
                displayPaymentMethod.setText("CASH");
            }
        }

        if(orderName.charAt(0)>='0' && orderName.charAt(0)<='9') {
            type = 1;
        }
        else{
            type = 2;
        }

        int c=0,pre=0;
        String temp="";
        OrderDetailsNo.setText(orderNo);
        OrderDetailsName.setText(orderName);
        if (OperationType.equals("OrderHistory")){
            deliverOrder.setEnabled(false);
            deliverOrder.setVisibility(View.GONE);
        }
        else {
            deliverOrder.setEnabled(true);
            deliverOrder.setVisibility(View.VISIBLE);
            OrderStatus = intent.getStringExtra("OrderStatus");
        }
        for(int i=0;i<orderDetails.length();i++){
            if (orderDetails.charAt(i)=='\n'){
                c++;
                if(c%3==2){
                    temp = orderDetails.substring(pre,i);
                    pre=i+1;
                }
                else if(c%3==0){
                    temp = temp + "\nQuantity: " + orderDetails.substring(pre,i);
                    pre=i+1;
                    mItemName.add(temp);
                }
            }
        }

        temp += "\nQuantity: " + orderDetails.substring(pre,orderDetails.length());
        mItemName.add(temp);

        listView.setAdapter(arrayAdapter);
        for (int i=0;i<mItemName.size();i++){
            int pree=0,flg=0;
            String amou="",quty="";
            for(int j=0;j<mItemName.get(i).length();j++){
                if (flg==1 && mItemName.get(i).charAt(j)=='\n'){
                    amou = mItemName.get(i).substring(pree,j);
                }
                if (flg==0 && mItemName.get(i).charAt(j)>='0' && mItemName.get(i).charAt(j)<='9' ){
                    flg=1;
                    pree=j;
                }
            }
            flg=0;
            for(int j=0;j<mItemName.get(i).length();j++){
                if (mItemName.get(i).charAt(j)=='\n'){
                    flg++;
                }
                if (flg==2 && mItemName.get(i).charAt(j)>='0' && mItemName.get(i).charAt(j)<='9' ){
                    quty = mItemName.get(i).substring(j,mItemName.get(i).length());
                    break;
                }
            }
            amount += Integer.parseInt(amou)*Integer.parseInt(quty);

        }
        TotalAmount.setText(Integer.toString(amount));

        if(!OperationType.equals("OrderHistory") &&  OrderStatus.equals("Cooking")){
            deliverOrder.setText("Ready");
            deliverOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseDatabase db1 = FirebaseDatabase.getInstance();
                    final DatabaseReference table_CurrentOrder = db1.getReference("CurrentOrders");
                    table_CurrentOrder.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (flag == 0) {
                                order = dataSnapshot.child(OrderNumber).getValue(Order.class);
                                flag = 1;
                                order.setStatus("Ready");
                                table_CurrentOrder.child(OrderNumber).setValue(order);
                                OrderStatus="Ready";
                                Intent it=new Intent(getApplicationContext(),CanteenOrderDeliver.class);
                                it.putExtra("CanteenName",CanteenName);
                                it.putExtra("OrderBasic", orderBasics);
                                it.putExtra("OrderNo",orderNo);
                                it.putExtra("CustomerID",orderName);
                                it.putExtra("OrderDetails",orderDetails);
                                it.putExtra("OrderNumber",OrderNumber);
                                it.putExtra("OperationType",OperationType);
                                it.putExtra("CookingInstruction",CookingInstruction);
                                it.putExtra("CanteenAvailable", CanteenAvailable);
                                it.putExtra("PaymentMethod",PaymentMethod);
                                it.putExtra("OrderStatus",OrderStatus);
                                it.putExtra("CookingInstructions",instr);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(it);
                                overridePendingTransition(0, 0);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
        else{
            deliverOrder.setText("Deliver");
            deliverOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseDatabase db1 = FirebaseDatabase.getInstance();
                    final DatabaseReference table_CurrentOrder = db1.getReference("CurrentOrders");
                    table_CurrentOrder.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(flag==0) {
                                order = dataSnapshot.child(OrderNumber).getValue(Order.class);
                                flag = 1;
                                order.setStatus("Delivered");
                                SharedPreferences.Editor editor = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE).edit();
                                editor.putInt("position",type);
                                editor.apply();
                                final DatabaseReference table_DeliveredOrder = db1.getReference("DeliveredOrders");
                                table_DeliveredOrder.child(OrderNumber).setValue(order);
                                final DatabaseReference table_tem = db1.getReference("CurrentOrders");
                                table_tem.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.child(OrderNumber).getRef().removeValue();
                                        Intent intent = new Intent(CanteenOrderDeliver.this, CanteenManager.class);
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(OperationType.equals("OrderHistory")){
            Intent intent = new Intent(CanteenOrderDeliver.this,CanteenOrderStatus.class);
            intent.putExtra("CanteenName",CanteenName);
            intent.putExtra("OperationType",OperationType);
            intent.putExtra("CanteenAvailable", CanteenAvailable);
            finish();
            startActivity(intent);
        }
        else{
            SharedPreferences.Editor editor = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE).edit();
            editor.putInt("position",type);
            editor.apply();
            Intent intent = new Intent(CanteenOrderDeliver.this,CanteenManager.class);
            intent.putExtra("CanteenName",CanteenName);
            intent.putExtra("CanteenAvailable",CanteenAvailable);
            finish();
            startActivity(intent);
        }
    }
}
