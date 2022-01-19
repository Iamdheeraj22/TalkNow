package com.example.tallnow.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallnow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity
{
    TextView btn3;
    EditText uname,email1,createpassword,confirmpassword,fullname;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        btn3.setOnClickListener(v -> {
            String txt_username=uname.getText().toString();
            String txt_email=email1.getText().toString().trim();
            String create_password=createpassword.getText().toString();
            String confirm_password=createpassword.getText().toString();
            String fullName=fullname.getText().toString();
            if(TextUtils.isEmpty(txt_username)){
                Toast.makeText(Register_Activity.this,"Please enter username",Toast.LENGTH_SHORT).show();
            }else if(fullName.equals("")){
                Toast.makeText(Register_Activity.this,"Please enter fullname",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(txt_email)){
                Toast.makeText(Register_Activity.this,"Please enter email",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(create_password) && TextUtils.isEmpty(confirm_password)){
                Toast.makeText(Register_Activity.this,"Please enter password",Toast.LENGTH_SHORT).show();
            }else if(create_password.length()<7){
                Toast.makeText(Register_Activity.this,"Password atleast 8 digits",Toast.LENGTH_SHORT).show();
            }else if(!confirm_password.equals(create_password)){
                Toast.makeText(Register_Activity.this,"Password doesn't match!",Toast.LENGTH_SHORT).show();
            }else {
                RegisterUser(fullName,txt_username,txt_email,confirm_password);
            }
        });
    }

    private void initViews ()
    {
        btn3=findViewById(R.id.btn3);
        uname=findViewById(R.id.uname);
        email1=findViewById(R.id.email1);
        createpassword=findViewById(R.id.createpassword);
        confirmpassword=findViewById(R.id.confirmpassword);
        firebaseAuth=FirebaseAuth.getInstance();
        fullname=findViewById(R.id.userFullName);
        progressDialog=new ProgressDialog(this);
    }

    private void RegisterUser(String fullName,String username,String email,String password)
    {
        progressDialog.setTitle("Please wait we are creating your account!");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                String UserId=firebaseUser.getUid();
                databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("id",UserId);
                hashMap.put("username",username);
                hashMap.put("email",email);
                hashMap.put("imageurl","default");
                hashMap.put("fullname",fullName);
                hashMap.put("mood","default");
                hashMap.put("status","Offline");
                hashMap.put("user","new");
                hashMap.put("about","Available");
                hashMap.put("search",username.toUpperCase());

                databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                       firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task11 -> {
                            if(task11.isSuccessful())
                            {
                                Intent intent=new Intent(Register_Activity.this, Login_Activity.class);
                                startActivity(intent);
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(Register_Activity.this, task11.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                       });
                    }
                });
            }else
            {
                progressDialog.dismiss();
                Toast.makeText(Register_Activity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register_Activity.this,Login_Activity.class));
        finish();
    }
}