package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class UserList_Item_Details extends AppCompatActivity {

    private TextView vULID_User_Type,vULID_User_Name;
    private Button vULID_Ban_btn,vULID_UnBan_btn;
    private String onScreenUserUid;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list__item__details);

        vULID_User_Name = (TextView) findViewById(R.id.ULID_User_Name);
        vULID_User_Type = (TextView) findViewById(R.id.ULID_User_Type);
        vULID_Ban_btn = (Button) findViewById(R.id.ULID_Ban_btn);
        vULID_UnBan_btn = (Button) findViewById(R.id.ULID_UnBan_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        vULID_User_Name.setText(bundle.getString(AdminHome.USER_NAME_DETAILS));
        vULID_User_Type.setText(bundle.getString(AdminHome.USER_TYPE_DETAILS));
        onScreenUserUid = bundle.getString(AdminHome.USER_UID);

        databaseReference.child("Users").child(vULID_User_Type.getText().toString()).child(onScreenUserUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    boolean ban = ((boolean) dataSnapshot.child("ban").getValue());
                    if(ban){
                        hideBanButton();
                    }else {
                        showBanButton();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vULID_Ban_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Users").child(vULID_User_Type.getText().toString()).child(onScreenUserUid).child("ban").setValue(true);
                hideBanButton();
            }
        });

        vULID_UnBan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Users").child(vULID_User_Type.getText().toString()).child(onScreenUserUid).child("ban").setValue(false);
                showBanButton();
            }
        });
    }

    public void showBanButton(){
        vULID_Ban_btn.setVisibility(View.VISIBLE);
        vULID_UnBan_btn.setVisibility(View.GONE);
    }

    public void hideBanButton(){
        vULID_Ban_btn.setVisibility(View.GONE);
        vULID_UnBan_btn.setVisibility(View.VISIBLE);
    }
}
