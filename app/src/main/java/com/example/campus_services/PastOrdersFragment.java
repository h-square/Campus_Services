package com.example.campus_services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastOrdersFragment extends Fragment {

    public PastOrdersFragment() {
        // Required empty public constructor
    }

    private TextView PendingOrderNumber, StatusType;
    private ListView listView;
    private ArrayList<String> mItemName, OrderDetails,saveOrderNumber, CookingInstruction, paymentMethod, OrderNo, Customers;
    private ArrayAdapter<String> arrayAdapter;
    private String CanteenName, OperationType,table_name,CanteenAvailable;
    private int count=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_past_orders, container, false);

        Intent intent = getActivity().getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        OperationType = "Current Orders";
        CanteenAvailable = intent.getStringExtra("CanteenAvailable");

        PendingOrderNumber = (TextView) rootView.findViewById(R.id.tvPendingOrderCanteen);
        StatusType = (TextView) rootView.findViewById(R.id.tvCanteenOrderStatusType);
        listView = (ListView) rootView.findViewById(R.id.lvPendingOrderCanteen);
        mItemName= new ArrayList<>();
        OrderNo = new ArrayList<>();
        Customers = new ArrayList<>();
        CookingInstruction = new ArrayList<>();
        saveOrderNumber = new ArrayList<>();
        OrderDetails= new ArrayList<>();
        paymentMethod = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.dish_info,R.id.dishnameid,mItemName);
        PendingOrderNumber.setText(Integer.toString(count));

//        if (OperationType.equals("OrderHistory")){
//            StatusType.setText("Past Orders: ");
//            table_name = "DeliveredOrders";
//        }
//        else {
//            StatusType.setText("Pending Orders: ");
//            table_name = "CurrentOrders";
//        }

        StatusType.setText("Pending Orders: ");
        table_name = "CurrentOrders";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_curr_orders = database.getReference(table_name);
        final Query query = table_curr_orders.orderByChild("canteenName").equalTo(CanteenName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    mItemName.add("Order No: " + order.getOrderNo() + "\nCustomerID: " + order.getCustomerId());
                    OrderNo.add(order.getOrderNo());
                    Customers.add(order.getCustomerId());
                    OrderDetails.add(order.getOrderDetails());
                    saveOrderNumber.add(order.getOrderNo());
                    CookingInstruction.add(order.getCookingInstruction());
                    paymentMethod.add(order.getPaymentMethod());
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
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE).edit();
                editor.putInt("position",1);
                editor.apply();
                Intent intent = new Intent(getActivity().getApplicationContext(),CanteenOrderDeliver.class);
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
                getActivity().finish();
                startActivity(intent);
            }
        });

        onResume();

        return rootView;
    }
}
