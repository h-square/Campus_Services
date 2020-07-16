package com.example.campus_services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemQuantity extends AppCompatActivity {

    private String CanteenName,OrderString,CurrentDish,CurrentActivity;
    private TextView DName,DPrice,qty;
    private Button add,sub,submit;
    private String name,price;
    private int quantity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_quantity);

        Intent intent = getIntent();
        CanteenName = intent.getStringExtra("CanteenName");
        OrderString = intent.getStringExtra("OrderString");
        CurrentDish = intent.getStringExtra("CurrentDish");
        CurrentActivity = intent.getStringExtra("CurrentActivity");

        DName = findViewById(R.id.tvItemName);
        DPrice = findViewById(R.id.tvItemPrice);
        qty = findViewById(R.id.tvQuantity);
        add = findViewById(R.id.btnAddQuantity);
        sub = findViewById(R.id.btnSubQuantity);
        submit = findViewById(R.id.btnSubmitQuantity);

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderString+="\n";
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
                OrderString = OrderString.trim();
                if (quantity!=0){
                    OrderString += "\n" +CurrentDish + "\n" + Integer.toString(quantity) + "\n";
                    OrderString = OrderString.trim();
                }

                Intent intent1 = new Intent(getApplicationContext(), CanteenMenu.class);
                if(CurrentActivity.equals("PlaceOrder")){
                    intent1 = new Intent(getApplicationContext(), PlaceOrder.class);
                }
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("CanteenName",CanteenName);
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
        finish();
        startActivity(i);

    }
}
