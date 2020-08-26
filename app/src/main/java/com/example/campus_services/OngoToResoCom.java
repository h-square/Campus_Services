package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.os.Bundle;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OngoToResoCom extends AppCompatActivity
{
    private Button btnPenToOngo;
    private ImageButton btnbackPenToOngo;
    private String username,userid,comtype,com,location,comid,CurrComId,asswor,worcon,status;
    private dataresocom obj;
    private DatabaseReference ref;
    //private TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongo_to_reso_com);
        btnbackPenToOngo=(ImageButton) findViewById(R.id.btnbackPenToOngo);
        btnPenToOngo=(Button) findViewById(R.id.btnPenToOngo);
        //temp=(TextView) findViewById(R.id.temp);
        btnbackPenToOngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OngoToResoCom.this, SupervisorMainOngo.class);
                startActivity(intent1);
            }
        });
        obj = new dataresocom();
        Intent intent = getIntent();

        CurrComId = intent.getStringExtra("CurrComId");
        ref = FirebaseDatabase.getInstance().getReference().child("ResolvedComplains");
        //temp.setText(CurrComId);
        int i,in;
        for (i=0;i<CurrComId.length();i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                username=CurrComId.substring(0,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                userid=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                comtype=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                com=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                location=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                comid=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                asswor=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                worcon=CurrComId.substring(in,i);
                break;
            }
        }
        i++;
        in=i;
        for(;;i++)
        {
            if(CurrComId.charAt(i)=='\n')
            {
                status=CurrComId.substring(in,i);
                break;
            }
        }
        //temp.setText(comid);
        btnPenToOngo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                function();
            }
        });
    }
    private void function()
    {
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("OngoingComplains").child(comid);
        if(dbref.equals(null))
        {
            Toast.makeText(this,"This complain's status has already been changed",Toast.LENGTH_SHORT).show();
            return;
        }
        if(status.equals("Not completed"))
            Toast.makeText(this,"This complain's status has not been approved by student",Toast.LENGTH_SHORT).show();
        else {
            dbref.removeValue();


            obj.setComplain(com);
            obj.setComplain_id(comid);
            obj.setComplain_type(comtype);
            obj.setLocation(location);
            obj.setUser_name(username);
            obj.setUser_id(userid);
            ref.child(comid).setValue(obj);

            Toast.makeText(this, "Complain's status has been changed...", Toast.LENGTH_SHORT).show();
        }
    }

}
