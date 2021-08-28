package com.example.tallnow.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tallnow.Classes.Chat;
import com.example.tallnow.Classes.User;
import com.example.tallnow.Fragments.Chats;
import com.example.tallnow.Fragments.Groups;
import com.example.tallnow.Fragments.Users;
import com.example.tallnow.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView profile;
    TextView uname;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    ImageView logoutImage,emojiImage;
    //Handler handler;
    DatabaseReference databaseReference;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getAndSetInfo();
        setTheEmoji();
        final TabLayout tabLayout=findViewById(R.id.tab_layout);
        final ViewPager viewPager=findViewById(R.id.view_pager);

        floatingActionButton.setOnClickListener(v->
                startActivity(new Intent(getApplicationContext(), User_Profile_Activity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
        setMessageAndTablayout(tabLayout,viewPager);
        logoutImage.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Alert");
            alert.setMessage("Do you want to logout?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Start_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }).setNegativeButton("No",null);
            alert.show();
        });
    }

    private void setTheEmoji ()
    {
        DatabaseReference emoji=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        emoji.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists()){
                       String emojiMood=snapshot.child("mood").getValue().toString();
                       if(emojiMood.equals("HAPPY")){
                           emojiImage.setImageResource(R.drawable.happy);
                       }else if(emojiMood.equals("SAD")){
                           emojiImage.setImageResource(R.drawable.sad);
                       }else if (emojiMood.equals("CRY")){
                           emojiImage.setImageResource(R.drawable.cry);
                       }else if(emojiMood.equals("LAUGH")){
                           emojiImage.setImageResource(R.drawable.laugh);
                       }
                   }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMessageAndTablayout (TabLayout tabLayout, ViewPager viewPager)
    {
        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager());
                int unread=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    assert chat != null;
                    if(chat.getReceiver().equals(firebaseUser.getUid())&& !chat.isIsseen()){
                        unread++;
                    }}
                if(unread==0){
                    viewPagerAdapter.addFragment(new Chats(),"Chats");
                }else {
                    viewPagerAdapter.addFragment(new Chats(),"("+unread+") Chats");
                }

                viewPagerAdapter.addFragment(new Users(),"Users");
                viewPagerAdapter.addFragment(new Groups(),"Groups");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews ()
    {
        //handler=new Handler();
        emojiImage=findViewById(R.id.moodEmoji);
        toolbar=findViewById(R.id.toolbar);
        profile=toolbar.findViewById(R.id.profile_pic1);
        uname=toolbar.findViewById(R.id.uname2);
        logoutImage=toolbar.findViewById(R.id.logoutButton);
        floatingActionButton=findViewById(R.id.floatingButton1);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.main_menu,menu);
    }

    @Override
    public boolean onContextItemSelected (@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Start_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }else if(item.getItemId()==R.id.Profile){
            startActivity(new Intent(getApplicationContext(), User_Profile_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }else if (item.getItemId() == R.id.create_group){
            startActivity(new Intent(MainActivity.this,CreateGroup_Activity.class));
            return true;}
        return false;
    }


    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm)
        {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
    private void status(String status){
        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("Offline");
    }

    //Get and Set the Image and name
    private void getAndSetInfo(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                assert user != null;
                uname.setText(user.getUsername());
                if(user.getImageurl().equals("default"))
                {
                    profile.setImageResource(R.mipmap.ic_launcher_round);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageurl()).into(profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}