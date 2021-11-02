package com.example.tallnow.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tallnow.R;

public class ForgetPasswordConfirmation extends AppCompatActivity {
    TextView tryanother,skipLater,openEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_confirmation);
        initViews();
        tryanother.setOnClickListener(v->{
            startActivity(new Intent(ForgetPasswordConfirmation.this,ForgetPassword_Activity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        });

        skipLater.setOnClickListener(view -> startActivity(new Intent(ForgetPasswordConfirmation.this,Login_Activity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));

        openEmail.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            startActivity(intent);
        });
    }

    private void initViews() {
        tryanother=findViewById(R.id.tryanother);
        skipLater=findViewById(R.id.skipConfirm);
        openEmail=findViewById(R.id.openEmail);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgetPasswordConfirmation.this,Login_Activity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}