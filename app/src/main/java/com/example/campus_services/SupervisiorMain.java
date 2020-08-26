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

import java.util.ArrayList;
import java.util.List;

public class SupervisiorMain extends AppCompatActivity
{
    private Button pencom1,ongocom1,resocom1;
    private FirebaseAuth mAuth;
    //private String userID;
    private DatabaseReference table_user;
    private ValueEventListener listener1;
    private ListView pencomsuper;
    private DatabaseReference mDatabase;
    private ArrayList<String> pencomarrlist,mItem, availability,pencomarrlist1;
    private ArrayAdapter<String> arrayAdapter;
    private String currusername;
    private String curruserid;
    private String currcomid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisior_main);
        pencom1=(Button) findViewById(R.id.pen1);
        ongocom1=(Button) findViewById(R.id.ongo1);
        pencomsuper=(ListView) findViewById(R.id.pencomsuper);
        mAuth = FirebaseAuth.getInstance();
        resocom1=(Button) findViewById(R.id.reso1);
        mDatabase = FirebaseDatabase.getInstance().getReference("PendingComplains");
        pencomarrlist=new ArrayList<>();
        pencomarrlist1=new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.dish_info,R.id.dishnameid,pencomarrlist);
        pencomsuper.setAdapter(arrayAdapter);
        if(mAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    datapencom item = ds.getValue(datapencom.class);
                    //currcomid= item.getComplain_id();
                    pencomarrlist1.add(item.getUser_name()+"\n"+item.getUser_id()+"\n"+item.getComplain_type()+"\n"+item.getComplain()+"\n"+item.getLocation()+"\n"+item.getComplain_id()+"\n"+item.getImage_URL()+"\n");
                    pencomarrlist.add("Student Name:-"+item.getUser_name()+"\n"+"Student ID:-"+item.getUser_id()+"\n"+"Complain Type:-"+item.getComplain_type()+"\n"+"Complain:-"+item.getComplain()+"\n"+"Location:-"+item.getLocation()+"\n");
                }
                pencomsuper.setAdapter(arrayAdapter);
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pencom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SupervisiorMain.this,SupervisiorMain.class);
                startActivity(intent);
            }
        });
        ongocom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SupervisiorMain.this,SupervisorMainOngo.class);
                startActivity(intent);
            }
        });
        resocom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SupervisiorMain.this,SupervisorMainReso.class);
                startActivity(intent);
            }
        });
        pencomsuper.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent1 = new Intent(getApplicationContext(), PenToOngoCom.class);

                intent1.putExtra("CurrComId",pencomarrlist1.get(position));
                /*intent1.putExtra("CurrentDish", pencomarrlist.get(position));
                intent1.putExtra("OrderString", OrderString);
                intent1.putExtra("CanteenName",CanteenName);*/
                finish();
                startActivity(intent1);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_page,menu);

        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuLogout:
                //table_user.removeEventListener(listener1);
                mAuth.signOut();
                Intent intent = new Intent(this, Login_Activity.class);
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
