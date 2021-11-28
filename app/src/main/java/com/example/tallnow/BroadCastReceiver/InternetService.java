package com.example.tallnow.BroadCastReceiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tallnow.R;

public class InternetService extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean onConnectivity=intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(onConnectivity){
                Toast.makeText(context, "Internet connection is not available....", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void isCheck(Context context,Intent intent){
//        AlertDialog alertDialog=new AlertDialog.Builder(context).create();
//        View view= LayoutInflater.from(context).inflate(R.layout.internet_connection_alertbox,null,false);
//        alertDialog.setView(view);
//        boolean onConnectivity=intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//        if(onConnectivity){
//            alertDialog.setCancelable(false);
//            alertDialog.show();
//            Button alertRetryButton=view.findViewById(R.id.alertRetryButton);
//            alertRetryButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    isCheck(context, intent);
//                }
//            });
//        }else{
//            alertDialog.dismiss();
//        }
//    }
}
