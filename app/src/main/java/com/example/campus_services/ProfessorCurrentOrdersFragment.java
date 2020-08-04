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
public class ProfessorCurrentOrdersFragment extends Fragment {

    public ProfessorCurrentOrdersFragment() {
        // Required empty public constructor
    }

    private TextView PendingOrderNumber, StatusType;
    private ListView listView;
    private ArrayList<String> mItemName, OrderDetails,saveOrderNumber, CookingInstruction, paymentMethod, OrderNo, Customers, OrderStatus;
    private ArrayAdapter<String> arrayAdapter;
    private String CanteenName, OperationType,table_name,CanteenAvailable;
    private int count=0;
    private ArrayList<ArrayList<String>> all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_professor_current_orders, container, false);

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
        OrderStatus = new ArrayList<>();
        all = new ArrayList<ArrayList<String>>();
        arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.dish_info,R.id.dishnameid,mItemName);
        PendingOrderNumber.setText(Integer.toString(count));

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
                    if(order.getCustomerId().charAt(0)<'0' || order.getCustomerId().charAt(0)>'9') {
                        mItemName.add("Order No: " + order.getOrderNo() + "\n" + order.getCustomerId());
                        OrderNo.add(order.getOrderNo());
                        Customers.add(order.getCustomerId());
                        OrderDetails.add(order.getOrderDetails());
                        saveOrderNumber.add(order.getOrderNo());
                        CookingInstruction.add(order.getOrderDetails());
                        paymentMethod.add(order.getPaymentMethod());
                        all.add(order.getCookingInstruction());
                        OrderStatus.add(order.getStatus());
                        count += 1;
                        PendingOrderNumber.setText(Integer.toString(count));
                    }
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
                editor.putInt("position",2);
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
                intent.putExtra("OrderStatus",OrderStatus.get(position));
                intent.putExtra("CookingInstructions",all.get(position));
                getActivity().finish();
                startActivity(intent);
            }
        });

        onResume();

        return rootView;
    }
}
