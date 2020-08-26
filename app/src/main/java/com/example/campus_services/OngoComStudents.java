package com.example.campus_services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OngoComStudents extends AppCompatActivity
{
    private TextView temp;
    private String CurrCom;
    private String Comid,status;
    private Button btn;
    private DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongo_com_students);
        //temp=(TextView) findViewById(R.id.temp);
        Intent intent = getIntent();
        btn=(Button) findViewById(R.id.statusbystudent);
        CurrCom = intent.getStringExtra("CurrCom");
        dbref= FirebaseDatabase.getInstance().getReference("OngoingComplains");
        int i,in;
        for (i=0;i<CurrCom.length();i++)
        {
            if(CurrCom.charAt(i)=='\n')
            {
                //username=CurrComId.substring(0,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrCom.charAt(i)=='\n')
            {
                //userid=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrCom.charAt(i)=='\n')
            {
                //comtype=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrCom.charAt(i)=='\n')
            {
                Comid=CurrCom.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrCom.charAt(i)=='\n')
            {
                status=CurrCom.substring(in,i);
                break;
            }
        }
        //temp.setText(status);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fun();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),PreviousComplains.class);
        finish();
        startActivity(intent);
    }
    public void fun()
    {
        if(status.equals("Not completed"))
        {
            dbref.child(Comid).child("status_by_student").setValue("Completed");
            Toast.makeText(this,"This complain's status has been changed to Completed...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"This complain's status has already been changed by you...",Toast.LENGTH_SHORT).show();
        }
    }

}
