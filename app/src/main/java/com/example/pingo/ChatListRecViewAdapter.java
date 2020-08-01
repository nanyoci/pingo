package com.example.pingo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import java.util.ArrayList;

public class ChatListRecViewAdapter extends RecyclerView.Adapter<ChatListRecViewAdapter.ViewHolder>{

    private ArrayList<ChatList> chatList = new ArrayList<>();
    private Context context;

    public ChatListRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list, parent, false);
        ChatListRecViewAdapter.ViewHolder viewHolder = new  ChatListRecViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int unread = chatList.get(position).getUnread();
        if(unread > 0) {
            if(unread>99){
                holder.unreadCount.setText("99+");
            }
            else {
                holder.unreadCount.setText("" + chatList.get(position).getUnread());
            }
            holder.unreadCount.setVisibility(View.VISIBLE);
        }
        else{
            holder.unreadCount.setVisibility(View.GONE);
        }
        holder.txtContactUsername.setText(chatList.get(position).getFriend());
        holder.chatListLayout.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(context, ChatActivity.class);
                                                            intent.putExtra("friendUsername", holder.txtContactUsername.getText().toString());
                                                            context.startActivity(intent);
                                                        }
                                                    }
        );
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout chatListLayout;
        private ImageView imgContactProfile;
        private TextView txtContactUsername;
        private TextView unreadCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatListLayout = itemView.findViewById(R.id.contactListLayout);
            imgContactProfile = itemView.findViewById(R.id.imgContactProfile);
            txtContactUsername = itemView.findViewById(R.id.txtContactUsername);
            unreadCount = itemView.findViewById(R.id.unreadCount);
        }
    }

    public void setChatList(ArrayList<ChatList> chatList){
        this.chatList = chatList;
        notifyDataSetChanged();
    }
}
