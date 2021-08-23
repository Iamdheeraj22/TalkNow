package com.example.tallnow.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tallnow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword_Activity extends AppCompatActivity {

    EditText email;
    Button forget_password;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();
    }
    private void initViews ()
    {
        email=findViewById(R.id.forget_email);
        forget_password=findViewById(R.id.forget_submit);
        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
    }
    private void sendResetPassword(String txt_email){
        progressDialog.setTitle("Please wait few seconds!");
        if(txt_email.equals("")){
            Toast.makeText(ForgetPassword_Activity.this,"Please enter the your email!",Toast.LENGTH_SHORT).show();
        }else
        {
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassword_Activity.this,"Please check your email!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(ForgetPassword_Activity.this,Login.class));
                }else {
                    progressDialog.dismiss();
                    String error=task.getException().getMessage();
                    Toast.makeText(ForgetPassword_Activity.this,error,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}