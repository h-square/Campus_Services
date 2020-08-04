package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemQuantity extends AppCompatActivity {

    private String CanteenName,OrderString,CurrentDish,CurrentActivity, InstructionString;
    private TextView DName,DPrice,qty;
    private Button add,sub,submit;
    private String name,price,text;
    private int quantity=0;
    private Spinner mSpinner;
    private ArrayList<String> instructions, instr;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_quantity);

        final Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        OrderString = intent.getStringExtra("OrderString");
        CurrentDish = intent.getStringExtra("CurrentDish");
        CurrentActivity = intent.getStringExtra("CurrentActivity");
        instructions = intent.getStringArrayListExtra("CookingInstructions");
        InstructionString = intent.getStringExtra("InstructionString");
        instr = intent.getStringArrayListExtra("CookI");

        DName = findViewById(R.id.tvItemName);
        DPrice = findViewById(R.id.tvItemPrice);
        qty = findViewById(R.id.tvQuantity);
        add = findViewById(R.id.btnAddQuantity);
        sub = findViewById(R.id.btnSubQuantity);
        submit = findViewById(R.id.btnSubmitQuantity);
        mSpinner = findViewById(R.id.spCookingInstructions);

        for(int i=0;i<CurrentDish.length();i++){
            if (CurrentDish.charAt(i)=='\n'){
                name = CurrentDish.substring(0,i);
            }
            if(CurrentDish.charAt(i)>='0' && CurrentDish.charAt(i)<='9'){
                price = CurrentDish.substring(i,CurrentDish.length());
                break;
            }
        }
        DName.setText(name);
        DPrice.setText(price);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity<10)
                    quantity+=1;
                else
                {
                    Toast.makeText(ItemQuantity.this, "Can't order more items!", Toast.LENGTH_SHORT).show();
                    return;
                }
                qty.setText(Integer.toString(quantity));
                OrderString+="\n";
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>0)
                    quantity-=1;
                qty.setText(Integer.toString(quantity));
                OrderString+="\n";
            }
        });

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,instructions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderString+="\n";
                InstructionString+="\n";
                String temp="";
                int c=0,flag=0,prev=0,pre=0;
                for(int i=0;i<OrderString.length();i++){
                    if(OrderString.charAt(i)=='\n')
                    {
                        c+=1;
                        if(c%3==1 && OrderString.substring(prev,i).equals(name)){
                            flag=1;
                        }
                        else if(c%3==0){
                            if(flag==1) {
                                OrderString = OrderString.substring(0, pre) + OrderString.substring(i+1,OrderString.length());
                                flag=0;
                            }
                            pre=i+1;
                        }
                        prev=i+1;
                    }
                }
                temp="";
                c=0;flag=0;prev=0;pre=0;
                for(int i=0;i<InstructionString.length();i++){
                    if(InstructionString.charAt(i)=='\n')
                    {
                        c+=1;
                        if(c%3==1 && InstructionString.substring(prev,i).equals(name)){
                            flag=1;
                        }
                        else if(c%3==0){
                            if(flag==1) {
                                InstructionString = InstructionString.substring(0, pre) + InstructionString.substring(i+1,InstructionString.length());
                                flag=0;
                            }
                            pre=i+1;
                        }
                        prev=i+1;
                    }
                }
                for(int i=0;i<instr.size();i++){
                    String t=instr.get(i);
                    String nam="";
                    int j=0;
                    while(t.charAt(j) != '\n'){
                        nam+=t.charAt(j);
                        j++;
                    }
                    //Toast.makeText(getApplicationContext(), nam, Toast.LENGTH_SHORT).show();
                    if(nam.equals(name)){
                        instr.remove(i);
                        break;
                    }
                }
                text = mSpinner.getSelectedItem().toString();
                instr.add(name + "\n" + "Price: " + price + "\n" + "Quantity: " + quantity + "   " + text);
                OrderString = OrderString.trim();
                InstructionString = InstructionString.trim();
                if (quantity!=0){
                    OrderString += "\n" +CurrentDish + "\n" + Integer.toString(quantity) + "\n";
                    InstructionString += "\n" + name + "\n" + text + "\n";
                    OrderString = OrderString.trim();
                    InstructionString = InstructionString.trim();
                }

                Intent intent1 = new Intent(getApplicationContext(), CanteenMenu.class);
                if(CurrentActivity.equals("PlaceOrder")){
                    intent1 = new Intent(getApplicationContext(), PlaceOrder.class);
                }
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("CanteenName", CanteenName);
                intent1.putExtra("InstructionString", InstructionString);
                intent1.putExtra("CookI",instr);
                finish();
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,CanteenMenu.class);
        i.putExtra("CanteenName", CanteenName);
        i.putExtra("OrderString", OrderString);
        i.putExtra("InstructionString", InstructionString);
        i.putExtra("CookI",instr);
        finish();
        startActivity(i);

    }
}
