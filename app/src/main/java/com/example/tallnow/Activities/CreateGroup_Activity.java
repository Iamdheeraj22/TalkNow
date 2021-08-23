package com.example.tallnow.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tallnow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroup_Activity extends AppCompatActivity {
    TextView textView;
    EditText groupName,groupDesc;
    Button create,cancel;
    CircleImageView circleImageView;
    DatabaseReference databaseReference;
    String username;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initViews();

        databaseReference= FirebaseDatabase.getInstance().getReference();
        cancel.setOnClickListener(v -> {
            startActivity(new Intent(CreateGroup_Activity.this,MainActivity.class));
            finish();
        });
        create.setOnClickListener(v -> {
            String groupFieldName=groupName.getText().toString();
            String groupFieldDescription=groupDesc.getText().toString();
            if(TextUtils.isEmpty(groupFieldName)){
                groupName.setError("Enter group name!");
            }else if(TextUtils.isEmpty(groupFieldDescription)){
                groupDesc.setError("Enter description");
            }else{
                createGroup(groupFieldName,groupFieldDescription);
            }
        });
    }

    private void initViews () {
        textView=findViewById(R.id.textView6);
        groupName=findViewById(R.id.update_groupname);
        groupDesc=findViewById(R.id.update_groupdesc);
        create=findViewById(R.id.Update_btn);
        cancel=findViewById(R.id.cancel_btn);
        circleImageView=findViewById(R.id.groupimage);
    }

    private void createGroup(String groupfieldname, String groupfielddescription) {
        FirebaseUser firebaseUser;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Groups").child(groupfieldname);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("groupname",groupfieldname);
        hashMap.put("description",groupfielddescription);
        hashMap.put("groupimage","default");
        hashMap.put("createdby",firebaseUser.getUid());
        hashMap.put("search",groupfieldname.toUpperCase());
        databaseReference.setValue(hashMap).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(CreateGroup_Activity.this,"Group created..",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateGroup_Activity.this,MainActivity.class));
                finish();
            }else {
                Toast.makeText(CreateGroup_Activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}