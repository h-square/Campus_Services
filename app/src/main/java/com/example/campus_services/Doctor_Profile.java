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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;

public class Doctor_Profile extends AppCompatActivity {

    private static final int GalleryImageSelector = 0;
    private CircleImageView vDoctor_Profile_Image,vDoctor_Profile_Image_Selector;
    private TextView vDoctor_Profile_User_Name,vDoctor_Profile_Phone_Number;
    private Spinner vDoctor_Profile_Slot_Spinner;
    private Button vDoctor_Profile_Logout,vsaveSlotNumberbtn;
    private ProgressDialog progressDialog;
    private ArrayList<String> slotArrayList;
    private ArrayAdapter<String> slotAdapter;

    private DatabaseReference databaseReference;
    private String Uid;
    private long initialSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__profile);
        vDoctor_Profile_Image = (CircleImageView) findViewById(R.id.Doctor_Profile_Image);
        vDoctor_Profile_Image_Selector = (CircleImageView) findViewById(R.id.Doctor_Profile_Image_Selector);
        vDoctor_Profile_User_Name = (TextView) findViewById(R.id.Doctor_Profile_User_Name);
        vDoctor_Profile_Phone_Number = (TextView) findViewById(R.id.Doctor_Profile_Phone_Number);
        vDoctor_Profile_Slot_Spinner = (Spinner) findViewById(R.id.Doctor_Profile_Slot_Spinner);
        vDoctor_Profile_Logout = (Button) findViewById(R.id.Doctor_Profile_Logout);
        vsaveSlotNumberbtn = (Button) findViewById(R.id.saveSlotNumberbtn);
        progressDialog = new ProgressDialog(this);
        slotArrayList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        slotAdapter = new ArrayAdapter<>(Doctor_Profile.this,android.R.layout.simple_spinner_dropdown_item,slotArrayList);
        vDoctor_Profile_Slot_Spinner.setAdapter(slotAdapter);
        hideSlotSavebtn();

        slotArrayList.clear();
        slotArrayList.add("none");
        slotArrayList.add("Slot 1 - (12:30 to 13:30)");
        slotArrayList.add("Slot 2 - (16:30 to 17:30)");
        slotAdapter.notifyDataSetChanged();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.doctor_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.doctor_pending_appointments:
                        finish();
                        startActivity(new Intent(Doctor_Profile.this,Doctor_Pending_Appointments.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.doctor_list_appointments:
                        finish();
                        startActivity(new Intent(Doctor_Profile.this,Doctor_List_Appointments.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.doctor_profile:
                        return true;
                }
                return false;
            }
        });

        databaseReference.child("Users").child("Doctor").child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phoneNumber = dataSnapshot.child("contact").getValue().toString();
                    String url = dataSnapshot.child("image_Url").getValue().toString();
                    initialSelectedPosition = (long)(dataSnapshot.child("slot").getValue());
                    moveDataFromAppointmentToAppointmentHistory();
                    vDoctor_Profile_User_Name.setText(name);
                    vDoctor_Profile_Phone_Number.setText(phoneNumber);
                    vDoctor_Profile_Slot_Spinner.setSelection((int)initialSelectedPosition);
                    if(!url.equals("")){
                        Picasso.get().load(url).into(vDoctor_Profile_Image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vDoctor_Profile_Image_Selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GalleryImageSelector);
            }
        });

        vDoctor_Profile_User_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(Doctor_Profile.this);
                View mview = getLayoutInflater().inflate(R.layout.student_profile_edit_text_layout,null);
                mbuilder.setTitle("Edit User Name");

                final EditText vStudent_Profile_Edit_Text = mview.findViewById(R.id.Student_Profile_Edit_Text);
                vStudent_Profile_Edit_Text.setText(vDoctor_Profile_User_Name.getText().toString());

                mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = vStudent_Profile_Edit_Text.getText().toString().trim();
                        databaseReference.child("Users").child("Doctor").child(Uid).child("name").setValue(name);
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

        vDoctor_Profile_Phone_Number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(Doctor_Profile.this);
                View mview = getLayoutInflater().inflate(R.layout.student_profile_edit_text_layout,null);
                mbuilder.setTitle("Edit Phone Number");

                final EditText vStudent_Profile_Edit_Text = mview.findViewById(R.id.Student_Profile_Edit_Text);
                vStudent_Profile_Edit_Text.setInputType(InputType.TYPE_CLASS_PHONE);
                vStudent_Profile_Edit_Text.setText(vDoctor_Profile_Phone_Number.getText().toString());

                mbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = vStudent_Profile_Edit_Text.getText().toString().trim();
                        databaseReference.child("Users").child("Doctor").child(Uid).child("contact").setValue(number);
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

        vDoctor_Profile_Slot_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(initialSelectedPosition == position) {
                    hideSlotSavebtn();
                }else{
                    databaseReference.child("Users").child("Doctor").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int f = 0;
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                if(!Uid.equals(dataSnapshot1.getKey())){
                                    Doctor doc = dataSnapshot1.getValue(Doctor.class);
                                    if(doc.getSlot() == position){
                                        f = 1;
                                    }
                                }
                            }
                            if(f == 1 && position != 0){
                                Toast.makeText(Doctor_Profile.this,"There is already another doctor for this slot",Toast.LENGTH_SHORT).show();
                                hideSlotSavebtn();
                            }else{
                                showSlotSavebtn();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vsaveSlotNumberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialSelectedPosition = vDoctor_Profile_Slot_Spinner.getSelectedItemPosition();
                databaseReference.child("Users").child("Doctor").child(Uid).child("slot").setValue(initialSelectedPosition);
                hideSlotSavebtn();
            }
        });

        vDoctor_Profile_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Doctor_Profile.this,Login_Activity.class);
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
                final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Profile_Images").child("Doctor").child(Uid + ".jpg");
                filepath.putFile(resltUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String DownloadUrl = uri.toString();
                                    databaseReference.child("Users").child("Doctor").child(Uid).child("image_Url")
                                            .setValue(DownloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Doctor_Profile.this, "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Doctor_Profile.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                        else{
                            Toast.makeText(Doctor_Profile.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }
    }

    public void hideSlotSavebtn(){
        vsaveSlotNumberbtn.setVisibility(View.GONE);
    }

    public void showSlotSavebtn(){
        vsaveSlotNumberbtn.setVisibility(View.VISIBLE);
    }

    private void moveDataFromAppointmentToAppointmentHistory(){
        final String SLOT = "Slot - " + String.valueOf(initialSelectedPosition);
        databaseReference.child("Appointments").child(SLOT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Appointment ap = dataSnapshot1.getValue(Appointment.class);
                        if(CalendarUtils.isPastAppointment(ap.getDate(),ap.getSlot())){
                            String key = databaseReference.child("Appointment_History").push().getKey();
                            databaseReference.child("Appointment_History").child(ap.getStudent_Id()).child(key).setValue(ap);
                            databaseReference.child("Appointments").child(SLOT).child(ap.getStudent_Id()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
