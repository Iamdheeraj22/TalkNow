package com.example.tallnow.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tallnow.Classes.Chat;
import com.example.tallnow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>
{
    public static final int msg_type_left=0;
    public static final int msg_type_right=1;
    private final Context mContext;
    private final List<Chat> mChat;
    private final String imageurl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext,List<Chat> mChat,String imageurl)
    {
        this.mContext=mContext;
        this.mChat=mChat;
        this.imageurl=imageurl;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==msg_type_right)
        {
             View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
             return new ViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(view);
        }
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Chat chat=mChat.get(position);
            holder.show_message.setText(chat.getMessage());
            if(imageurl.equals("default")){
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            }else
            {
                Glide.with(mContext).load(imageurl).into(holder.profile_image);
            }
            if(position==mChat.size()-1){
                if(chat.isIsseen()){
                    holder.seenmessage.setText("Seen");
                }else
                {
                    holder.seenmessage.setText("Delivered");
                }
            }else{
                holder.seenmessage.setVisibility(View.GONE);
            }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_image;
        public TextView show_message;
        public TextView seenmessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            show_message=itemView.findViewById(R.id.show_message);
            seenmessage=itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return msg_type_right;
        }else {
            return msg_type_left;
        }
    }
}
