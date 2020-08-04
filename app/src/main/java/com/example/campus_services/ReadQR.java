package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private String CanteenName,OperationType,CanteenAvailable, orderBasics, orderDetails, cookingInstruction, paymentMethod, orderNo, orderName;
    private int flag=0;

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
            //Toast.makeText(getApplicationContext(),"Permission",Toast.LENGTH_LONG).show();
        }

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        Alert(rawResult);
    }

    public void Alert(Result rawResult){
        flag=0;
        final String orderNumber = rawResult.getText().toString().trim();
        for (int i=0; i<orderNumber.length(); i++){
            if (orderNumber.charAt(i)<='0' || orderNumber.charAt(i)>='9'){
                Toast.makeText(getApplicationContext(),"Invalid QR", Toast.LENGTH_SHORT).show();
                mScannerView.resumeCameraPreview(ReadQR.this);
                return;
            }
        }
        Intent intent1 = getIntent();
        CanteenName = intent1.getStringExtra("CanteenName");
        OperationType = intent1.getStringExtra("OperationType");
        CanteenAvailable = intent1.getStringExtra("CanteenAvailable");
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference table_order = db.getReference("CurrentOrders");
        table_order.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.child(orderNumber).getValue(Order.class);

                if (order == null){
                    flag=1;
                    Toast.makeText(getApplicationContext(),"Invalid QR", Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(ReadQR.this);
                }
                else if (!order.getCanteenName().equals(CanteenName)){
                    flag=1;
                    Toast.makeText(getApplicationContext(),"QR of other Canteen", Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(ReadQR.this);
                }
                else {
                    orderBasics = "Order No: " + order.getOrderNo() + "\nCustomerID: " + order.getCustomerId();
                    orderNo = order.getOrderNo();
                    orderName = order.getCustomerId();
                    orderDetails = order.getOrderDetails();
                    cookingInstruction = order.getOrderDetails();
                    paymentMethod = order.getPaymentMethod();
                    Intent intent = new Intent(ReadQR.this,CanteenOrderDeliver.class);
                    intent.putExtra("CanteenName",CanteenName);
                    intent.putExtra("OrderBasic", orderBasics);
                    intent.putExtra("OrderNo",orderNo);
                    intent.putExtra("CustomerID",orderName);
                    intent.putExtra("OrderDetails",orderDetails);
                    intent.putExtra("OrderNumber",orderNumber);
                    intent.putExtra("OperationType",OperationType);
                    intent.putExtra("CookingInstruction",cookingInstruction);
                    intent.putExtra("CanteenAvailable", CanteenAvailable);
                    intent.putExtra("PaymentMethod",paymentMethod);
                    table_order.removeEventListener(this);
                    finish();
                    startActivity(intent);

                }
                table_order.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        Intent intent1 = getIntent();
        CanteenName = intent1.getStringExtra("CanteenName");
        OperationType = intent1.getStringExtra("OperationType");
        CanteenAvailable = intent1.getStringExtra("CanteenAvailable");
        Intent intent = new Intent(ReadQR.this,CanteenManager.class);
        intent.putExtra("OperationType",OperationType);
        intent.putExtra("CanteenName",CanteenName);
        intent.putExtra("CanteenAvailable", CanteenAvailable);
        finish();
        startActivity(intent);
    }
}
