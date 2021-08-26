package com.example.tallnow.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tallnow.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings_Activity extends AppCompatActivity {

    Button account_delete, change_password, update;
    FirebaseUser firebaseUser;
    EditText oldPswd, newPswd, newConfirmPswd;
    RelativeLayout relativeLayout;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();

        change_password.setOnClickListener(v -> {
            change_password.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        });
        update.setOnClickListener(v -> {
            String old = oldPswd.getText().toString();
            String newpw = newPswd.getText().toString();
            String newconfirm = newConfirmPswd.getText().toString();
            if (TextUtils.isEmpty(old)) {
                oldPswd.setError("Please enter old password!");
            } else if (TextUtils.isEmpty(newpw) && TextUtils.isEmpty(newconfirm)) {
                newPswd.setError("Please enter password!");
            } else if (!newpw.equals(newconfirm)) {
                Toast.makeText(Settings_Activity.this, "Please check your password!", Toast.LENGTH_SHORT).show();
                oldPswd.setText("");
                newPswd.setText("");
                newConfirmPswd.setText("");
            } else if (newpw.length() < 8) {
                newPswd.setError("Password at least 8 digits ");
                newConfirmPswd.setError("");
            } else {
                changePassword(old, newconfirm);
            }
        });
        account_delete.setOnClickListener(v -> deleteAccount());
    }

    private void changePassword(String old, String confirmpas) {
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), old);
        firebaseUser.reauthenticate(credential).addOnSuccessListener(aVoid -> firebaseUser.updatePassword(confirmpas)
                .addOnSuccessListener(aVoid1 -> {
                    Toast.makeText(Settings_Activity.this, "Password Updated.....", Toast.LENGTH_SHORT).show();
                    change_password.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Settings_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })).addOnFailureListener(e -> Toast.makeText(Settings_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }
    private void deleteAccount()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("");
        final EditText input=new EditText(this);
        RelativeLayout.LayoutParams relativeLayout=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(relativeLayout);
        builder.setView(input);
        builder.setPositiveButton("Delete", (dialog, which) -> {
            String password=input.getText().toString();
            final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential=EmailAuthProvider
                    .getCredential(user.getEmail(),password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> user.delete().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Account deleted successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Settings_Activity.this, Start_Activity.class));
                        }
                    }));
        }).setNegativeButton("Cancel",null);
        builder.show();
    }

    private void initViews(){
        account_delete = findViewById(R.id.delete_button);
        change_password = findViewById(R.id.change_btn);
        oldPswd = findViewById(R.id.old_password);
        newPswd = findViewById(R.id.new_password);
        newConfirmPswd = findViewById(R.id.confirm_password);
        update = findViewById(R.id.update);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        relativeLayout=findViewById(R.id.change_password_layout);
    }
    @Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Caution!");
        alert.setMessage("1. Delete account not to be recovered!" + "\n" + "2. Be carefully to change your password");
        alert.setPositiveButton("Okay", (dialog, which) -> Toast.makeText(Settings_Activity.this, "Be Careful....", Toast.LENGTH_SHORT).show());
        alert.show();
        relativeLayout.setVisibility(View.GONE);
    }
}