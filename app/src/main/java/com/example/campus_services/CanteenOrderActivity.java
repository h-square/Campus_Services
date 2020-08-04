package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CanteenOrderActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvBalance;
    private ListView CanteenList;
    private ArrayList<String> canteens,available;
    private ArrayAdapter<String> arrayAdapter;
    private String userID,user;

    private DatabaseReference table_user;
    private ValueEventListener listener;
    private ArrayList<String> instr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_order);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        tvBalance = findViewById(R.id.tvBalance);
        CanteenList = findViewById(R.id.CanteenList);
        userID = mAuth.getCurrentUser().getEmail();

        instr = new ArrayList<>();
        if(userID.charAt(0)>='0' && userID.charAt(0)<='9'){
            user="Student";
        }
        else{
            user="Professor";
        }
        int i=0;
        while(userID.charAt(i) != '@') {
            i++;
        }
        if(user.equals("Professor")){
            userID = mAuth.getCurrentUser().getUid();
        }
        else {
            userID = userID.substring(0, i);
        }

        canteens = new ArrayList<>();
        available = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.dish_info,R.id.dishnameid,canteens);
        CanteenList.setAdapter(arrayAdapter);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        getCanteenList();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        table_user = db.getReference("Users").child(user);
        listener = table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(userID).getValue(User.class);
                tvBalance.setText("₹ " + user.getVirtual_Money());
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CanteenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(available.get(position).equals("0")){
                    Toast.makeText(getApplicationContext(),"Canteen currently unavailable!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    table_user.removeEventListener(listener);
                    Intent intent = new Intent(getApplicationContext(),CanteenMenu.class);
                    //intent.putExtra("CanteenNumber",Integer.toString(position+1));
                    intent.putExtra("CanteenName",canteens.get(position).substring(0,canteens.get(position).length() - 10));
                    intent.putExtra("OrderString","");
                    intent.putExtra("InstructionString","");
                    intent.putExtra("CookI",instr);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void getCanteenList(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_canteen = database.getReference("Users/Canteen");
        table_canteen.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Canteen item = ds.getValue(Canteen.class);
                    if (item.getAvailable().equals("1")) {
                        canteens.add(item.getName() + "\nAvailable");
                    } else {
                        canteens.add(item.getName() + "\nUnavailable");
                    }
                    available.add(item.getAvailable());
                }
                CanteenList.setAdapter(arrayAdapter);
                table_canteen.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(user.equals("Student"))
        {
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            finish();
            startActivity(intent);
        }
        else{
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_canteen_order,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuOrderStatus:
                table_user.removeEventListener(listener);
                Intent intent1 = new Intent(this, OrderStatus.class);
                intent1.putExtra("OperationType", "OrderStatus");
                finish();
                startActivity(intent1);
                return true;
            case R.id.menuPastOrders:
                table_user.removeEventListener(listener);
                Intent intent2 = new Intent(this, OrderStatus.class);
                intent2.putExtra("OperationType", "OrderHistory");
                finish();
                startActivity(intent2);
                return true;
            case R.id.menuLogout:
                table_user.removeEventListener(listener);
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
