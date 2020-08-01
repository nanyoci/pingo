package com.example.pingo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatRecViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Chat> chats = new ArrayList<>();
    private String myUsername;

    public ChatRecViewAdapter(String myUsername) {
        this.myUsername = myUsername;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chats.get(position);
        if(myUsername.equals(chat.getSender())){
            return 0; //viewType if sender = 0
        }
        return 1;   //viewType if receiver = 1
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;

        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_box_sender, parent, false);
                viewHolder = new ViewHolderSender(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_box_receiver, parent, false);
                viewHolder = new ViewHolderReceiver(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String message = chats.get(position).getMessage();

            switch (holder.getItemViewType()){
                case 0:
                    ((ViewHolderSender) holder).txtChatMsgSender.setText(message);
                    break;
                default:
                    ((ViewHolderReceiver)holder).txtChatMsgReceiver.setText(message);
            }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolderSender extends RecyclerView.ViewHolder{

        private TextView txtChatMsgSender;
        public ViewHolderSender(@NonNull View itemView) {
            super(itemView);
            txtChatMsgSender = itemView.findViewById(R.id.txtChatMsgSender);
        }
    }

    public class ViewHolderReceiver extends  RecyclerView.ViewHolder{

        private TextView txtChatMsgReceiver;
        public ViewHolderReceiver(@NonNull View itemView) {
            super(itemView);
            txtChatMsgReceiver = itemView.findViewById(R.id.txtChatMsgReciever);
        }
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }
}
