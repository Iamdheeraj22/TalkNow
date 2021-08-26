package com.example.tallnow.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.tallnow.Classes.User;
import com.example.tallnow.R;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile_Activity extends AppCompatActivity {

    CircleImageView circleImageView,img_on,img_off;
    TextView username,about,display_email;
    EditText aboutchange;
    Button about_button;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    List<String> moodNames;
    Spinner spinner;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        getAndSetInfo();

        circleImageView.setOnClickListener(v -> {
            Intent intent=new Intent(User_Profile_Activity.this, profile_photo_activity.class);
            startActivity(intent);
        });
        about_button.setOnClickListener(v -> {
            String aboutstatus=aboutchange .getText().toString();
            changeAbout(aboutstatus);
            aboutchange.setVisibility(View.GONE);
            about_button.setVisibility(View.GONE);
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                moodUpdating(item);
            }
            @Override
            public void onNothingSelected (AdapterView<?> parent) {
                //Nothing
            }
        });
        registerForContextMenu(about);
    }

    private void initViews () {
        img_on=findViewById(R.id.img_on);
        img_off=findViewById(R.id.img_off);
        circleImageView =findViewById(R.id.profile);
        username =findViewById(R.id.profile_username);
        about=findViewById(R.id.about);
        aboutchange=findViewById(R.id.changeabout);
        about_button=findViewById(R.id.about_btn);
        display_email=findViewById(R.id.display_email);
        progressDialog=new ProgressDialog(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setUpSpinner();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,v.getId(),0,"Clear About");
        menu.add(0,v.getId(),0,"Change About");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle()=="Clear About")
        {
            databaseReference.child("about").setValue("Available").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(User_Profile_Activity.this,"Default Status update successfully",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(User_Profile_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show());
            return true;
        }
        else  if (item.getTitle()=="Change About"){
            aboutchange.setVisibility(View.VISIBLE);
            about_button.setVisibility(View.VISIBLE);
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Setting) {
            startActivity(new Intent(getApplicationContext(), Settings_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }else if(item.getItemId()==R.id.AboutUs){
            startActivity(new Intent(getApplicationContext(), AppInfo_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        return false;
    }

    //Get and set the information of user
    private void getAndSetInfo(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getApplicationContext()==null){
                    return;
                }
                User user = snapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                about.setText(user.getAbout());
                display_email.setText(user.getEmail());
                if (user.getImageurl().equals("default")) {
                    circleImageView.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                    Glide.with(User_Profile_Activity.this).load(user.getImageurl()).into(circleImageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_Profile_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Change About
    private void changeAbout(String aboutstatus){
        if(aboutstatus.equals(""))
        {
            Toast.makeText(User_Profile_Activity.this,"Please type your status!",Toast.LENGTH_SHORT).show();
        }
        else{
            databaseReference.child("about").setValue(aboutstatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(User_Profile_Activity.this,"Status update successfully",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(User_Profile_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void moodUpdating(String mood){
        progressDialog.setMessage("Mood Updating.....");
        progressDialog.show();
        databaseReference.child("mood").setValue(mood).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                progressDialog.dismiss();
                Toast.makeText(User_Profile_Activity.this, "Mood Updated...", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(User_Profile_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(User_Profile_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setUpSpinner(){
        moodNames=new ArrayList<>();
        moodNames.add("HAPPY");
        moodNames.add("SAD");
        moodNames.add("CRY");
        moodNames.add("LAUGH");
        spinner=findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, moodNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}