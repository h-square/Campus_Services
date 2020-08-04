package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;

public class ResoPreviousComplains extends AppCompatActivity
{
    private Button pen,ongo,resol,postc,prevc;
    private ListView pencomlistview;
    private FirebaseAuth mAuth;
    private ArrayList<String> pencomarrlist,available;
    private ArrayAdapter<String> arrayAdapter;
    private String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reso_previous_complains);
        pen=(Button) findViewById(R.id.pen);
        ongo=(Button) findViewById(R.id.ongo);
        resol=(Button) findViewById(R.id.reso);
        postc=(Button) findViewById(R.id.postcomplaint1);
        prevc=(Button) findViewById(R.id.previouscomplaint1);
        pencomlistview=(ListView) findViewById(R.id.resocomlistview);

        mAuth = FirebaseAuth.getInstance();
        pencomarrlist = new ArrayList<>();
        available = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.dish_info,R.id.dishnameid,pencomarrlist);
        pencomlistview.setAdapter(arrayAdapter);

        getResolvedComplainsList();
        ongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResoPreviousComplains.this,OngoPreviousComplains.class);
                startActivity(intent);
            }
        });
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResoPreviousComplains.this,PreviousComplains.class);
                startActivity(intent);
            }
        });
        resol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResoPreviousComplains.this,ResoPreviousComplains.class);
                startActivity(intent);
            }
        });
        postc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResoPreviousComplains.this,ComplainActivityMain.class);
                startActivity(intent);
            }
        });
        prevc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResoPreviousComplains.this,ResoPreviousComplains.class);
                startActivity(intent);
            }
        });
    }
    private void getResolvedComplainsList()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_canteen = database.getReference("ResolvedComplains");
        user_email = mAuth.getCurrentUser().getEmail();
        int i=0;
        while(user_email.charAt(i) != '@')
            i++;
        user_email =user_email.substring(0,i);
        //final int c=0;
        table_canteen.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    dataresocom item=ds.getValue(dataresocom.class);
                    if(item.getUser_id().equals(user_email))
                    {
                        //pencomarrlist.add(toString(c));
                        pencomarrlist.add("Complain Type:-"+item.getComplain_type()+"\n"+"Complain:-"+item.getComplain()+"\n"+"Location:-"+item.getLocation()+"\n"+"Complain_ID:-"+item.getComplain_id()+"\n");
                        //pencomarrlist.add(item.getImage_URL());
                    }
                    pencomlistview.setAdapter(arrayAdapter);
                    table_canteen.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}
