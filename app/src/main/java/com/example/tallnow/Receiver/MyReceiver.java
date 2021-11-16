package com.example.tallnow.Receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.tallnow.Activities.MainActivity;
import com.example.tallnow.R;

public class MyReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
         try{
             if(!isOnline(context)){
                 AlertDialog builder=new AlertDialog.Builder(context.getApplicationContext(), R.style.internet_alertdialogbox).create();
                 builder.setCancelable(false);
                 View view= LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.internet_connection_alertbox,null);
                 builder.setView(view);
                 builder.show();
                 Button cancelButton,settingButton;
                 cancelButton=view.findViewById(R.id.alertCancelButton);
                 settingButton=view.findViewById(R.id.alertSettingButton);
                 cancelButton.setOnClickListener(v -> {
                     builder.dismiss();
                     Toast.makeText(context, "Internet is available", Toast.LENGTH_SHORT).show();
                 });
                 settingButton.setOnClickListener(v -> {
                     PopupMenu popupMenu=new PopupMenu(context,settingButton);
                     popupMenu.getMenuInflater().inflate(R.menu.internet_connection,popupMenu.getMenu());
                     popupMenu.setOnMenuItemClickListener(item -> {
                         if(item.getItemId()==R.id.mobileData){
                             context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                             return true;
                         }else if(item.getItemId()==R.id.wifiSetting){
                             context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                             return true;
                         }
                         return false;
                     });
                     popupMenu.show();
                 });
             }
         }catch (NullPointerException e){
             e.printStackTrace();
         }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isOnline(Context context){
        try{
            ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            return (info!=null && info.isConnected());
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
}