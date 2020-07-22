package com.example.campus_services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class Student_Profile extends AppCompatActivity {

    private static final int GalleryImageSelector = 0;
    private CircleImageView vStudent_Profile_Image,vStudent_Profile_Image_Selector;
    private TextView vStudent_Profile_User_Name,vStudent_Profile_Phone_Number;
    private Button vStudent_Profile_Logout;
    private ProgressDialog progressDialog;
    private String Student_Id;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String email = firebaseUser.getEmail().toString();
        Student_Id = email.substring(0,9);

        vStudent_Profile_Image = (CircleImageView) findViewById(R.id.Student_Profile_Image);
        vStudent_Profile_Image_Selector = (CircleImageView) findViewById(R.id.Student_Profile_Image_Selector);
        vStudent_Profile_Phone_Number = (TextView) findViewById(R.id.Student_Profile_Phone_Number);
        vStudent_Profile_User_Name = (TextView) findViewById(R.id.Student_Profile_User_Name);
        vStudent_Profile_Logout = (Button) findViewById(R.id.Student_Profile_Logout);
        progressDialog = new ProgressDialog(this);


        databaseReference.child("Users").child("Student").child(Student_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String User_Name = dataSnapshot.child("name").getValue().toString();
                    String Phone_Number = dataSnapshot.child("contact").getValue().toString();
                    String Url = dataSnapshot.child("image_Url").getValue().toString();
                    vStudent_Profile_User_Name.setText(User_Name);
                    vStudent_Profile_Phone_Number.setText(Phone_Number);
                    //Log.d("Url = ",Url);
                    if(!Url.equals("")){
                        Picasso.get().load(Url).into(vStudent_Profile_Image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        vStudent_Profile_Image_Selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GalleryImageSelector);
            }
        });

        vStudent_Profile_User_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(Student_Profile.this);
                View mview = getLayoutInflater().inflate(R.layout.student_profile_edit_text_layout,null);
                mbuilder.setTitle("Edit User Name");

                final EditText vStudent_Profile_Edit_Text = mview.findViewById(R.id.Student_Profile_Edit_Text);
                vStudent_Profile_Edit_Text.setText(vStudent_Profile_User_Name.getText().toString());

                mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = vStudent_Profile_Edit_Text.getText().toString().trim();
                        databaseReference.child("Users").child("Student").child(Student_Id).child("name").setValue(name);
                    }
                });

                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mbuilder.setView(mview);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });

        vStudent_Profile_Phone_Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(Student_Profile.this);
                View mview = getLayoutInflater().inflate(R.layout.student_profile_edit_text_layout,null);
                mbuilder.setTitle("Edit Phone Number");

                final EditText vStudent_Profile_Edit_Text = mview.findViewById(R.id.Student_Profile_Edit_Text);
                vStudent_Profile_Edit_Text.setInputType(InputType.TYPE_CLASS_PHONE);
                vStudent_Profile_Edit_Text.setText(vStudent_Profile_Phone_Number.getText().toString());

                mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = vStudent_Profile_Edit_Text.getText().toString().trim();
                        databaseReference.child("Users").child("Student").child(Student_Id).child("contact").setValue(number);
                    }
                });

                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mbuilder.setView(mview);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });


        vStudent_Profile_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Student_Profile.this,Login_Activity.class);
                finish();
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryImageSelector && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            CropImage.activity(uri)
                      .setAspectRatio(1,1)
                      .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult  result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                progressDialog.setTitle("Set Your Profile Image");
                progressDialog.setMessage("Your Profile Image is Uploading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resltUri = result.getUri();
                final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Profile_Images").child("Student").child(Student_Id + ".jpg");
                filepath.putFile(resltUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String DownloadUrl = uri.toString();
                                    databaseReference.child("Users").child("Student").child(Student_Id).child("image_Url")
                                            .setValue(DownloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Student_Profile.this, "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Student_Profile.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                        else{
                            Toast.makeText(Student_Profile.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }

    }
}
