package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CanteenManager extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabsAccessorAdapter mTabsAccessorAdapter;
    private FirebaseAuth mAuth;
    private String CanteenName,CanteenAvailability;

    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_manager);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        CanteenAvailability = intent.getStringExtra("CanteenAvailable");

        mViewPager = findViewById(R.id.canteen_manager_tabs_pager);
        mTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabsAccessorAdapter);

        mTabLayout = findViewById(R.id.canteen_manager_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_canteen_manager,menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int position = prefs.getInt("position", 0);
        mViewPager.setCurrentItem(position);

        SharedPreferences.Editor editor = getSharedPreferences("MySharedPref", MODE_PRIVATE).edit();
        editor.putInt("position", 0);
        editor.apply();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogoutCanteen:
                mAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.menuCanteenOrderHistory:
                Intent intent2 = new Intent(this,CanteenOrderStatus.class);
                intent2.putExtra("OperationType","OrderHistory");
                intent2.putExtra("CanteenName",CanteenName);
                intent2.putExtra("CanteenAvailable", CanteenAvailability);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menuContactFeedbackCanteen:
                Intent intent1 = new Intent(this,CanteenFeedback.class);
                intent1.putExtra("CanteenName",CanteenName);
                intent1.putExtra("CanteenAvailable", CanteenAvailability);
                startActivity(intent1);
                finish();
                return true;
            case R.id.menuScanQR:
                Intent intent = new Intent(this,ReadQR.class);
                intent.putExtra("CanteenName",CanteenName);
                intent.putExtra("OperationType","OrderStatus");
                intent.putExtra("CanteenAvailable", CanteenAvailability);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
