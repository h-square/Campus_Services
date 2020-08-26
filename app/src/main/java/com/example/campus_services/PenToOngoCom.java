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

public class PenToOngoCom extends AppCompatActivity
{
    private Button btnPenToOngo;
    private ImageButton btnbackPenToOngo;
    //private TextView temp;
    private String CurrComId;
    private DatabaseReference mDatabase;
    private String username,userid,comtype,com,location,comid,image_url;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> pencomarrlist;
    private dataongocom obj;
    private DatabaseReference ref;
    private ImageView imageView;
    private FirebaseStorage fbstorage;
    private StorageReference storageRef;
    private EditText worname,worcontact;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pen_to_ongo_com);
        Intent intent = getIntent();
        CurrComId = intent.getStringExtra("CurrComId");
        btnbackPenToOngo=(ImageButton) findViewById(R.id.btnbackPenToOngo);
        btnPenToOngo=(Button) findViewById(R.id.btnPenToOngo);
        worname=(EditText) findViewById(R.id.workername) ;
        worcontact=(EditText) findViewById(R.id.workercontact);
       // imageView=(ImageView) findViewById(R.id.imageview);
        //temp=(TextView) findViewById(R.id.temp);
        obj = new dataongocom();
        btnbackPenToOngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PenToOngoCom.this, SupervisiorMain.class);
                startActivity(intent1);
            }
        });
        ref = FirebaseDatabase.getInstance().getReference().child("OngoingComplains");
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
                image_url=CurrComId.substring(in,i);
                break;
            }
        }
        //temp.setText(comid);
        fbstorage=FirebaseStorage.getInstance();
        storageRef=fbstorage.getReferenceFromUrl("gs://campusservices-208ad.appspot.com/PendingComplains/Images").child(image_url);
        btnPenToOngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function(comid);
            }
        });
    }
    private void function(String comid)
    {
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("PendingComplains").child(comid);
        if(dbref.equals(null))
        {
            Toast.makeText(this,"This complain's status has already been changed",Toast.LENGTH_SHORT).show();
            return;
        }
        String wname=worname.getText().toString();
        String wcon=worcontact.getText().toString();

        if(TextUtils.isEmpty(wname))
        {
            Toast.makeText(this,"Enter worker's name...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(wcon))
        {
            Toast.makeText(this,"Enter worker's contact number...",Toast.LENGTH_SHORT).show();
        }
        else if(wcon.length()!=10)
        {
            Toast.makeText(this,"Worker's contact number is not correct...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            dbref.removeValue();

            obj.setComplain(com);
            obj.setComplain_id(comid);
            obj.setComplain_type(comtype);
            obj.setLocation(location);
            obj.setUser_name(username);
            obj.setUser_id(userid);
            obj.setImage_URL(image_url);
            obj.setComplain_id(comid);
            obj.setWorker_name(wname);
            obj.setWorker_number(wcon);
            obj.setStatus_by_student("Not completed");
            ref.child(comid).setValue(obj);

            Toast.makeText(this, "Complain's status has been changed...", Toast.LENGTH_SHORT).show();
        }

    }

}
