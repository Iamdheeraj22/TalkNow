package com.example.tallnow.Methods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tallnow.Activities.MainActivity;
import com.example.tallnow.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Methods
{
    public void setTheEmoji (CircleImageView emojiImage, FirebaseUser firebaseUser, Context context)
    {
        DatabaseReference emoji= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        emoji.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String emojiMood=snapshot.child("mood").getValue().toString();
                    switch (emojiMood) {
                        case "HAPPY":
                            emojiImage.setImageResource(R.drawable.happy);
                            break;
                        case "SAD":
                            emojiImage.setImageResource(R.drawable.sad);
                            break;
                        case "CRY":
                            emojiImage.setImageResource(R.drawable.cry);
                            break;
                        case "LAUGH":
                            emojiImage.setImageResource(R.drawable.laugh);
                            break;
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public String setTimeSession(){
        String timeSession = "";
        Calendar calendar=Calendar.getInstance();
        int time_of_day=calendar.get(Calendar.HOUR_OF_DAY);
        if(time_of_day>=4 && time_of_day<=12){
            timeSession="Good Morning";
        }else if(time_of_day>=12 && time_of_day<16){
            timeSession="Good Afternoon";
        }else if(time_of_day>=16 && time_of_day<21){
            timeSession="Good Evening";
        }else if(time_of_day>21){
            timeSession="Good Night";
        }
        return timeSession;
    }
}
