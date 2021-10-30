package com.example.tallnow.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tallnow.R;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity
{
    TextView forget_password,btnSignUp,btnlogin;
    EditText email2,password2;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder alert=new AlertDialog.Builder(Login_Activity.this);
        alert.setMessage("If you are new user then first verify your email(Check your email)")
                .setPositiveButton("Okay", (dialog, which) ->
                        Toast.makeText(Login_Activity.this,"Thank you",Toast.LENGTH_SHORT).show());
        AlertDialog alertDialog=alert.create();
        alertDialog.setTitle("Notice");
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(Login_Activity.this, Register_Activity.class));
            finish();
        });
        forget_password.setOnClickListener(v -> {
            Intent intent=new Intent(Login_Activity.this,ForgetPassword_Activity.class);
            startActivity(intent);
            finish();
        });
        btnlogin.setOnClickListener(v -> {
            progressDialog.setMessage("Please wait few seconds..");
            progressDialog.show();
            String txt_email=email2.getText().toString();
            String txt_password=password2.getText().toString();
            if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                progressDialog.dismiss();
                Toast.makeText(Login_Activity.this,"Please fill all required field!",Toast.LENGTH_SHORT).show();
            }else
            {
                firebaseAuth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                            progressDialog.dismiss();
                            Intent intent=new Intent(Login_Activity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Login_Activity.this,"Please verify emailAddress",Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Login_Activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initViews () {
        btnlogin=findViewById(R.id.btnlogin);
        email2=findViewById(R.id.email2);
        password2=findViewById(R.id.password2);
        forget_password=findViewById(R.id.forget);
        firebaseAuth= FirebaseAuth.getInstance();
        btnSignUp=findViewById(R.id.btnSignUp);
        progressDialog=new ProgressDialog(this);
    }
}