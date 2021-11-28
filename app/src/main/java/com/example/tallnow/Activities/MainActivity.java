package com.example.tallnow.Activities;import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.appcompat.app.AlertDialog;import androidx.appcompat.app.AppCompatActivity;import androidx.appcompat.widget.Toolbar;import androidx.fragment.app.Fragment;import androidx.fragment.app.FragmentManager;import androidx.fragment.app.FragmentStatePagerAdapter;import androidx.viewpager.widget.ViewPager;import android.annotation.SuppressLint;import android.content.BroadcastReceiver;import android.content.Intent;import android.content.IntentFilter;import android.content.SharedPreferences;import android.net.ConnectivityManager;import android.os.Bundle;import android.view.ContextMenu;import android.view.MenuItem;import android.view.View;import android.widget.ImageView;import android.widget.TextView;import android.widget.Toast;import com.bumptech.glide.Glide;import com.example.tallnow.BroadCastReceiver.InternetService;import com.example.tallnow.Classes.Chat;import com.example.tallnow.Classes.User;import com.example.tallnow.Fragments.Chats;import com.example.tallnow.Fragments.Groups;import com.example.tallnow.Fragments.Users;import com.example.tallnow.Methods.Methods;import com.example.tallnow.R;import com.google.android.material.floatingactionbutton.FloatingActionButton;import com.google.android.material.tabs.TabLayout;import com.google.firebase.auth.FirebaseAuth;import com.google.firebase.auth.FirebaseUser;import com.google.firebase.database.DataSnapshot;import com.google.firebase.database.DatabaseError;import com.google.firebase.database.DatabaseReference;import com.google.firebase.database.FirebaseDatabase;import com.google.firebase.database.ValueEventListener;import java.util.ArrayList;import java.util.Calendar;import java.util.HashMap;import de.hdodenhof.circleimageview.CircleImageView;public class MainActivity extends AppCompatActivity {    CircleImageView profile;    TextView uname;    Toolbar toolbar;    String timeSession;    FirebaseUser firebaseUser;    ImageView logoutImage,emojiImage;    Methods methods=new Methods();    BroadcastReceiver internetService=new InternetService();    DatabaseReference databaseReference;    FloatingActionButton floatingActionButton;    @Override    protected void onCreate (Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        initViews();        getAndSetInfo();        timeSession=methods.setTimeSession();        //setTheEmoji();        final TabLayout tabLayout=findViewById(R.id.tab_layout);        final ViewPager viewPager=findViewById(R.id.view_pager);        floatingActionButton.setOnClickListener(v->                 startActivity(new Intent(this, User_Profile_Activity.class)));        setMessageAndTablayout(tabLayout,viewPager);        logoutImage.setOnClickListener(v -> {            AlertDialog.Builder alert = new AlertDialog.Builder(this);            alert.setTitle("Alert");            alert.setMessage("Do you want to logout?");            alert.setPositiveButton("Yes", (dialog, which) -> {                SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);                SharedPreferences.Editor editor=sharedPreferences.edit();                editor.putString("check","logout");                editor.apply();                FirebaseAuth.getInstance().signOut();                Intent intent=new Intent(MainActivity.this,Login_Activity.class);                startActivity(intent);                finish();            }).setNegativeButton("No",null);            alert.show();        });    }    private void setMessageAndTablayout (TabLayout tabLayout, ViewPager viewPager)    {        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");        databaseReference.addValueEventListener(new ValueEventListener() {            @Override            public void onDataChange(@NonNull DataSnapshot snapshot) {                ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager());                int unread=0;                for (DataSnapshot dataSnapshot:snapshot.getChildren()){                    Chat chat=dataSnapshot.getValue(Chat.class);                    assert chat != null;                    if(chat.getReceiver().equals(firebaseUser.getUid())&& !chat.isIsseen()){                        unread++;                    }}                if(unread==0){                    viewPagerAdapter.addFragment(new Chats(),"Chats");                }else {                    viewPagerAdapter.addFragment(new Chats(),"("+unread+") Chats");                }                viewPagerAdapter.addFragment(new Users(),"Users");                viewPagerAdapter.addFragment(new Groups(),"Groups");                viewPager.setAdapter(viewPagerAdapter);                tabLayout.setupWithViewPager(viewPager);            }            @Override            public void onCancelled(@NonNull DatabaseError error) {                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();            }        });    }    private void initViews ()    {        //handler=new Handler();        //emojiImage=findViewById(R.id.moodEmoji);        toolbar=findViewById(R.id.toolbar);        profile=toolbar.findViewById(R.id.profile_pic1);        uname=toolbar.findViewById(R.id.uname2);        logoutImage=toolbar.findViewById(R.id.logoutButton);        floatingActionButton=findViewById(R.id.floatingButton1);        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());    }    @Override    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {        super.onCreateContextMenu(menu, v, menuInfo);        getMenuInflater().inflate(R.menu.main_menu,menu);    }    @Override    public boolean onContextItemSelected (@NonNull MenuItem item) {        if (item.getItemId() == R.id.logout) {            FirebaseAuth.getInstance().signOut();            startActivity(new Intent(getApplicationContext(), Login_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));            return true;        }else if(item.getItemId()==R.id.Profile){            startActivity(new Intent(getApplicationContext(), User_Profile_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));            return true;        }else if (item.getItemId() == R.id.create_group){            startActivity(new Intent(MainActivity.this,CreateGroup_Activity.class));            return true;}        return false;    }    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {        private final ArrayList<Fragment> fragments;        private final ArrayList<String> titles;        ViewPagerAdapter(FragmentManager fm)        {            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);            this.fragments=new ArrayList<>();            this.titles=new ArrayList<>();        }        @NonNull        @Override        public Fragment getItem(int position) {            return fragments.get(position);        }        @Override        public int getCount() {            return fragments.size();        }        public void addFragment(Fragment fragment,String title)        {            fragments.add(fragment);            titles.add(title);        }        @Nullable        @Override        public CharSequence getPageTitle(int position) {            return titles.get(position);        }    }    private void status(String status){        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());        HashMap<String , Object> hashMap=new HashMap<>();        hashMap.put("status",status);        databaseReference.updateChildren(hashMap);    }    @Override    protected void onResume() {        super.onResume();        registerService();        status("Online");    }    @Override    protected void onPause() {        super.onPause();        status("Offline");        unregisterReceiver(internetService);    }    //Get and Set the Image and name    private void getAndSetInfo(){        databaseReference.addValueEventListener(new ValueEventListener() {            @SuppressLint("SetTextI18n")            @Override            public void onDataChange(@NonNull DataSnapshot snapshot) {                User user=snapshot.getValue(User.class);                assert user != null;                uname.setText(timeSession+" , "+user.getUsername());                if(user.getImageurl().equals("default"))                {                    profile.setImageResource(R.mipmap.ic_launcher_round);                }else {                    Glide.with(getApplicationContext()).load(user.getImageurl()).into(profile);                }            }            @Override            public void onCancelled(@NonNull DatabaseError error) {                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();            }        });    }//    protected void registerNetwork(){//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){//            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));//        }//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));//        }//    }//    protected void unRegisterNetwork(){//         try {////         }catch (IllegalArgumentException e){//             e.printStackTrace();//         }//    }    @Override    protected void onStart() {        super.onStart();        registerService();    }    @Override    protected void onDestroy() {        super.onDestroy();        unregisterReceiver(internetService);    }    private void registerService(){        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);        registerReceiver(internetService,intentFilter);    }}