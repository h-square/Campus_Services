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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ComplainActivityMain extends AppCompatActivity
{
    private Button uplo, postc, prevc, chop;
    private ImageButton backb;
    private EditText comt1, com1, lo1;
    private ImageView iv;
    private datapencom obj;
    private String user_email;
    private long id = 0;
    private FirebaseAuth mAuth;
    private StorageReference sref;
    private DatabaseReference ref;
    public Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_main);
        chop = (Button) findViewById(R.id.choosep);
        uplo = (Button) findViewById(R.id.upload);
        postc = (Button) findViewById(R.id.postcomplaint);
        prevc = (Button) findViewById(R.id.previouscomplaint);
        backb = (ImageButton) findViewById(R.id.bb);
        comt1 = (EditText) findViewById(R.id.compt);
        com1 = (EditText) findViewById(R.id.com);
        lo1 = (EditText) findViewById(R.id.lo);
        sref = FirebaseStorage.getInstance().getReference("PendingComplains/Images");
        iv = (ImageView) findViewById(R.id.imav);
        obj = new datapencom();
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("PendingComplains");
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    id++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            prevc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent=new Intent(ComplainActivityMain.this,PreviousComplains.class);
                    startActivity(intent);
                }
            });
            backb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ComplainActivityMain.this,HomeActivity.class);
                    startActivity(intent);
                }
            });
        postc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComplainActivityMain.this, ComplainActivityMain.class);
                startActivity(intent);
            }
        });
        chop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chopsepic();
            }
        });
        uplo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadpic();
            }
        });
    }

    private String getExtention(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadpic() {
        String ct = comt1.getText().toString();
        String c = com1.getText().toString();
        String l = lo1.getText().toString();
        user_email = mAuth.getCurrentUser().getEmail();

        String imageid;
        imageid = System.currentTimeMillis() + "." + getExtention(imguri);
        //System.out.println(imageid);
        if (TextUtils.isEmpty(ct))
        {
            Toast.makeText(ComplainActivityMain.this, "Please enter complaint type...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(c))
        {
            Toast.makeText(ComplainActivityMain.this, "Please enter your complaint...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(l)) {
            Toast.makeText(ComplainActivityMain.this, "Please enter appropriate location...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(imageid)) {
            Toast.makeText(ComplainActivityMain.this, "Please enter photo...", Toast.LENGTH_SHORT).show();
        } else {
            obj.setComplain_type(ct);
            obj.setComplain(c);
            obj.setLocation(l);
            obj.setImage_URL(imageid);
            obj.setUser_id(user_email);
            //obj.setEmail_id("shrey");
            ref.child(String.valueOf(id + 1)).setValue(obj);
            Toast.makeText(ComplainActivityMain.this, "Complaint has been submitted...", Toast.LENGTH_SHORT).show();
            StorageReference sref1 = sref.child(imageid);
            sref1.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //Toast.makeText(MainActivity.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
            //fun(ct,c,l);
        }
    }

    private void chopsepic() {
        Intent intent = new Intent();
        intent.setType("Images/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imguri = data.getData();
            iv.setImageURI(imguri);
        }
    }
}