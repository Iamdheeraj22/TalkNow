package com.example.tallnow.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallnow.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword_Activity extends AppCompatActivity {

    EditText email;
    TextView forget_password;
    ProgressDialog progressDialog;
    LinearLayout layout;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();
        forget_password.setOnClickListener(v -> sendResetPassword()
                );
        layout.setOnClickListener(v->{
            startActivity(new Intent(ForgetPassword_Activity.this,Login_Activity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        });
    }
    private void initViews ()
    {
        email=findViewById(R.id.forget_email);
        forget_password=findViewById(R.id.forget_submit);
        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        layout=findViewById(R.id.backLinerLyout);
    }
    private void sendResetPassword(){
        progressDialog.setTitle("Please wait few seconds!");
        String txt_email=email.getText().toString();
        if(txt_email.equals("")){
            Toast.makeText(ForgetPassword_Activity.this,"Please enter the your email!",Toast.LENGTH_SHORT).show();
        }else
        {
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassword_Activity.this,"Please check your email!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(ForgetPassword_Activity.this, Login_Activity.class));
                }else {
                    progressDialog.dismiss();
                    String error=task.getException().getMessage();
                    Toast.makeText(ForgetPassword_Activity.this,error,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgetPassword_Activity.this,Login_Activity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}