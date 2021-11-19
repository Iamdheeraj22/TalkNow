package com.example.tallnow.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.tallnow.Methods.Methods;
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

    CircleImageView circleImageView,mood_Emoji;
    TextView username,about,display_email;
    EditText aboutchange;
    Button about_button,moodButton;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    Methods methods=new Methods();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        getAndSetInfo();
        methods.setTheEmoji(mood_Emoji,firebaseUser,User_Profile_Activity.this);
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
        moodButton.setOnClickListener(v -> showMoodAlertDialog());
        registerForContextMenu(about);
    }

    private void showMoodAlertDialog () {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("AlertDialog");
        String[] items = {"HAPPY","SAD","CRY","LAUGH"};
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                if(which==0){
                    String mood="HAPPY";
                    moodUpdating(mood);
                }else if(which==1){
                    String mood="SAD";
                    moodUpdating(mood);
                }else if(which==2){
                    String mood="CRY";
                    moodUpdating(mood);
                }else if(which==3){
                    String mood="LAUGH";
                    moodUpdating(mood);
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initViews () {
        mood_Emoji=findViewById(R.id.mood_Emoji);
        circleImageView =findViewById(R.id.profile);
        username =findViewById(R.id.profile_username);
        about=findViewById(R.id.about);
        moodButton=findViewById(R.id.moodListButton);
        aboutchange=findViewById(R.id.changeabout);
        about_button=findViewById(R.id.about_btn);
        display_email=findViewById(R.id.display_email);
        progressDialog=new ProgressDialog(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
            databaseReference.child("about").setValue("Available").addOnSuccessListener(aVoid ->
                    Toast.makeText(User_Profile_Activity.this,"Default Status update successfully",Toast.LENGTH_SHORT).show()).
                    addOnFailureListener(e -> Toast.makeText(User_Profile_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show());
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
                    Glide.with(getApplicationContext()).load(user.getImageurl()).into(circleImageView);

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
            databaseReference.child("about").setValue(aboutstatus).addOnSuccessListener(aVoid ->
                    Toast.makeText(User_Profile_Activity.this,"Status update successfully",Toast.LENGTH_SHORT).
                            show()).addOnFailureListener(e ->
                    Toast.makeText(User_Profile_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show());
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
}