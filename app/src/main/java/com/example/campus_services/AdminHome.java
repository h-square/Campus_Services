package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity implements
        UserListAdapter.UserListAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView vUser_List_RecyclerView;
    private UserListAdapter vUserListAdapter;
    private DatabaseReference databaseReference;
    private boolean isBannedUserListShown;
    private boolean isStudentListShown;
    private boolean isCanteenListShown;
    private boolean isDoctorListShown;
    private boolean isSupervisorListShown;
    private boolean isProfessorListShown;

    public static final String USER_NAME_DETAILS = "name";
    public static final String USER_TYPE_DETAILS = "type";
    public static final String USER_UID = "uidOfUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        vUser_List_RecyclerView = (RecyclerView) findViewById(R.id.User_List_RecyclerView);
        vUserListAdapter = new UserListAdapter(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        vUser_List_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vUser_List_RecyclerView.setAdapter(vUserListAdapter);

        setUpSharedPreferneces();
        setUpData();

    }

    private void setUpData() {
        final ArrayList<String> name1 = new ArrayList<>();
        final ArrayList<String> type1 = new ArrayList<>();
        final ArrayList<String> uids = new ArrayList<>();
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    name1.clear();
                    type1.clear();
                    uids.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        if(!dataSnapshot1.getKey().equals("Admin")) {
                            if(dataSnapshot1.getKey().equals("Student") && isStudentListShown) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    if(isBannedUserListShown == ((boolean)dataSnapshot2.child("ban").getValue())) {
                                        uids.add(dataSnapshot2.getKey());
                                        name1.add(dataSnapshot2.child("name").getValue().toString());
                                        type1.add(dataSnapshot1.getKey());
                                    }
                                }
                            }
                            if(dataSnapshot1.getKey().equals("Canteen") && isCanteenListShown){
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    if(isBannedUserListShown == ((boolean)dataSnapshot2.child("ban").getValue())) {
                                        uids.add(dataSnapshot2.getKey());
                                        name1.add(dataSnapshot2.child("name").getValue().toString());
                                        type1.add(dataSnapshot1.getKey());
                                    }
                                }
                            }
                            if(dataSnapshot1.getKey().equals("Doctor") && isDoctorListShown){
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    if(isBannedUserListShown == ((boolean)dataSnapshot2.child("ban").getValue())) {
                                        uids.add(dataSnapshot2.getKey());
                                        name1.add(dataSnapshot2.child("name").getValue().toString());
                                        type1.add(dataSnapshot1.getKey());
                                    }
                                }
                            }
                            if(dataSnapshot1.getKey().equals("Professor") && isProfessorListShown){
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    if(isBannedUserListShown == ((boolean)dataSnapshot2.child("ban").getValue())) {
                                        uids.add(dataSnapshot2.getKey());
                                        name1.add(dataSnapshot2.child("name").getValue().toString());
                                        type1.add(dataSnapshot1.getKey());
                                    }
                                }
                            }
                            if(dataSnapshot1.getKey().equals("Supervisor") && isSupervisorListShown){
                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                    if(isBannedUserListShown == ((boolean)dataSnapshot2.child("ban").getValue())) {
                                        uids.add(dataSnapshot2.getKey());
                                        name1.add(dataSnapshot2.child("name").getValue().toString());
                                        type1.add(dataSnapshot1.getKey());
                                    }
                                }
                            }
                        }
                    }
                    vUserListAdapter.setUserData(name1,type1,uids);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpSharedPreferneces() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isBannedUserListShown = sharedPreferences.getBoolean(getString(R.string.pref_ban_key),getResources().getBoolean(R.bool.pref_show_true));
        isStudentListShown = sharedPreferences.getBoolean(getString(R.string.pref_Student_key),getResources().getBoolean(R.bool.pref_show_true));
        isProfessorListShown = sharedPreferences.getBoolean(getString(R.string.pref_Professor_key),getResources().getBoolean(R.bool.pref_show_true));
        isCanteenListShown = sharedPreferences.getBoolean(getString(R.string.pref_Canteen_key),getResources().getBoolean(R.bool.pref_show_true));
        isDoctorListShown = sharedPreferences.getBoolean(getString(R.string.pref_Doctor_key),getResources().getBoolean(R.bool.pref_show_true));
        isSupervisorListShown = sharedPreferences.getBoolean(getString(R.string.pref_Supervisor_key),getResources().getBoolean(R.bool.pref_show_true));

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setUpSharedPreferneces();
        setUpData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(String[] currentUser) {
        Intent intent = new Intent(AdminHome.this,UserList_Item_Details.class);
        intent.putExtra(USER_NAME_DETAILS,currentUser[0]);
        intent.putExtra(USER_TYPE_DETAILS,currentUser[1]);
        intent.putExtra(USER_UID,currentUser[2]);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menuLogout:{
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminHome.this,Login_Activity.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.UserListSetting:{
                Intent intent = new Intent(AdminHome.this,UserListSetting.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }


}
