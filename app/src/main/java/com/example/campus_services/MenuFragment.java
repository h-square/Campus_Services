package com.example.campus_services;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

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
    private ArrayList<ArrayList<String>> Instructions_set;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_menu, container, false);

        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getUid();

        Intent data = getActivity().getIntent();
        CanteenName = data.getStringExtra("CanteenName");
        CanteenAvailability = data.getStringExtra("CanteenAvailable");

        tvDisplayCanteenName = rootView.findViewById(R.id.tvDisplayCanteenName);
        tvCanteenAvailability = rootView.findViewById(R.id.tvCanteenAvailability);
        tvCanteenBalance = rootView.findViewById(R.id.tvCanteenBalance);
        lvCanteenItems = rootView.findViewById(R.id.lvCanteenItems);
        btnAddItem = rootView.findViewById(R.id.btnAddItem);
        btnChangeAvailability = rootView.findViewById(R.id.btnChangeAvailability);

        tvDisplayCanteenName.setText(CanteenName);
        if(CanteenAvailability.equals("1")) {
            tvCanteenAvailability.setText("Available");
        }
        else{
            tvCanteenAvailability.setText("Unavailable");
        }
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
        Instructions_set = new ArrayList<ArrayList<String>>();
        arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.dish_info,R.id.dishnameid,mItem);
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
                    Instructions_set.add(item.getInstructions());
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
                Intent intent1 = new Intent(getActivity().getApplicationContext(), ItemEdit.class);
                intent1.putExtra("ItemString", mItemName.get(position));
                intent1.putExtra("CanteenName",CanteenName);
                intent1.putExtra("Availability",availability.get(position));
                intent1.putExtra("CanteenAvailable", CanteenAvailability);
                intent1.putExtra("CookingInstructions",Instructions_set.get(position));
                getActivity().finish();
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
                Intent intent = new Intent(getActivity().getApplicationContext(), AddItem.class);
                intent.putExtra("CanteenAvailable", CanteenAvailability);
                intent.putExtra("CanteenName",CanteenName);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }
}
