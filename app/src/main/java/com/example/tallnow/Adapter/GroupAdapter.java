package com.example.tallnow.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tallnow.Activities.Group_Message_Activity;
import com.example.tallnow.Classes.Group;
import com.example.tallnow.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{
    private final Context mContext;
    private final List<Group> mGroups;

    public GroupAdapter(Context mContext, List<Group> mGroups) {
        this.mContext = mContext;
        this.mGroups = mGroups;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.group_name,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Group group=mGroups.get(position);
            holder.group_name.setText(group.getGroupname());
            if(group.getGroupimage().equals("default")){
                holder.group_image.setImageResource(R.drawable.group_image);
            }else{
                Glide.with(mContext).load(group.getGroupimage()).into(holder.group_image);
            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, Group_Message_Activity.class);
                intent.putExtra("groupname",group.getGroupname());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView group_image;
        public TextView group_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group_image=itemView.findViewById(R.id.groupimage);
            group_name=itemView.findViewById(R.id.update_groupname);
        }
    }
}
